package com.goormy.hackathon.lambda;

import com.goormy.hackathon.entity.Follow;
import com.goormy.hackathon.entity.Like;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.repository.JPA.FollowRepository;
import com.goormy.hackathon.repository.JPA.LikeRepository;
import com.goormy.hackathon.repository.JPA.PostRepository;
import com.goormy.hackathon.repository.JPA.UserRepository;
import com.goormy.hackathon.repository.Redis.FollowListRedisRepository;
import com.goormy.hackathon.repository.Redis.LikeRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledFunction implements Consumer<Object>{

    private final LikeRedisRepository likeRedisRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final FollowListRedisRepository followListRedisRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;

    @Override
    public void accept(Object o) {
        dumpToDB();
        migrateDB();
    }

    /**
     * @description Redis에 있는 '좋아요' 정보들을 RDB에 반영하는 함수
     * */
    @Transactional
    public void dumpToDB() {
        // 1. "postlike:{postId} 형식의 모든 key 목록을 불러옴
        Set<String> postLikeKeySet = likeRedisRepository.findAllKeys();

        // 2. Key마다 postId, userId, value를 조회하는 과정
        for (String key: postLikeKeySet) {

            // 2-1. Key로 Hash 자료구조를 조회함. field = userId / value = 1 or -1
            Map<Object, Object> result = likeRedisRepository.findPostLikeByKey(key);

            // 2-2. key를 파싱하여 postId를 구함
            String[] split = key.split(":");
            Long postId = Long.valueOf(split[1]);

            for (Map.Entry<Object, Object> entry : result.entrySet()) {
                // 2-3. field를 형변환하여 userId를 구함
                Long userId = Long.valueOf(String.valueOf(entry.getKey()));
                // 2-4. value를 형변환하여 1 또는 -1 값을 얻게 됨
                Integer value = Integer.valueOf(String.valueOf(entry.getValue()));

                // 3. value 값에 따라 DB에 어떻게 반영할지 결정하여 처리함
                if (value == 1) {      // 3-1. 좋아요를 추가한 상태였다면 RDB에 insert 쿼리 발생
                    User user = userRepository.getReferenceById(userId);
                    Post post = postRepository.getReferenceById(postId);
                    likeRepository.save(new Like(user, post));
                }else if (value == -1) {  // 3-2. 좋아요를 취소한 상태였다면 RDB에 delete 쿼리 발생
                    likeRepository.deleteByPostIdAndUserId(postId, userId);
                }
            }

            // 4. 해당 Key에 대해 RDB에 반영하는 과정을 마쳤으므로,
            likeRedisRepository.delete(key);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void migrateDB() {
        log.info("데이터 이전을 시작합니다.");
        try {
            List<Follow> follows = followListRedisRepository.getAllFollows();
            followRepository.deleteAll();
            followRepository.saveAll(follows);
            log.info("Redis 데이터를 RDBMS로 옮기고 Redis를 초기화했습니다.");
        }
        catch (Exception e) {
            log.error("데이터 이전에 실패했습니다.",e);
        }
    }


}

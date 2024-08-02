package com.goormy.hackathon.lambda;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goormy.hackathon.entity.Like;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.repository.Redis.LikeRedisRepository;
import com.goormy.hackathon.repository.JPA.LikeRepository;
import com.goormy.hackathon.repository.JPA.PostRepository;
import com.goormy.hackathon.repository.JPA.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikeFunction implements Consumer<Object>{

    private final LikeRedisRepository likeRedisRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void accept(Object messageBody) {
        try {
            String messageString = new String((byte[]) messageBody, StandardCharsets.UTF_8);

            Map<String, Object> messageMap = objectMapper.readValue(messageString, Map.class);
            List<Map<String, Object>> records = (List<Map<String, Object>>) messageMap.get("Records");
            String bodyString = (String) records.get(0).get("body");
            Map<String, Object> body = objectMapper.readValue(bodyString, Map.class);

            long userId = ((Number) body.get("userId")).longValue();
            long postId = ((Number) body.get("postId")).longValue();
            String action = (String) body.get("action");

            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다. userId: " + userId));
            Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("존재하지 않는 포스트 입니다. postId: " + postId));

            if ("like".equals(action)) {
                addLike(postId,userId);
                System.out.println("좋아요 성공: " + messageBody);
            } else if ("unlike".equals(action)) {
                cancelLike(postId,userId);
                System.out.println("좋아요 취소 성공: " + messageBody);
            } else {
                System.out.println("존재하지 않는 action입니다 : " + action);
            }

        } catch (Exception e) {
            System.err.println("메시지 전송 실패: " + messageBody);
            e.printStackTrace();
        }
    }

    /**
     * @description 좋아요 정보를 Redis 캐시에 업데이트
     * */
    @Transactional
    public void addLike(Long postId, Long userId) {
        // 캐시에서 좋아요에 대한 정보를 먼저 조회함
        Integer findPostLike = likeRedisRepository.findPostLikeByPostIdAndUserId(postId, userId);

        if (findPostLike == null) {
            // 캐시에 좋아요에 대한 정보가 없다면,
            // Key = postlike:{postId}, Field = {userId}, Value = 1 로 '좋아요' 정보 생성
            likeRedisRepository.set(postId, userId, 1);

        }else if (findPostLike == -1) {
            // '좋아요 취소' 정보가 있는 상태라면
            // '좋아요'를 다시 누른 것이기 때문에 '취소 정보'를 삭제
            likeRedisRepository.delete(postId,userId);
        }
    }

    /**
     * @description 좋아요 취소 정보를 Redis 캐시에 업데이트
     * */
    @Transactional
    public void cancelLike(Long postId, Long userId) {
        // 캐시에서 좋아요 정보를 먼저 조회함
        Integer findPostLike = likeRedisRepository.findPostLikeByPostIdAndUserId(postId, userId);

        // 캐시에 좋아요에 대한 정보가 없다면,
        // Key = postlike:{postId}, Field = {userId}, Value = -1 로 '좋아요 취소' 정보 생성
        if (findPostLike == null) {
            likeRedisRepository.set(postId,userId,-1);
        }else if (findPostLike == 1) {
            // '좋아요'라는 정보가 있는 상태라면
            // '좋아요 취소'를 다시 누른 것이기 때문에 '좋아요' 정보를 삭젳
            likeRedisRepository.delete(postId,userId);
        }
    }

    /**
     * @description 좋아요 정보가 있는지 조회 (1. Redis 조회 2. RDB 조회)
     * */
    public boolean findLike(Long postId, Long userId) {
        // 1. 캐시로부터 '좋아요'에 대한 정보를 조회함
        Integer value = likeRedisRepository.findPostLikeByPostIdAndUserId(postId, userId);

        if (value == null) {          // 캐시에 정보가 없다면 DB에서 조회되는지 여부에 따라 true/false 리턴
            return likeRepository.isExistByPostIdAndUserId(postId, userId);
        }else if (value == -1) {      // 캐시에 '좋아요 삭제' 정보가 있다면 false 리턴
            return false;
        }else{           // 캐시에 '좋아요 추가' 정보가 있다면 true 리턴
            return true;
        }
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

            int likeCount = 0;

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
                    likeCount++;
                }else if (value == -1) {  // 3-2. 좋아요를 취소한 상태였다면 RDB에 delete 쿼리 발생
                    likeRepository.deleteByPostIdAndUserId(postId, userId);
                    likeCount--;
                }
            }

            // 4. 해당 Key에 대해 RDB에 반영하는 과정을 마쳤으므로,
            likeRedisRepository.delete(key);
            Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("존재하지 않는 포스트입니다. postId: " + postId));
            post.setLikeCount(post.getLikeCount() + likeCount);
            postRepository.save(post);
        }
    }
}
package com.goormy.hackathon.service;

import com.goormy.hackathon.converter.LocalDateTimeConverter;
import com.goormy.hackathon.dto.response.GetFeedResponseDto;
import com.goormy.hackathon.entity.Follow;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.redis.entity.PostRedis;
import com.goormy.hackathon.redis.entity.PostSimpleInfo;
import com.goormy.hackathon.redis.entity.UserRedis;
import com.goormy.hackathon.repository.FeedHashtagRedisRepository;
import com.goormy.hackathon.repository.FeedUserRedisRepository;
import com.goormy.hackathon.repository.PostRedisRepository;
import com.goormy.hackathon.repository.PostRepository;
import com.goormy.hackathon.repository.RecentUpdateRedisRepository;
import com.goormy.hackathon.repository.UserRedisRepository;
import com.goormy.hackathon.repository.UserRespository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetFeedService {

    private final UserRespository userRespository;
    private final PostRepository postRepository;

    private final LocalDateTimeConverter localDateTimeConverter;

    private final UserRedisRepository userRedisRepository;
    private final PostRedisRepository postRedisRepository;
    private final RecentUpdateRedisRepository recentUpdateRedisRepository;
    private final FeedUserRedisRepository feedUserRedisRepository;
    private final FeedHashtagRedisRepository feedHashtagRedisRepository;

    // 1. 사용자 정보를 가져옴
    private UserRedis getUser(Long userId) {
        // 캐시 우선 검색
        Optional<UserRedis> userCacheOptional = userRedisRepository.get(userId);
        // 캐시에 없으면 RDB에서 검색
        return userCacheOptional.orElseGet(() -> {
            User user = userRespository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")); // 적절한 예외로 변경 필요
            List<Long> followIdList = user.getFollows().stream()
                .map(Follow::getId)
                .toList();
            // RDB에서 검색하고, UserCache에 추가
            userRedisRepository.set(userId,
                UserRedis.toEntity(user.getId(), user.getName(), user.getPassword(),
                    user.getFollowerCount(), user.getFollowingCount(), followIdList));
            return UserRedis.toEntity(user.getId(), user.getName(), user.getPassword(),
                user.getFollowerCount(), user.getFollowingCount(), followIdList);
        });
    }

    // 2, 3. Push & Pull 방식으로 피드를 가져옴
    private List<PostSimpleInfo> getPushPullFeed(UserRedis userRedis) {
        // 날짜 세팅
        // recentUpdatedTime - 가장 마지막으로 업데이트한 시간이랑 3일 중 현재와 가장 가까운 시간을 기준으로 잡음
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime recentUpdatedTime = recentUpdateRedisRepository.get(
            userRedis.getId()).orElse(now.minusDays(3));

        // *** 2. Push 방식 ***
        // Push 방식으로 저장되어있는 포스트 가져오기 -> Created At 정보도 같이 저장해야함
        // (이유) 한개의 Key에 있는 각 값마다 TTL 설정이 불가능하기 때문
        // feedUser 접근
        List<PostSimpleInfo> feedUserCache = feedUserRedisRepository.getAll(userRedis.getId());
        // push 캐시 가져오고 비우기 & 3일 전 포스트까지만 가져오기
        List<PostSimpleInfo> postSimpleInfoList = new ArrayList<>(feedUserCache.stream()
            .filter(simpleInfo -> localDateTimeConverter.convertToLocalDateTime(
                simpleInfo.getCreatedAt()).isAfter(recentUpdatedTime))
            .toList());

        // *** 3. Pull 방식 ***
        // Pull 방식으로 저장되어있는 인플루언서 포스트 가져오기 -> 사용자가 최근에 업데이트한 시간 이후부터 진행
        // (이유) 한개의 Key에 있는 각 값마다 TTL 설정이 불가능하기 때문
        // user의 follow 리스트를 순회하면서 가져와야함
        // 5000명이 넘는지를 판단해야함 -> popular 캐시에 있으면 되는 것
        // 기본적인 우선순위 정보는 가지고 있어도 좋을듯  -> 꼭 필요한 Post 정보만 가지고올 수 있도록
        // 3일 전 포스트까지만 가져오기
        userRedis.getFollowerIdList().forEach(
            followId -> {
                List<PostSimpleInfo> info = feedHashtagRedisRepository.getAll(followId);
                postSimpleInfoList.addAll(info.stream()
                    //.filter(simpleInfo -> simpleInfo.getCreatedAt().isAfter(recentUpdatedTime))
                    .toList()
                );
            }
        );

        // 최신 업데이트 시간 반영
        recentUpdateRedisRepository.set(userRedis.getId(), now);

        return postSimpleInfoList;
    }

    // 5. size개의 post를 구분
    private List<Long> splitPostSimpleInfoList(List<PostSimpleInfo> postSimpleInfoList,
        int size) {
        // 초기 반환을 위한 Id 리스트
        // TODO: 만약 size가 작으면 인기 게시물에서 가져와야함

        // TODO: 나머지 정렬한 값을 Redis에 저장
        List<PostSimpleInfo> postSimpleInfoListForRedis = postSimpleInfoList.stream()
            .skip(size)
            .toList();

        // 사용자에게 반환할 게시글 리스트
        return postSimpleInfoList.stream()
            .limit(size)
            .map(PostSimpleInfo::getPostId)
            .toList();
    }

    // 5. PostList를 가져옴
    private List<PostRedis> getPostList(List<Long> postIdList) {
        List<PostRedis> postRedisList = new ArrayList<>(postRedisRepository.getAll(postIdList));
        List<Long> postCacheIdList = postRedisList.stream().map(PostRedis::getId).toList();

        // cache에 없는 post id 리스트를 찾음
        List<Long> postIdListNotInCache = postIdList.stream()
            .filter(id -> !postCacheIdList.contains(id)).toList();
        // RDS에서 가져온 이후 Cache에 업데이트
        List<Post> postList = postRepository.findAllByIdIn(postIdListNotInCache);
        postList.forEach(post -> {
            PostRedis postRedis = PostRedis.toEntity(
                post.getId(), post.getContent(), post.getImageUrl(), post.getStar(),
                post.getLikeCount(), post.getUser().getId(),
                post.getPostHashtags().stream().map(hashtag -> hashtag.getHashtag().getName())
                    .toList(), post.getCreatedAt()
            );
            postRedisRepository.set(postRedis.getId(), postRedis);
            postRedisList.add(postRedis);
        });

        // TODO: postId별로 like여부 받아오는 코드 추가

        return postRedisList;
    }

    // TODO: 각 포스트의 가중치를 생각해서 정렬하기 -> 일단은 날짜 기준으로 정렬되어있어서 패스
    public List<GetFeedResponseDto> getFeedList(Long userId, int size) {
        // 1. 사용자 정보 가져오기 (Redis -> RDS)  - 완료
        UserRedis userRedis = getUser(userId);

        // TODO: 2.0 진행 예정

        // 2, 3. Push, Pull로 Feed List 가져오기 & 필터링
        // id, createdAt을 함께 가진 PostSimpleInfo 데이터 리스트
        // 나중에 (1)여기 먼저 조회 & 있으면 반환, 없으면 (2)push/pull 확인 후 부족하면 (3)인기 게시글 반환
        List<PostSimpleInfo> pushPullFeedList = getPushPullFeed(userRedis);

        // 4. 최신순으로 정렬
        List<PostSimpleInfo> sortedPushPullFeedList = pushPullFeedList.stream()
            .sorted(Comparator.comparing(PostSimpleInfo::getCreatedAt))
            .toList();

        // 5. 반환할 데이터와, Redis에 저장할 데이터를 구분하고, Redis에 저장
        List<Long> postIdListForReturn = splitPostSimpleInfoList(sortedPushPullFeedList, size);
        // 5. post를 조회해서 반환
        return getPostList(postIdListForReturn).stream().map(GetFeedResponseDto::toDto).toList();

    }
}

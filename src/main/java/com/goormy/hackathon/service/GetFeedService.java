package com.goormy.hackathon.service;

import com.goormy.hackathon.common.util.LocalDateTimeConverter_DS;
import com.goormy.hackathon.dto.response.GetFeedResponseDto;
import com.goormy.hackathon.entity.Follow;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.redis.entity.PostRedis_DS;
import com.goormy.hackathon.redis.entity.PostSimpleInfo_DS;
import com.goormy.hackathon.redis.entity.UserRedis_DS;
import com.goormy.hackathon.repository.FeedHashtagRedisRepository_DS;
import com.goormy.hackathon.repository.FeedUserRedisRepository_DS;
import com.goormy.hackathon.repository.FeedUserSortRedisRepository_DS;
import com.goormy.hackathon.repository.PostRedisRepository_DS;
import com.goormy.hackathon.repository.PostRepository;
import com.goormy.hackathon.repository.RecentUpdateRedisRepository_DS;
import com.goormy.hackathon.repository.UserRedisRepository_DS;
import com.goormy.hackathon.repository.UserRespository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetFeedService {

    private final UserRespository userRespository;
    private final PostRepository postRepository;

    private final LocalDateTimeConverter_DS localDateTimeConverter;

    private final FeedHashtagRedisRepository_DS feedHashtagRedisRepository;
    private final FeedUserRedisRepository_DS feedUserRedisRepository;
    private final FeedUserSortRedisRepository_DS feedUserSortRedisRepository;
    private final PostRedisRepository_DS postRedisRepository;
    private final RecentUpdateRedisRepository_DS recentUpdateRedisRepository;
    private final UserRedisRepository_DS userRedisRepository;

    // 1. 사용자 정보를 가져옴
    private UserRedis_DS getUser(Long userId) {
        // 캐시 우선 검색
        Optional<UserRedis_DS> userCacheOptional = userRedisRepository.get(userId);
        // 캐시에 없으면 RDB에서 검색
        return userCacheOptional.orElseGet(() -> {
            User user = userRespository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다.")); // 적절한 예외로 변경 필요
            List<Long> followIdList = user.getFollows().stream()
                .map(Follow::getId)
                .toList();
            // RDB에서 검색하고, UserCache에 추가
            userRedisRepository.set(userId,
                UserRedis_DS.toEntity(user.getId(), user.getName(), user.getPassword(),
                    user.getFollowerCount(), user.getFollowingCount(), followIdList));
            return UserRedis_DS.toEntity(user.getId(), user.getName(), user.getPassword(),
                user.getFollowerCount(), user.getFollowingCount(), followIdList);
        });
    }

    // 2, 3. Push & Pull 방식으로 피드를 가져옴
    private List<PostSimpleInfo_DS> getPushPullFeed(UserRedis_DS userRedisDS) {
        // 날짜 세팅
        // recentUpdatedTime - 가장 마지막으로 업데이트한 시간이랑 3일 중 현재와 가장 가까운 시간을 기준으로 잡음
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime recentUpdatedTime = recentUpdateRedisRepository.get(userRedisDS.getId())
            .orElse(now.minusDays(3));

        // *** 2. Push 방식 ***
        // Push 방식으로 저장되어있는 포스트 가져오기 -> Created At 정보도 같이 저장해야함
        // (이유) 한개의 Key에 있는 각 값마다 TTL 설정이 불가능하기 때문
        // feedUser 접근
        List<PostSimpleInfo_DS> feedUserCache = feedUserRedisRepository.getAll(userRedisDS.getId());
        // push 캐시 가져오고 비우기 & 3일 전 포스트까지만 가져오기
        List<PostSimpleInfo_DS> postSimpleInfoDSList = new ArrayList<>(feedUserCache.stream()
            .filter(simpleInfo -> localDateTimeConverter.convertToLocalDateTime(
                simpleInfo.getCreatedAt()).isAfter(recentUpdatedTime))
            .toList());

        // *** 3. Pull 방식 ***
        // Pull 방식으로 저장되어있는 인플루언서 포스트 가져오기 -> 사용자가 최근에 업데이트한 시간 이후부터 진행
        // (이유) 한개의 Key에 있는 각 값마다 TTL 설정이 불가능하기 때문
        // user의 follow 리스트를 순회하면서 가져와야함 & 3일 전 포스트까지만 가져오기
        userRedisDS.getFollowerIdList().forEach(
            followId -> {
                List<PostSimpleInfo_DS> info = feedHashtagRedisRepository.getAll(followId);
                postSimpleInfoDSList.addAll(info.stream()
                    .filter(Objects::nonNull)
                    .filter(simpleInfo -> localDateTimeConverter.convertToLocalDateTime(
                        simpleInfo.getCreatedAt()).isAfter(recentUpdatedTime))
                    .toList()
                );
            }
        );

        // 최신 업데이트 시간 반영
        recentUpdateRedisRepository.set(userRedisDS.getId(), now);

        return postSimpleInfoDSList.stream().filter(Objects::nonNull).toList();
    }

    // 5. size개의 post를 구분
    private List<Long> splitPostSimpleInfoList(UserRedis_DS userRedisDS,
        List<Long> postIdList, int size) {
        // 초기 반환을 위한 Id 리스트

        // 나머지 정렬한 값을 Redis에 저장
        List<Long> postIdListForRedis = postIdList.stream()
            .skip(size).toList();
        postIdListForRedis.forEach(
            postId -> feedUserSortRedisRepository.add(userRedisDS.getId(), postId));

        // 사용자에게 반환할 게시글 리스트
        return postIdList.stream().limit(size).toList();
    }

    // 5. PostList를 가져옴
    private List<PostRedis_DS> getPostList(List<Long> postIdList) {
        List<PostRedis_DS> postRedisDSList = new ArrayList<>(postRedisRepository.getAll(postIdList));

        if (postRedisDSList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> postCacheIdList = postRedisDSList.stream()
            .filter(Objects::nonNull)
            .map(PostRedis_DS::getId)
            .toList();

        // cache에 없는 post id 리스트를 찾음
        List<Long> postIdListNotInCache = postIdList.stream()
            .filter(id -> !postCacheIdList.contains(id)).toList();
        // RDS에서 가져온 이후 Cache에 업데이트
        List<Post> postList = postRepository.findAllByIdIn(postIdListNotInCache);
        postList.forEach(post -> {
            PostRedis_DS postRedisDS = PostRedis_DS.toEntity(
                post.getId(), post.getContent(), post.getImageUrl(), post.getStar(),
                post.getLikeCount(), post.getUser().getId(),
                post.getPostHashtags().stream().map(Hashtag::getName)
                    .toList(), post.getCreatedAt()
            );
            postRedisRepository.set(postRedisDS.getId(), postRedisDS);
            postRedisDSList.add(postRedisDS);
        });

        // TODO: postId별로 like여부 받아오는 코드 추가

        return postRedisDSList.stream().filter(Objects::nonNull).toList();
    }

    // 날짜 기준으로 정렬하는 것으로 우선순위 선정
    @Transactional
    public List<GetFeedResponseDto> getFeedList(Long userId, int size) {
        // 1. 사용자 정보 가져오기 (Redis -> RDS)
        UserRedis_DS userRedisDS = getUser(userId);

        // 2.0 이미 정리해둔 post가 있으면 가져오기
        List<Long> postIdList = feedUserSortRedisRepository.getSome(userId, size);
        if (postIdList.size() == size) {
            return getPostList(postIdList).stream().map(GetFeedResponseDto::toDto)
                .toList();
        } else {
            size -= postIdList.size();
        }

        // 2, 3. Push, Pull로 Feed List 가져오기 & 필터링
        // 나중에 (1)여기 먼저 조회 & 있으면 반환, 없으면 (2)push/pull 확인 후 부족하면 (3)인기 게시글 반환
        List<PostSimpleInfo_DS> pushPullFeedList = getPushPullFeed(userRedisDS);
        // 여기서 Null 체크, 이후 리스트에 들어가는 모든 post는 null이 있을 수 없음

        // 4. 최신순으로 정렬
        List<Long> sortedPushPullFeedList = pushPullFeedList.stream()
            .sorted(Comparator.comparing(PostSimpleInfo_DS::getCreatedAt))
            .map(PostSimpleInfo_DS::getId)
            .toList();

        // 5. 반환할 데이터와, Redis에 저장할 데이터를 구분하고, Redis에 저장
        List<Long> postIdListForReturn = splitPostSimpleInfoList(userRedisDS, sortedPushPullFeedList,
            size);
        // 5. post를 조회해서 반환
        return getPostList(postIdListForReturn).stream().map(GetFeedResponseDto::toDto).toList();

    }
}

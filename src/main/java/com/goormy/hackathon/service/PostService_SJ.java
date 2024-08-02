package com.goormy.hackathon.service;

import com.goormy.hackathon.dto.hashtag.HashtagResponseDto_SJ;
import com.goormy.hackathon.dto.post.PostResponseDto_SJ;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.redis.entity.PostRedis_DS;
import com.goormy.hackathon.repository.JPA.HashtagRepository;
import com.goormy.hackathon.repository.JPA.PostRepository;
import com.goormy.hackathon.repository.JPA.UserRepository;
import com.goormy.hackathon.repository.Redis.PostRedisRepository;
import com.goormy.hackathon.repository.Redis.PostRedisRepository_SJ;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService_SJ {
    private final PostRepository postRepository;
    private final PostRedisRepository_SJ postRedisRepositorySJ;
    private final PostRedisRepository postRedisRepository;
    private final PhotoService photoServiceSJ;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다. "));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. "));
    }

    // 포스트 삭제
    @Transactional
    public void deletePost(Long postId) {
        Post post = findPostById(postId);
        postRepository.delete(post);
        postRedisRepositorySJ.delete(postId);
    }

    // 단일 조회
    // 포스트 단일 조회
    @Transactional(readOnly = true)
    public PostResponseDto_SJ getPost(Long postId) {

        PostRedis_DS redisData = postRedisRepository.get(postId)
                .orElseGet(null);

        if (redisData == null) {
            return getPostInRDB(postId);
        }

        return getPostInRedis(postId, redisData);
    }

    // rdb에서 조회
    private PostResponseDto_SJ getPostInRDB(Long postId) {
        Post post = findPostById(postId);

        // 레디스에 저장

        PostRedis_DS postRedisDS = PostRedis_DS.toEntity(
                post.getId(), post.getContent(), post.getImageUrl(), post.getStar(),
                post.getLikeCount(), post.getUser().getId(),
                post.getPostHashtags().stream().map(Hashtag::getName)
                        .toList(), post.getCreatedAt()
        );
        postRedisRepositorySJ.set(postRedisDS);

        String imageUrl = photoServiceSJ.getCDNUrl(postId, post.getImageUrl());
        List<HashtagResponseDto_SJ> postHashtags = post.getPostHashtags().stream().map(HashtagResponseDto_SJ::toDto).toList();
        User user = findUserById(post.getUser().getId());
        return PostResponseDto_SJ.toDtoFromPost(post, imageUrl, user, postHashtags);
    }


    private PostResponseDto_SJ getPostInRedis(Long postId, PostRedis_DS postRedis) {
        String imageUrl = photoServiceSJ.getCDNUrl(postId, postRedis.getImageUrl());
        User user = findUserById(postRedis.getUserId());


        // TODO hashtag 캐시에서 가져오기
        List<HashtagResponseDto_SJ> postHashtags = postRedis.getPostHashtags()
                .stream().map(hashtagRepository::findByName).map(Optional::get)
                .map(HashtagResponseDto_SJ::toDto).toList();
        return PostResponseDto_SJ.toDtoFromPostRedis(postRedis, imageUrl, user, postHashtags);
    }
}

package com.goormy.hackathon.service;

import com.goormy.hackathon.entity.Like;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.lambda.LikeFunction;
import com.goormy.hackathon.repository.LikeRedisRepository;
import com.goormy.hackathon.repository.LikeRepository;
import com.goormy.hackathon.repository.PostRepository;
import com.goormy.hackathon.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class LikeServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    LikeFunction likeFunction;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    LikeRedisRepository likeRedisRepository;

    @BeforeEach
    void 데이터_주입() {
        User user1 = User.builder()
                .name("test")
                .followerCount(0)
                .followingCount(0)
                .password("abc").build();
        User user2 = User.builder()
                .name("test")
                .followerCount(0)
                .followingCount(0)
                .password("abc").build();
        User user3 = User.builder()
                .name("test")
                .followerCount(0)
                .followingCount(0)
                .password("abc").build();

        userRepository.saveAll(List.of(user1, user2, user3));

        Post post1 = Post.builder()
                .user(user1)
                .likeCount(0)
                .content("null")
                .star(3)
                .build();
        Post post2 = Post.builder()
                .user(user2)
                .likeCount(0)
                .content("null2")
                .star(3)
                .build();
        Post post3 = Post.builder()
                .user(user1)
                .likeCount(0)
                .content("null3")
                .star(3)
                .build();
        Post post4 = Post.builder()
                .user(user2)
                .likeCount(0)
                .content("null4")
                .star(3)
                .build();
        Post post5 = Post.builder()
                .user(user3)
                .likeCount(0)
                .content("null5")
                .star(3)
                .build();

        postRepository.saveAll(List.of(post1, post2, post3, post4, post5));

        Like like = Like.builder()
                .user(user3)
                .post(post4)
                .build();

        likeRepository.save(like);
    }
    @Test
    void 좋아요_추가() {
        // given

        // when
        likeFunction.addLike(2L,1L);

    }

    @Test
    void 좋아요_삭제() {
        // given

        // when
        likeFunction.cancelLike(2L,1L);

    }

    @Test
    void 좋아요여부_조회() {

        // given

        // when
        boolean exist = likeFunction.findLike(4L, 3L);
        boolean noExist = likeFunction.findLike(2L, 3L);

        // then
        Assertions.assertEquals(exist, true);
        Assertions.assertEquals(noExist, false);
    }

    @Test
    void 캐시로부터_DB로_반영() {

        // given
        likeRedisRepository.set(3L, 1L, 1);
        likeRedisRepository.set(3L, 2L, 1);
        likeRedisRepository.set(3L, 3L, 1);
        likeRedisRepository.set(4L, 3L, -1);

        // when
        likeFunction.dumpToDB();

        // then
    }
}
package com.goormy.hackathon.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LikeRedisRepositoryTest {

    @Autowired
    LikeRedisRepository likeRedisRepository;

    @Test
    void addLike() {
    }

    @Test
    void cancelLike() {
    }

    @Test
    void findByKey() {
        likeRedisRepository.findAllKeys();
    }
}
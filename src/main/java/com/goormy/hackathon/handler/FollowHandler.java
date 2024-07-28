//package com.goormy.hackathon.handler;
//
//import org.springframework.cloud.function.adapter.aws.SpringBootStreamHandler;
//import com.goormy.hackathon.repository.FollowRepository;
//import com.goormy.hackathon.repository.HashtagRepository;
//import com.goormy.hackathon.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class FollowHandler implements SpringBootStreamHandler{
//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private HashtagRepository hashtagRepository;
//
//    @Autowired
//    private FollowRepository followRepository;
//
//}

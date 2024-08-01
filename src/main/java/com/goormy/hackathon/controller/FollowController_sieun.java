package com.goormy.hackathon.controller;

import com.goormy.hackathon.dto.HashtagDto_sieun;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.service.FollowService_sieun;
import com.goormy.hackathon.service.UserService_sieun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/followings")
public class FollowController_sieun {

    @Autowired
    private FollowService_sieun followServiceSieun;
    @Autowired
    private UserService_sieun userServiceSieun;

    @GetMapping
    public List<HashtagDto_sieun> getFollowedHashtags(@RequestHeader("userId") Long userId) {
        User user = userServiceSieun.findById(userId);

        List<Hashtag> hashtags = followServiceSieun.getFollowedHashtags(user);

        return hashtags.stream()
                .map(hashtag -> new HashtagDto_sieun(hashtag.getId(), hashtag.getName(), hashtag.getType().toString()))
                .collect(Collectors.toList());
    }
}

package com.goormy.hackathon.controller;

import com.goormy.hackathon.dto.HashtagDto_sieun;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.service.FollowSQSService;
import com.goormy.hackathon.service.FollowService;
import com.goormy.hackathon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FollowController {

    @Autowired
    private FollowSQSService followSQSService;

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @PostMapping("/follow")
    public ResponseEntity<String> follow(@RequestHeader long userId, @RequestParam long hashtagId) {
        followSQSService.sendFollowRequest(userId,hashtagId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/unfollow")
    public ResponseEntity<String> unfollow(@RequestHeader long userId, @RequestParam long hashtagId) {
        followSQSService.sendUnfollowRequest(userId,hashtagId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/followings")
    public List<HashtagDto_sieun> getFollowedHashtags(@RequestHeader("userId") Long userId) {
        User user = userService.findById(userId);

        List<Hashtag> hashtags = followService.getFollowedHashtags(user);

        return hashtags.stream()
                .map(hashtag -> new HashtagDto_sieun(hashtag.getId(), hashtag.getName(), hashtag.getType().toString()))
                .collect(Collectors.toList());
    }


}

package com.goormy.hackathon.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

@Getter
public class FollowListCache_SY implements Serializable {

    @Id
    private String hashtagId;
    private List<Long> userIdList;

}

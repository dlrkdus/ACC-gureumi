package com.goormy.hackathon.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

@Getter
public class FeedHashtagCache_SY implements Serializable {

    @Id
    Long hashtagId;
    private List<FeedSimpleInfo_SY> postList;

}

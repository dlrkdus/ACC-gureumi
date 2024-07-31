package com.goormy.hackathon.redis.entity;

import com.goormy.hackathon.entity.Hashtag;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Getter
public class FollowCountCache implements Serializable {

    @Id
    private Long hashtagId;
    private Integer followCount;

    public FollowCountCache(Hashtag hashtag) {
        this.hashtagId = hashtag.getId();
        this.followCount = 0;
    }

    public String getKey() {
        return "FollowCount:" + hashtagId;
    }

    public String getField() {
        return String.valueOf(hashtagId);
    }

}

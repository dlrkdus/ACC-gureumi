package com.goormy.hackathon.redis.entity;

import com.goormy.hackathon.entity.Post;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

@Getter
public class FeedUserCache implements Serializable {

    @Id
    Long userId;
    private List<FeedSimpleInfo> postList;

    public FeedUserCache(Post post) {
        this.userId = post.getUser().getId();
    }

}

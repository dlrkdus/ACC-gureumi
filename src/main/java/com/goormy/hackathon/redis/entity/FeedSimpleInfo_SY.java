package com.goormy.hackathon.redis.entity;

import com.goormy.hackathon.common.util.LocalDateTimeConverter__SY;
import com.goormy.hackathon.entity.Post;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Getter
public class FeedSimpleInfo_SY implements Serializable {

    @Id
    private Long id;
    private String createdAt;

    public FeedSimpleInfo_SY(Post post) {
        this.id = post.getId();
        this.createdAt = LocalDateTimeConverter__SY.convert(post.getCreatedAt());
    }

}

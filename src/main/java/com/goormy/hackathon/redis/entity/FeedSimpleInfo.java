package com.goormy.hackathon.redis.entity;

import com.goormy.hackathon.common.util.LocalDateTimeConverter;
import com.goormy.hackathon.entity.Post;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Getter
public class FeedSimpleInfo implements Serializable {

    @Id
    private Long id;
    private String createdAt;

    public FeedSimpleInfo(Post post) {
        this.id = post.getId();
        this.createdAt = LocalDateTimeConverter.convert(post.getCreatedAt());
    }

}

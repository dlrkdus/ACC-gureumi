package com.goormy.hackathon.redis.entity;


import com.goormy.hackathon.common.util.LocalDateTimeConverter__SY;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.Post;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

@Getter
public class PostCache_SY implements Serializable {

    @Id
    private Long postId;
    private String content;
    private String imageUrl;
    private Integer star;
    private Integer likeCount;
    private Long userId;
    private List<String> postHashtags;
    private String createdAt;

    public PostCache_SY(Post post) {
        this.postId = post.getId();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();
        this.star = post.getStar();
        this.likeCount = 0;
        this.userId = post.getUser().getId();
        this.postHashtags = post.getPostHashtags().stream()
                .map(Hashtag::getName)
                .toList();
        this.createdAt = LocalDateTimeConverter__SY.convert(post.getCreatedAt());
    }

    public String getKey() {
        return "post:" + postId;
    }

}

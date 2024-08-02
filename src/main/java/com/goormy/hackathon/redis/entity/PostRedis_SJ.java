package com.goormy.hackathon.redis.entity;

import com.goormy.hackathon.common.util.LocalDateTimeConverter__SY;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.Post;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class PostRedis_SJ implements Serializable {

    @Id
    private Long id;
    private String content;
    private Integer star;
    private int likeCount;
    private Long userId;
    private String imageUrl;
    private List<String> postHashtags = new ArrayList<>();
    private String createdAt;


    public static PostRedis_SJ toRedis(Post post) {
        return PostRedis_SJ.builder()
                .id(post.getId())
                .content(post.getContent())
                .star(post.getStar())
                .likeCount(post.getLikeCount())
                .userId(post.getUser().getId())
                .imageUrl(post.getImageUrl())
                .postHashtags(post.getPostHashtags().stream().map(Hashtag::getName).toList())
                .createdAt(LocalDateTimeConverter__SY.convert(post.getCreatedAt()))
                .build();
    }
}

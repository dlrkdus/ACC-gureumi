package com.goormy.hackathon.redis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("post")
@AllArgsConstructor
@Builder
public class PostRedis_DS {

    @Id
    private Long id;

    private String content;
    private String imgUrl;
    private Integer star;
    private Integer likeCount;
    private Long userId;
    private List<String> postHashtags;
    private String createdAt;


    @JsonCreator
    public PostRedis_DS(
        @JsonProperty("id") Long id,
        @JsonProperty("content") String content,
        @JsonProperty("imgUrl") String imgUrl,
        @JsonProperty("star") int star,
        @JsonProperty("likeCount") int likeCount,
        @JsonProperty("userId") Long userId,
        @JsonProperty("postHashtags") List<String> postHashtags,
        @JsonProperty("createdAt") String createdAt) {
        this.id = id;
        this.content = content;
        this.imgUrl = imgUrl;
        this.star = star;
        this.likeCount = likeCount;
        this.userId = userId;
        this.postHashtags = postHashtags;
        this.createdAt = createdAt;
    }

    public static PostRedis_DS toEntity(Long id, String content, String imgUrl, Integer star,
        Integer likeCount, Long userId, List<String> postHashtags, LocalDateTime createdAt) {
        return PostRedis_DS.builder()
            .id(id)
            .content(content)
            .imgUrl(imgUrl)
            .star(star)
            .likeCount(likeCount)
            .userId(userId)
            .postHashtags(postHashtags)
            .createdAt(createdAt.toString())
            .build();
    }
}

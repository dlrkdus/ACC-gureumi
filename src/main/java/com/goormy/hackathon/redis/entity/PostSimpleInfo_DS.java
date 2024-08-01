package com.goormy.hackathon.redis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSimpleInfo_DS {

    private Long id;
    private String createdAt;

    @JsonCreator
    public PostSimpleInfo_DS(
        @JsonProperty("id") Long postId,
        @JsonProperty("createdAt") String createdAt) {
        this.id = postId;
        this.createdAt = createdAt;
    }

    public static PostSimpleInfo_DS toEntity(Long postId, String createdAt) {
        return PostSimpleInfo_DS.builder()
            .id(postId)
            .createdAt(createdAt)
            .build();
    }


}

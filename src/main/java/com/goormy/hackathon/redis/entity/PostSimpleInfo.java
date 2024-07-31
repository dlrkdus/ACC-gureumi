package com.goormy.hackathon.redis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Builder
public class PostSimpleInfo {

    private Long postId;
    private String createdAt;

    @JsonCreator
    public PostSimpleInfo(
        @JsonProperty("postId") Long postId,
        @JsonProperty("createdAt") String createdAt) {
        this.postId = postId;
        this.createdAt = createdAt;
    }

    public static PostSimpleInfo toEntity(Long postId, LocalDateTime createdAt) {
        return PostSimpleInfo.builder()
            .postId(postId)
            .createdAt(createdAt.toString())
            .build();
    }


}

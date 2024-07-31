package com.goormy.hackathon.redis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.goormy.hackathon.common.util.LocalDateTimeConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Builder
public class PostSimpleInfo {

    private Long id;
    private String createdAt;

    @JsonCreator
    public PostSimpleInfo(
        @JsonProperty("id") Long postId,
        @JsonProperty("createdAt") String createdAt) {
        this.id = postId;
        this.createdAt = createdAt;
    }

    public static PostSimpleInfo toEntity(Long postId, String createdAt) {
        return PostSimpleInfo.builder()
            .id(postId)
            .createdAt(createdAt)
            .build();
    }


}

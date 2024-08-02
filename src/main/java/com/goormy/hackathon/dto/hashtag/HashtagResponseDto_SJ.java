package com.goormy.hackathon.dto.hashtag;

import com.goormy.hackathon.entity.Hashtag;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HashtagResponseDto_SJ {

    private Long id;
    private String name;
    private Hashtag.Type type;

    public static HashtagResponseDto_SJ toDto(Hashtag hashtag) {
        return HashtagResponseDto_SJ.builder()
                .id(hashtag.getId())
                .name(hashtag.getName())
                .type(hashtag.getType())
                .build();
    }
}

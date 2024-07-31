package com.goormy.hackathon.dto.hashtag;

import com.goormy.hackathon.entity.Hashtag;

public record PostHashtagRequestDto(
        String name,
        Hashtag.Type type
) {

}

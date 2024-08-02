package com.goormy.hackathon.dto.hashtag;

import com.goormy.hackathon.entity.Hashtag;

public record PostHashtagResponseDto_SY(
        String name,
        Hashtag.Type type
) {

}

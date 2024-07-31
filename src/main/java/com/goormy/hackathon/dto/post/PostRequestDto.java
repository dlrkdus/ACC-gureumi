package com.goormy.hackathon.dto.post;

import com.goormy.hackathon.dto.hashtag.PostHashtagRequestDto;

import java.util.List;

public record PostRequestDto(
        String content,
        String imageUrl,
        Integer star,
        List<PostHashtagRequestDto> postHashtags
) {

}

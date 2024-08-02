package com.goormy.hackathon.dto.post;

import com.goormy.hackathon.dto.hashtag.PostHashtagRequestDto_SY;

import java.util.List;

public record PostRequestDto_SY(
        String content,
        String imageUrl,
        Integer star,
        List<PostHashtagRequestDto_SY> postHashtags
) {

}

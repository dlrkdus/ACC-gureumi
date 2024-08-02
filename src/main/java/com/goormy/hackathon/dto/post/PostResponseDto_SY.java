package com.goormy.hackathon.dto.post;

import com.goormy.hackathon.dto.hashtag.PostHashtagResponseDto_SY;
import com.goormy.hackathon.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDto_SY(
        Long id,
        String content,
        String imageUrl,
        Integer star,
        List<PostHashtagResponseDto_SY> postHashtags,
        LocalDateTime createdAt

) {

    public PostResponseDto_SY(Post post) {
        this(
                post.getId(),
                post.getContent(),
                post.getImageUrl(),
                post.getStar(),
                mapHashtagsToDto(post),
                post.getCreatedAt()
        );
    }

    private static List<PostHashtagResponseDto_SY> mapHashtagsToDto(Post post) {
        return post.getPostHashtags().stream()
                .map(hashtag -> new PostHashtagResponseDto_SY(hashtag.getName(), hashtag.getType()))
                .toList();
    }
}

package com.goormy.hackathon.dto.post;

import com.goormy.hackathon.dto.hashtag.PostHashtagResponseDto;
import com.goormy.hackathon.entity.Post;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDto(
        Long id,
        String content,
        String imageUrl,
        Integer star,
        List<PostHashtagResponseDto> postHashtags,
        LocalDateTime createdAt

) implements Serializable {

    public PostResponseDto(Post post) {
        this(
                post.getId(),
                post.getContent(),
                post.getImageUrl(),
                post.getStar(),
                mapHashtagsToDto(post),
                post.getCreatedAt()
        );
    }

    private static List<PostHashtagResponseDto> mapHashtagsToDto(Post post) {
        return post.getPostHashtags().stream()
                .map(hashtag -> new PostHashtagResponseDto(hashtag.getName(), hashtag.getType()))
                .toList();
    }
}

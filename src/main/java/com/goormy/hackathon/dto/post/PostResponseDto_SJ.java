package com.goormy.hackathon.dto.post;

import com.goormy.hackathon.dto.hashtag.HashtagResponseDto_SJ;
import com.goormy.hackathon.dto.user.UserResponseDto_SJ;
import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.redis.entity.PostRedis_DS;
import com.goormy.hackathon.redis.entity.PostRedis_SJ;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Builder
@Getter
public class PostResponseDto_SJ {

    private Long postId;
    private String content;
    private Integer star;
    private int likeCount;
    private UserResponseDto_SJ user;
    private String imageUrl;
    private List<HashtagResponseDto_SJ> postHashtags = Collections.emptyList();

    public static <T> PostResponseDto_SJ toDtoFromPost(Post post, String imageUrl, User user, List<HashtagResponseDto_SJ> postHashtags) {
        return PostResponseDto_SJ.builder()
                .postId(Long.parseLong(post.getId().toString()))
                .content(post.getContent())
                .star(post.getStar())
                .likeCount(post.getLikeCount())
                .user(UserResponseDto_SJ.builder()
                        .userId(user.getId())
                        .name(user.getName())
                        .build())
                .imageUrl(imageUrl)
                .postHashtags(postHashtags)
                .build();
    }

    public static PostResponseDto_SJ toDtoFromPostRedis(PostRedis_DS post, String imageUrl,
                                                        User user, List<HashtagResponseDto_SJ> postHashtags) {
        return PostResponseDto_SJ.builder()
                .postId(post.getId())
                .content(post.getContent())
                .star(post.getStar())
                .likeCount(post.getLikeCount())
                .user(UserResponseDto_SJ.builder()
                        .userId(user.getId())
                        .name(user.getName())
                        .build())
                .imageUrl(imageUrl)
                .postHashtags(postHashtags)
                .build();
    }
}

//    public void updatePhoto(String imageUrl) {
//        this.imageUrl = imageUrl;



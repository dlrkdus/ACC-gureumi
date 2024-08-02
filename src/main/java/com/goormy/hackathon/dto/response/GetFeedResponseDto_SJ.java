package com.goormy.hackathon.dto.response;

import com.goormy.hackathon.redis.entity.PostRedis_DS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetFeedResponseDto_SJ {

    private Long id;
    private String content;
    private String imgUrl;
    private Integer star;
    private Integer likeCount;
    private Long userId;
    private List<String> postHashtags;

    public static GetFeedResponseDto_SJ toDto(PostRedis_DS postRedisDS, String imageURL) {
        return GetFeedResponseDto_SJ.builder()
            .id(postRedisDS.getId())
            .content(postRedisDS.getContent())
            .imgUrl(imageURL)
            .star(postRedisDS.getStar())
            .likeCount(postRedisDS.getLikeCount())
            .userId(postRedisDS.getUserId())
            .postHashtags(postRedisDS.getPostHashtags())
            .build();
    }
}

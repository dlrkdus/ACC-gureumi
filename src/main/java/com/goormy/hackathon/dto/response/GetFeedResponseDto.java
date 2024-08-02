package com.goormy.hackathon.dto.response;

import com.goormy.hackathon.redis.entity.PostRedis_DS;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetFeedResponseDto {

    private Long id;
    private String content;
    private String imgUrl;
    private Integer star;
    private Integer likeCount;
    private Long userId;
    private List<String> postHashtags;

    public static GetFeedResponseDto toDto(PostRedis_DS postRedisDS, String imgUrl) {
        return GetFeedResponseDto.builder()
            .id(postRedisDS.getId())
            .content(postRedisDS.getContent())
            .imgUrl(imgUrl)
            .star(postRedisDS.getStar())
            .likeCount(postRedisDS.getLikeCount())
            .userId(postRedisDS.getUserId())
            .postHashtags(postRedisDS.getPostHashtags())
            .build();
    }
}

package com.goormy.hackathon.dto.response;

import com.goormy.hackathon.redis.entity.PostRedis;
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

    public static GetFeedResponseDto toDto(PostRedis postRedis) {
        return GetFeedResponseDto.builder()
            .id(postRedis.getId())
            .content(postRedis.getContent())
            .imgUrl(postRedis.getImgUrl())
            .star(postRedis.getStar())
            .likeCount(postRedis.getLikeCount())
            .userId(postRedis.getUserId())
            .postHashtags(postRedis.getPostHashtags())
            .build();
    }
}

package com.goormy.hackathon.redis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
public class UserRedis implements Serializable {

    private Long id;

    private String name;

    private String password;

    private Integer followerCount;

    private Integer followingCount;

    private List<Long> followerIdList;

    @JsonCreator
    public UserRedis(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("password") String password,
        @JsonProperty("followerCount") Integer followerCount,
        @JsonProperty("followingCount") Integer followingCount,
        @JsonProperty("followerIdList") List<Long> followerIdList) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.followerIdList = followerIdList;
    }


    public static UserRedis toEntity(Long id, String name, String password, Integer followerCount,
        Integer followingCount, List<Long> followerIdList) {
        return UserRedis.builder()
            .id(id)
            .name(name)
            .password(password)
            .followerCount(followerCount)
            .followingCount(followingCount)
            .followerIdList(followerIdList)
            .build();
    }
}

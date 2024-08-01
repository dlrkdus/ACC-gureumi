package com.goormy.hackathon.redis.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRedis_DS implements Serializable {

    private Long id;

    private String name;

    private String password;

    private Integer followerCount;

    private Integer followingCount;

    private List<Long> followerIdList;

    @JsonCreator
    public UserRedis_DS(
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


    public static UserRedis_DS toEntity(Long id, String name, String password, Integer followerCount,
        Integer followingCount, List<Long> followerIdList) {
        return UserRedis_DS.builder()
            .id(id)
            .name(name)
            .password(password)
            .followerCount(followerCount)
            .followingCount(followingCount)
            .followerIdList(followerIdList)
            .build();
    }
}

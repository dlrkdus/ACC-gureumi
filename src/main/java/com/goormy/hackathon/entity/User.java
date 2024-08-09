package com.goormy.hackathon.entity;

import com.goormy.hackathon.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;

    private String password;

    private Integer followerCount;

    private Integer followingCount;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Follow> follows = new ArrayList<>();

    @Builder
    public User(String name, String password, Integer followerCount, Integer followingCount) {
        this.name = name;
        this.password = password;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }
}

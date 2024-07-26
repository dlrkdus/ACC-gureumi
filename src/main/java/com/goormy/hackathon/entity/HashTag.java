package com.goormy.hackathon.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL)
    private List<Follow> follows = new ArrayList<>();

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL)
    private List<PostHashTag> postHashTags = new ArrayList<>();

    public enum Type {
        LOCATION,
        FOOD,
        RESTAURANT
        ;
    }

    @Builder
    public HashTag(String name, Type type) {
        this.name = name;
        this.type = type;
    }
}

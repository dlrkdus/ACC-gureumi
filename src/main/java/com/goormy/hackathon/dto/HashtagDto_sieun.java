package com.goormy.hackathon.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HashtagDto_sieun {
    private Long id;
    private String name;
    private String type;

    public HashtagDto_sieun(Long id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}

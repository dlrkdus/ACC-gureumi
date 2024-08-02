package com.goormy.hackathon.dto.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponseDto_SJ {

    private Long userId;
    private String name;
}

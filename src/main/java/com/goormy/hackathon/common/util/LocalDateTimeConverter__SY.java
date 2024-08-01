package com.goormy.hackathon.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class LocalDateTimeConverter__SY {

    private LocalDateTimeConverter__SY() {
    }

    public static String convert(LocalDateTime value) {
        return value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
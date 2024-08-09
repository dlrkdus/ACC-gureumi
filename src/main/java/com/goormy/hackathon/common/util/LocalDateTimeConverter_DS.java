package com.goormy.hackathon.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.stereotype.Component;

@Component
public class LocalDateTimeConverter_DS {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDateTime convertToLocalDateTime(String source) {
        try {
            return LocalDateTime.parse(source, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                "Invalid date time format. Please use this pattern: yyyy-MM-dd HH:mm:ss");
        }
    }

    public String convertToString(LocalDateTime source) {
        return source.format(formatter);
    }
}
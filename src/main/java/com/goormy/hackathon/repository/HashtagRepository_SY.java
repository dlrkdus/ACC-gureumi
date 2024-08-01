package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository_SY extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByName(String name);
}

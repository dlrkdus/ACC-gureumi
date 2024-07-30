package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}

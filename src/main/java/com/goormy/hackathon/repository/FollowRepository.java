package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT f FROM Follow f WHERE f.user.id = :userId AND f.hashtag.id = :hashtagId")
    Optional<Follow> findByUserIdAndHashTagId(@Param("userId") Long userId, @Param("hashtagId") Long hashtagId);
}

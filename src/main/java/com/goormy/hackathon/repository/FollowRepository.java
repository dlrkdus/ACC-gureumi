package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.Follow;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT f FROM Follow f WHERE f.user.id = :userId AND f.hashtag.id = :hashtagId")
    Optional<Follow> findByUserIdAndHashTagId(@Param("userId") Long userId, @Param("hashtagId") Long hashtagId);

    @Query("SELECT f.hashtag FROM Follow f WHERE f.user = :user")
    List<Hashtag> findHashtagsByUser(@Param("user") User user);
}

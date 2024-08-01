package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.Follow;
import com.goormy.hackathon.entity.Hashtag;
import com.goormy.hackathon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository_sieun extends JpaRepository<Follow, Long> {
    @Query("SELECT f.hashtag FROM Follow f WHERE f.user = :user")
    List<Hashtag> findHashtagsByUser(@Param("user") User user);
}

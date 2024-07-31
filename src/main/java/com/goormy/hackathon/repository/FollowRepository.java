package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

}

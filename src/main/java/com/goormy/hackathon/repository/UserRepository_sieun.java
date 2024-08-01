package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository_sieun extends JpaRepository<User, Long> {
    User findByName(String name);
}

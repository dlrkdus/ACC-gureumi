package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}

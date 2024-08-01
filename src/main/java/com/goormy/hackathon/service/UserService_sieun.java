package com.goormy.hackathon.service;

import com.goormy.hackathon.entity.User;
import com.goormy.hackathon.repository.UserRepository_sieun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService_sieun {

    @Autowired
    private UserRepository_sieun userRepositorySieun;

    public User findById(Long id) {
        return userRepositorySieun.findById(id).orElse(null);
    }
}

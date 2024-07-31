package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

}

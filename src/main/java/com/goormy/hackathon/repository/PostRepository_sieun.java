package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository_sieun extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p JOIN p.postHashtags ph JOIN ph.hashtag h WHERE h.name = :hashtagName")
    Page<Post> findPostsByHashtagName(@Param("hashtagName") String hashtagName, Pageable pageable);
}

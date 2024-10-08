package com.goormy.hackathon.repository.JPA;

import com.goormy.hackathon.entity.Post;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByIdIn(List<Long> ids);

    @Query("SELECT p FROM Post p JOIN p.postHashtags ph JOIN ph.hashtag h WHERE h.name = :hashtagName")
    Page<Post> findPostsByHashtagName(@Param("hashtagName") String hashtagName, Pageable pageable);

}

package com.goormy.hackathon.repository;

import com.goormy.hackathon.entity.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Modifying
    @Query("delete from Like l where l.post.id = :postId and l.user.id = :userId")
    void deleteByPostIdAndUserId(@Param("postId") Long postId, @Param("userId")Long userId);

    @Query("select count(l)>0 from Like l where l.post.id = :postId and l.user.id = :userId")
    boolean isExistByPostIdAndUserId(@Param("postId") Long postId, @Param("userId")Long userId);
}

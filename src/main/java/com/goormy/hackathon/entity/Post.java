package com.goormy.hackathon.entity;

import com.goormy.hackathon.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@NoArgsConstructor
@Getter
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String content;

    private String imageUrl;

    private Integer star;

    private Integer likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();


    @Builder
    public Post(User user, String content, String imageUrl, Integer star, Integer likeCount) {
        this.user = user;
        this.content = content;
        this.imageUrl = imageUrl;
        this.star = star;
        this.likeCount = likeCount;
    }

    public List<Hashtag> getPostHashtags() {
        return postHashtags.stream()
                .map(PostHashtag::getHashtag)
                .toList();
    }

    public void setPostHashtags(List<Hashtag> hashtags) {
        this.postHashtags = hashtags.stream()
                .map(hashtag -> new PostHashtag(this, hashtag))
                .toList();
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

}

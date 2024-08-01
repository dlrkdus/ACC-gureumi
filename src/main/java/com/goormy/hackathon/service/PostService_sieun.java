package com.goormy.hackathon.service;

import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.repository.PostRepository_sieun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PostService_sieun {

    private final PostRepository_sieun postRepositorySieun;

    @Autowired
    public PostService_sieun(PostRepository_sieun postRepositorySieun) {
        this.postRepositorySieun = postRepositorySieun;
    }

    public Page<Post> getPostsByHashtag(String hashtagName, int page, int size) {
        System.out.println("Fetching posts with hashtag: " + hashtagName + ", page: " + page + ", size: " + size);
        Page<Post> postsPage = postRepositorySieun.findPostsByHashtagName(hashtagName, PageRequest.of(page, size));

        if (postsPage == null) {
            throw new RuntimeException("No posts found for the given hashtag.");
        }

        System.out.println("Total elements: " + postsPage.getTotalElements());
        System.out.println("Total pages: " + postsPage.getTotalPages());
        System.out.println("Current page: " + postsPage.getNumber());
        System.out.println("Number of posts on this page: " + postsPage.getNumberOfElements());
        return postsPage;
    }
}

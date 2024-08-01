package com.goormy.hackathon.controller;

import com.goormy.hackathon.entity.Post;
import com.goormy.hackathon.service.PostService_sieun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController_sieun {
    private final PostService_sieun postServiceSieun;

    @Autowired
    public PostController_sieun(PostService_sieun postServiceSieun) {
        this.postServiceSieun = postServiceSieun;
    }

    @GetMapping("/posts/by-hashtag")
    public Page<Post> getPostsByHashtag(
            @RequestParam(name = "hashtag") String hashtag,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size) {
        System.out.println("Received request to fetch posts by hashtag: " + hashtag);
        Page<Post> postsPage = postServiceSieun.getPostsByHashtag(hashtag, page, size);
        System.out.println("Returning page " + postsPage.getNumber() + " of " + postsPage.getTotalPages() + " for hashtag: " + hashtag);
        return postsPage;
    }
}

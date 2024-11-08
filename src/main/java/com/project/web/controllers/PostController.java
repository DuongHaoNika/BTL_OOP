package com.project.web.controllers;

import com.project.web.dtos.PostDTO;
import com.project.web.models.Post;
import com.project.web.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // @RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("")
    public String getAllPost(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
//        model.addAttribute("post", new Post());
        model.addAttribute("title", "Nika | Posts");
        return "index";
    }

    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        model.addAttribute("title", "Nika | Post");
        return "post";
    }

    @GetMapping("/admin/add-post")
    public String getAddPost(Model model) {
        model.addAttribute("title", "Nika | Post");
        return "admin/add_post";
    }

    @PostMapping("/admin/add-post")
    public String addPost(
            @Valid @RequestBody PostDTO postDTO,
            Model model) {
        model.addAttribute("title", "Nika | Post");
        try {
            Post newPost = postService.createPost(postDTO);
//        model.addAttribute("post", newPost);
            return "admin/dashboard";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    @PutMapping("/admin/edit-post/{id}")
    public String editPost(@PathVariable Long id,
                           @Valid @RequestBody PostDTO postDTO,
                           Model model) {
        try {
            Post post = postService.updatePost(id, postDTO);
            model.addAttribute("title", "Nika | Post");
            model.addAttribute("post", post);
            return "admin/dashboard";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/admin/delete-post/{id}")
    public String deletePost(@PathVariable Long id, Model model) {
        try {
            postService.deletePost(id);
            model.addAttribute("title", "Nika | Post");
            return "admin/dashboard";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }
}

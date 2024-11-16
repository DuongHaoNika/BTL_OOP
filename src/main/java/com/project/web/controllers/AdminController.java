package com.project.web.controllers;

import com.project.web.dtos.PostDTO;
import com.project.web.models.Post;
import com.project.web.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final PostService postService;
    @GetMapping()
    public String admin(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "admin/dashboard";
    }
    @GetMapping("/add-post")
    public String getAddPost(Model model) {
        model.addAttribute("title", "Nika | Post");
        return "admin/add_post";
    }

    @PostMapping("/add-post")
    public String addPost(
            @Valid @ModelAttribute PostDTO postDTO,
            Model model) {
        model.addAttribute("title", "Nika | Post");
        try {
            postService.createPost(postDTO);
            return "admin/dashboard";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/edit-post/{id}")
    public String getEditPost(@PathVariable Long id,
                           Model model) {
        try {
            Post post = postService.findById(id);
            model.addAttribute("title", "Nika | Post");
            model.addAttribute("post", post);
            return "admin/edit_post";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    @PutMapping("/edit-post/{id}")
    public String editPost(@PathVariable Long id,
                           @Valid @ModelAttribute PostDTO postDTO,
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

    @DeleteMapping("/delete-post/{id}")
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

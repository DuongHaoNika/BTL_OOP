package com.project.web.controllers;

import com.project.web.dtos.PostDTO;
import com.project.web.models.Post;
import com.project.web.models.User;
import com.project.web.services.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.project.web.services.S3Service;
import com.project.web.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final PostService postService;
    private final UserService userService;
    private final S3Service s3Service;
    @GetMapping()
    public String admin(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("createdAt").descending());
        Page<Post> postsPage = postService.findAll(pageRequest);
        int totalPages = postsPage.getTotalPages();
        boolean nextPage = page >= 2;
        boolean previousPage = totalPages > page;
        List<Post> posts = postsPage.getContent();
        model.addAttribute("posts", posts);
        model.addAttribute("title", "Nika | Posts");
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("previousPage", previousPage);
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
            @RequestParam("uploaded_files") MultipartFile[] files,
            Model model) {
        model.addAttribute("title", "Nika | Post");
        try {
            String body = postDTO.getBody();
            for(MultipartFile file : files) {
                if(file != null && file.getSize() > 0) {
                    String url = "![image](" + s3Service.uploadFile(file) + ")";
                    body = body.replaceFirst("<image>", url);
                }
            }
            postDTO.setBody(body);
            postService.createPost(postDTO);

            return  "redirect:/admin";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/edit-post/{id}")
    public String getEditPost(@PathVariable Long id,
                           Model model) {
        try {
            Post post = postService.findByIdAdmin(id);
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
            return "redirect:/admin";
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
            return "redirect:/admin";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }
}

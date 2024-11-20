package com.project.web.controllers;

import com.project.web.dtos.PostDTO;
import com.project.web.models.Post;
import com.project.web.models.User;
import com.project.web.services.ImageService;
import com.project.web.services.PostService;
import com.project.web.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final PostService postService;
    private final ImageService imageService;
    private final UserService userService;
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

    @GetMapping("/add-image")
    public String getUpload() {
        return "admin/add_image";
    }

    @PostMapping("/add-image")
    public String uploadImages(@RequestParam("uploaded_files") MultipartFile[] files) throws IOException {
        for(MultipartFile file : files) {
            if(file != null && file.getSize() > 0) {
                if(file.getSize() > 10 * 1024 * 1024){
//                    throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File is too large!");
                }
                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image")){
//                    return ResponseEntity.badRequest().body("File is not an image!");
                }
                imageService.storeFile(file);
            }
        }
        return "redirect:/admin/add-image";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }
}

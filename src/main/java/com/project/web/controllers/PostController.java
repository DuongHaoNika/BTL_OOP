package com.project.web.controllers;

import com.project.web.dtos.CommentDTO;
import com.project.web.dtos.PostDTO;
import com.project.web.models.Comment;
import com.project.web.models.Post;
import com.project.web.responses.CommentUserResponse;
import com.project.web.services.CommentService;
import com.project.web.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("")
    public String getAllPost(Model model, Principal principal) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("title", "Nika | Posts");
        return "index";
    }

    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        List<CommentUserResponse> comments = commentService.getCommentByPostId(id);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("title", "Nika | Post");
        return "post";
    }

    @PostMapping("/comment/{id}")
    public String comment(@PathVariable Long id, @ModelAttribute CommentDTO commentDTO, Model model, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        commentService.saveComment(commentDTO, id, username);
        return "redirect:/post/" + id;
    }

    @PutMapping("/comment/{id}")
    public String updateComment(@PathVariable Long id, @ModelAttribute CommentDTO commentDTO, Model model) {
        commentService.updateComment(commentDTO, id);
        return "redirect:/post/" + id;
    }

    public String deleteComment(@PathVariable Long id, Model model) {
        commentService.deleteComment(id);
        return "redirect:/post/" + id;
    }
}

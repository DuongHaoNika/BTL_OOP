package com.project.web.controllers;

import com.project.web.dtos.CommentDTO;
import com.project.web.dtos.PostDTO;
import com.project.web.models.Comment;
import com.project.web.models.Image;
import com.project.web.models.Post;
import com.project.web.responses.CommentUserResponse;
import com.project.web.services.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final S3Service s3Service;
    private final HTMLService htmlService;
    private final ImageService imageService;

    @GetMapping("")
    public String getAllPost(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("createdAt").descending());
        Page<Post> postsPage = postService.findAllActive(pageRequest);
        int totalPages = postsPage.getTotalPages();
        boolean nextPage = page >= 2;
        boolean previousPage = totalPages > page;
        List<Post> posts = postsPage.getContent();
        model.addAttribute("posts", posts);
        model.addAttribute("title", "Nika | Posts");
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("previousPage", previousPage);
        return "index";
    }

    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        if (post == null) {
            return "redirect:/";
        }
        List<CommentUserResponse> comments = commentService.getCommentByPostId(id);
        post.setBody(htmlService.mdToHtml(post.getBody()));
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("title", "Nika | Post");
        return "post";
    }

    @PostMapping("/comment/{id}")
    public String comment(@PathVariable Long id, @ModelAttribute CommentDTO commentDTO, Model model,
                          @RequestParam("uploaded_file") MultipartFile file, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        Comment comment = commentService.saveComment(commentDTO, id, username);
        if(file != null && !file.isEmpty()) {
            try {
                String imageUrl = s3Service.uploadFile(file);
                imageService.save(comment.getId(), imageUrl);
                model.addAttribute("imageUrl", imageUrl);
            }
            catch (Exception e) {
                return null;
            }
        }
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

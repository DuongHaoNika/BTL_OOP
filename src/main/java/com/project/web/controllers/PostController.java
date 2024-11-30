package com.project.web.controllers;

import com.project.web.dtos.CommentDTO;
import com.project.web.models.Comment;
import com.project.web.models.Post;
import com.project.web.models.User;
import com.project.web.responses.CommentUserResponse;
import com.project.web.services.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PostController {
    private final IPostService postService;
    private final ICommentService commentService;
    private final S3Service s3Service;
    private final HTMLService htmlService;
    private final IImageService imageService;

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
                String contentType = file.getContentType();
                assert contentType != null;
                if(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/jpg")) {
                    String imageUrl = s3Service.uploadFile(file);
                    imageService.save(comment.getId(), imageUrl);
                    model.addAttribute("imageUrl", imageUrl);
                }
                else {
                    throw new Exception("File is not an image!");
                }
            }
            catch (Exception e) {
                return "error/error";
            }
        }
        return "redirect:/post/" + id;
    }

    @GetMapping("/comment/edit/{id}")
    public String getUpdateComment(@PathVariable Long id, Model model) {
        Comment comment = commentService.getComment(id).orElse(null);
        if(comment != null) {
            model.addAttribute("comment", comment);
            model.addAttribute("title", "Nika | Post");
            return "edit-comment";
        }
        else {
            return "error/error";
        }
    }

    @PutMapping("/comment/edit/{id}")
    public String updateComment(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestParam String body, Model model) {
        try {
            Comment comment = commentService.getComment(id).orElse(null);
            if(comment != null && comment.getUser().getUsername().equals(user.getUsername())){
                commentService.updateComment(body, id);
                return "redirect:/post/" + comment.getPost().getId();
            }
            else {
                return "error/error";
            }
        }
        catch(Exception e) {
            return "error/error";
        }
    }

    @DeleteMapping("/comment/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteComment(@AuthenticationPrincipal User user, @PathVariable Long id) {
        String username = user.getUsername();
        Comment comment = commentService.getComment(id).orElse(null);

        Map<String, String> response = new HashMap<>();

        if (comment != null && comment.getUser().getUsername().equals(username)) {
            Long postId = comment.getPost().getId();
            commentService.deleteComment(id);

            response.put("redirectUrl", "/post/" + postId); // Use a clear key for the URL
            return ResponseEntity.ok(response); // Return JSON
        } else {
            response.put("error", "Unauthorized or comment not found");
            return ResponseEntity.status(403).body(response); // Return error JSON
        }
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam("keyword") String keyword) {
        List<Post> posts = postService.searchPosts(keyword);
        model.addAttribute("posts", posts);
        model.addAttribute("title", "Nika | Posts");
        return "index";
    }
}

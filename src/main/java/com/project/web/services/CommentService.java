package com.project.web.services;

import com.project.web.dtos.CommentDTO;
import com.project.web.models.Comment;
import com.project.web.models.Image;
import com.project.web.models.Post;
import com.project.web.models.User;
import com.project.web.repositories.CommentRepository;
import com.project.web.repositories.ImageRepository;
import com.project.web.repositories.PostRepository;
import com.project.web.repositories.UserRepository;
import com.project.web.responses.CommentUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public Optional<Comment> getComment(Long id) {
        return commentRepository.findById(id);
    }

    public Comment saveComment(CommentDTO commentDTO, Long postId, String username) {
        Post post = postRepository.findById(postId).orElse(null);
        User user = userRepository.findByUsername(username).orElse(null);
        if(post != null && user != null) {
            Comment comment = Comment
                    .builder()
                    .body(commentDTO.getBody())
                    .post(post)
                    .user(user)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            return commentRepository.save(comment);
        }
        return null;
    }

    public List<CommentUserResponse> getCommentByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentUserResponse> commentUserResponses = new ArrayList<>();
        for(Comment comment : comments) {
            User user = comment.getUser();
            Image image = imageRepository.findByCommentId(comment.getId());
            String urlImage = null;
            if(image != null) {
                urlImage = image.getSource();
            }
            commentUserResponses.add(new CommentUserResponse(comment.getId(), comment.getBody(), user.getUsername(), user.getFullname(), user.getAvatar(), urlImage,comment.getUpdatedAt()));
        }
        return commentUserResponses;
    }

    public Comment updateComment(String body, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if(comment != null) {
            comment.setUpdatedAt(LocalDateTime.now());
            comment.setBody(body);
            return commentRepository.save(comment);
        }
        return null;
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}

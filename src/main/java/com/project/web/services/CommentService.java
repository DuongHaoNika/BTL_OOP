package com.project.web.services;

import com.project.web.dtos.CommentDTO;
import com.project.web.models.Comment;
import com.project.web.models.Post;
import com.project.web.repositories.CommentRepository;
import com.project.web.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    public Comment saveComment(CommentDTO commentDTO, Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if(post != null) {
            Comment comment = Comment
                    .builder()
                    .body(commentDTO.getBody())
                    .post(post)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            return commentRepository.save(comment);
        }
        return null;
    }

    public List<Comment> getCommentByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public Comment updateComment(CommentDTO commentDTO, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if(comment != null) {
            comment.setUpdatedAt(LocalDateTime.now());
            comment.setBody(commentDTO.getBody());
            return commentRepository.save(comment);
        }
        return null;
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}

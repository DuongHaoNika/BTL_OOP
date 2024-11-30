package com.project.web.services;

import com.project.web.dtos.CommentDTO;
import com.project.web.models.Comment;
import com.project.web.responses.CommentUserResponse;

import java.util.List;
import java.util.Optional;

public interface ICommentService {
    Comment saveComment(CommentDTO commentDTO, Long postId, String username);
    List<CommentUserResponse> getCommentByPostId(Long postId);
    Comment updateComment(String body, Long commentId);
    void deleteComment(Long commentId);
    Optional<Comment> getComment(Long id);
}

package com.project.web.services;

import com.project.web.dtos.PostDTO;
import com.project.web.exceptions.DataNotFoundException;
import com.project.web.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IPostService {
    Page<Post> findAllActive(PageRequest pageRequest);
    Page<Post> findAll(PageRequest pageRequest);
    Post findById(Long id);
    Post findByIdAdmin(Long id);
    Post createPost(PostDTO postDTO);
    Post updatePost(Long id, PostDTO postDTO) throws DataNotFoundException;
    void deletePost(Long id) throws DataNotFoundException;
    List<Post> searchPosts(String keyword);
}

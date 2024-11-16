package com.project.web.services;

import com.project.web.dtos.PostDTO;
import com.project.web.exceptions.DataNotFoundException;
import com.project.web.models.Image;
import com.project.web.models.Post;
import com.project.web.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService implements IPostService {
    private final PostRepository postRepository;
    @Override
    public List<Post> findAllActive() {
        return postRepository.findByActiveTrue();
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public Post createPost(PostDTO postDTO) {
        Post newPost = Post.builder()
                .title(postDTO.getTitle())
                .body(postDTO.getBody())
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        System.out.println(newPost);
        return postRepository.save(newPost);
    }

    public Post updatePost(Long id, PostDTO postDTO) throws DataNotFoundException {
        Post post = postRepository.findById(id).orElse(null);
        if(post != null) {
            post.setTitle(postDTO.getTitle());
            post.setBody(postDTO.getBody());
            post.setActive(postDTO.getActive());
            post.setUpdatedAt(LocalDateTime.now());
            return postRepository.save(post);
        }
        else {
            throw new DataNotFoundException("Post is not found with id: " + id);
        }
    }

    public void deletePost(Long id) throws DataNotFoundException {
        Post post = postRepository.findById(id).orElse(null);
        if(post != null) {
            post.setActive(false);
            postRepository.save(post);
        }
        else {
            throw new DataNotFoundException("Post is not found with id: " + id);
        }

    }


}

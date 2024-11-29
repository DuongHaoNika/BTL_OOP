package com.project.web.repositories;

import com.project.web.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByActiveTrue();
    Page<Post> findByActiveTrue(Pageable pageable);
    List<Post> findByTitleContainingIgnoreCase(String keyword);
}

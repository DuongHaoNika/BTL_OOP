package com.project.web.repositories;

import com.project.web.models.ImageBlog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageBlogRepository extends JpaRepository<ImageBlog, Long> {

}

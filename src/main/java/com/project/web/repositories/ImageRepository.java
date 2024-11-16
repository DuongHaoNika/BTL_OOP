package com.project.web.repositories;

import com.project.web.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByCommentId(Long commentId);
}

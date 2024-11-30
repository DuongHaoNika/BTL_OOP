package com.project.web.services;

import com.project.web.models.Comment;
import com.project.web.models.Image;
import com.project.web.repositories.CommentRepository;
import com.project.web.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;
    public Optional<Image> getImage(Long id) {
        return imageRepository.findById(id);
    }
    public Image save(Long commentID, String url) {
        Comment comment = commentRepository.findById(commentID).get();
        Image image = Image
                .builder()
                .source(url)
                .createdAt(LocalDateTime.now().atZone(ZoneId.of("UTC+7")).toLocalDateTime())
                .updatedAt(LocalDateTime.now().atZone(ZoneId.of("UTC+7")).toLocalDateTime())
                .comment(comment)
                .build();
        return imageRepository.save(image);
    }
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
}

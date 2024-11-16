package com.project.web.services;

import com.project.web.models.Comment;
import com.project.web.models.Image;
import com.project.web.repositories.CommentRepository;
import com.project.web.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path uploadDir = Paths.get("src/main/resources/static/uploads");
        if(!Files.exists(uploadDir)) {
            Files.createDirectory(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), newFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return newFileName;
    }

    public Image getImageByCommentId(Long commentId) {
        return imageRepository.findByCommentId(commentId);
    }

    public String createCommentImage(MultipartFile file, Long commentId) throws IOException {
        String filename = storeFile(file);
        String urlImage = "/uploads/" + filename;
        Comment comment = commentRepository.findById(commentId).orElse(null);
        Image image = Image
                .builder()
                .name(filename)
                .source(urlImage)
                .comment(comment)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        imageRepository.save(image);
        return urlImage;
    }
}

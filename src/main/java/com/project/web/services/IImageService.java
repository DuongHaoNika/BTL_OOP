package com.project.web.services;

import com.project.web.models.Image;

import java.util.Optional;

public interface IImageService {
    Optional<Image> getImage(Long id);
    Image save(Long commentID, String url);
    void deleteImage(Long id);
}

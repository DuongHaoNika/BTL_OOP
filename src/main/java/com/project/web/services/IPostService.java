package com.project.web.services;

import com.project.web.models.Post;

import java.util.List;

public interface IPostService {
    List<Post> findAllActive();
}

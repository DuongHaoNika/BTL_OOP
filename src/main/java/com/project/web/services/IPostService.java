package com.project.web.services;

import com.project.web.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IPostService {
    Page<Post> findAllActive(PageRequest pageRequest);
}

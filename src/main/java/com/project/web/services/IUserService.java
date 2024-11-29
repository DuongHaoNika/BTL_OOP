package com.project.web.services;

import com.project.web.dtos.UserDTO;
import com.project.web.exceptions.DataNotFoundException;
import com.project.web.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    List<User> findAll();
    String login(String username, String password) throws DataNotFoundException;
    User save(User user);
    Optional<User> findByUsername(String username);
}

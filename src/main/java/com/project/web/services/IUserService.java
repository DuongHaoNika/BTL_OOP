package com.project.web.services;

import com.project.web.models.User;

import java.util.*;

public interface IUserService
{
    User saveUser(User user); // Lưu một người dùng mới
    Optional<User> getUserById(Long id); // Lấy thông tin người dùng theo ID
    List<User> getAllUsers(); // Lấy danh sách tất cả người dùng
    User updateUser(Long id, User userDetails); // Cập nhật thông tin người dùng
    void deleteUser(Long id); // Xóa người dùng theo ID
}

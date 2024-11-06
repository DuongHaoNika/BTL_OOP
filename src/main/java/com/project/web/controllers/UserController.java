package com.project.web.controllers;

import com.project.web.models.User;
import com.project.web.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user") // có gì chỉnh cho t cái này
public class UserController
{
    @Autowired
    private IUserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@RequestBody User user)
    {
        try
        {
            User createdUser = userService.saveUser(user); // Lưu thông tin user
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED); // Trả về user đã tạo
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // Xử lý lỗi
        }
    }
}

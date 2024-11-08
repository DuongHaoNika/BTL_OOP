package com.project.web.controllers;

import com.project.web.dtos.UserDTO;
import com.project.web.dtos.UserLoginDTO;
import com.project.web.models.User;
import com.project.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("title", "Nika | Login");
        model.addAttribute("userLoginDTO", new UserLoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute UserLoginDTO userLoginDTO, Model model) {
        model.addAttribute("title", "Nika | Login");
        return "index";
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("title", "Nika | Register");
        model.addAttribute("userDTO", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute UserDTO userDTO, Model model) {
        model.addAttribute("title", "Nika | Blog");
        try {
            User user = userService.createUser(userDTO);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "index";
    }
}

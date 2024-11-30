package com.project.web.controllers;

import com.project.web.dtos.UserDTO;
import com.project.web.dtos.UserLoginDTO;
import com.project.web.services.IUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    @GetMapping("/login")
    public String getLogin(Model model, HttpServletResponse response) {
        model.addAttribute("title", "Nika | Login");
        model.addAttribute("userLoginDTO", new UserLoginDTO());
        model.addAttribute("username", null);
        Cookie cookie = new Cookie("token", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute UserLoginDTO userLoginDTO, HttpServletResponse response, Model model) {
        try {
            String token = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(2592000);
            response.addCookie(cookie);
            return "redirect:/";
        }
        catch (Exception e) {
            model.addAttribute("title", "Nika | Login");
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("title", "Nika | Register");
        model.addAttribute("userDTO", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute UserDTO userDTO, Model model) {
        try {
            userService.createUser(userDTO);
        }
        catch (Exception e) {
            model.addAttribute("title", "Nika | Register");
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/auth/login";
    }


}

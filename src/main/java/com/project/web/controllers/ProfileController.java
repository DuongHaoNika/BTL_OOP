package com.project.web.controllers;

import com.project.web.models.User;
import com.project.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;

    @GetMapping("/profile/{username}")
    public String viewProfile(@PathVariable("username") String username, Model model) {
        Optional<User> user = userService.findByUsername(username);

        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "profile";  // Trả về trang profile.html
        } else {
            model.addAttribute("error", "User not found");
            return "error";  // Trả về trang error.html
        }
    }
}

package com.project.web.controllers;

import com.project.web.models.User;
import com.project.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;

    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal User user, Model model) {
        Optional<User> user_optional = userService.findByUsername(user.getUsername());

        if (user_optional.isPresent()) {
            model.addAttribute("user", user_optional.get());
            return "profile";  // Trả về trang profile.html
        } else {
            model.addAttribute("error", "User not found");
            return "error";  // Trả về trang error.html
        }
    }

    @GetMapping("/profile/{username}")
    public String viewProfile(@AuthenticationPrincipal User user, @PathVariable String username, Model model) {

        Optional<User> user_optional = userService.findByUsername(username);
        if (user_optional.isPresent()) {
            model.addAttribute("user", user_optional.get());
            if(user.getUsername().equals(user_optional.get().getUsername())) {
                return "profile";
            }
            else {
                return "profile2";
            }
        } else {
            model.addAttribute("error", "User not found");
            return "error/error";
        }
    }
}

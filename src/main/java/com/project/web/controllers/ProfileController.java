package com.project.web.controllers;

import com.project.web.models.User;
import com.project.web.services.IUserService;
import com.project.web.services.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final IUserService userService;
    private final S3Service s3Service;

    @GetMapping()
    public String viewProfile(@AuthenticationPrincipal User user, Model model) {
        Optional<User> user_optional = userService.findByUsername(user.getUsername());

        if (user_optional.isPresent()) {
            model.addAttribute("user", user_optional.get());
            return "profile";
        } else {
            model.addAttribute("error", "User not found");
            return "error/error";
        }
    }

    @GetMapping("/{username}")
    public String viewProfile(@AuthenticationPrincipal User user, @PathVariable String username, Model model) {
        Optional<User> user_optional = userService.findByUsername(username);
        if(user == null) {
            model.addAttribute("username", null);
            model.addAttribute("user", user_optional.get());
            return "profile2";
        }

        if (user_optional.isPresent()) {
            model.addAttribute("user", user_optional.get());
            model.addAttribute("username", user.getUsername());
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
    @GetMapping("/edit")
    public String editProfile(@AuthenticationPrincipal User user, Model model)
    {
        Optional<User> user_optional = userService.findByUsername(user.getUsername());

        if (user_optional.isPresent())
        {
            model.addAttribute("user", user_optional.get());
            return "edit-profile";
        } else
        {
            model.addAttribute("error", "User not found");
            return "error/error";
        }
    }

    @PostMapping("/avatar")
    public String uploadAvatar(@AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file){
        User existUser = userService.findByUsername(user.getUsername()).orElse(null);
        if(existUser != null) {
            try {
                String contentType = file.getContentType();
                assert contentType != null;
                if(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/jpg")) {
                    String urlImage = s3Service.uploadFile(file);
                    existUser.setAvatar(urlImage);
                    userService.save(existUser);
                }
                else {
                    throw new Exception("File is not an image!");
                }
            }
            catch(Exception e) {
                return null;
            }
        }
        return "redirect:/profile";
    }


    @PostMapping("/edit")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @ModelAttribute("user") User updatedUser,
            BindingResult result,
            Model model
    )
    {
        if (result.hasErrors())
        {
            model.addAttribute("user", updatedUser);
            return "edit-profile";
        }

        Optional<User> user_optional = userService.findByUsername(user.getUsername());

        if (user_optional.isPresent())
        {
            User existingUser = user_optional.get();

            existingUser.setFullname(updatedUser.getFullname());
            existingUser.setAge(updatedUser.getAge());
            existingUser.setHometown(updatedUser.getHometown());
            existingUser.setSchool(updatedUser.getSchool());
            existingUser.setSex(updatedUser.getSex());

            userService.save(existingUser);
            return "redirect:/profile/" + user.getUsername();
        }
        return "error/error";
    }
}

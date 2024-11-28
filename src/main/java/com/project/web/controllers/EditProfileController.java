package com.project.web.controllers;

import com.project.web.models.User;
import com.project.web.services.S3Service;
import com.project.web.services.UserService;
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
public class EditProfileController
{
    private final UserService userService;
    private final S3Service s3Service;

    @GetMapping("/profile/edit")
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
            return "error";
        }
    }

    @PostMapping("/profile/avatar")
    public String uploadAvatar(@AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file){
        User existUser = userService.findByUsername(user.getUsername()).orElse(null);
        if(existUser != null) {
            try {
                String urlImage = s3Service.uploadFile(file);
                existUser.setAvatar(urlImage);
                userService.save(existUser);
            }
            catch(Exception e) {
                return null;
            }
        }
        return "redirect:/profile/upload-success";
    }

    @GetMapping("/profile/upload-success")
    public String uploadSuccess() {
        return "upload-success";
    }


    @PostMapping("/profile/edit")
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
        return "error";
    }
}

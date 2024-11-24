package com.project.web.controllers;

import com.project.web.models.User;
import com.project.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@Controller
@RequiredArgsConstructor
public class EditProfileController
{
    private final UserService userService;

    @GetMapping("/profile/edit")
    public String editProfile(@AuthenticationPrincipal User user, Model model)
    {
        Optional<User> user_optional = userService.findByUsername(user.getUsername());

        if (user_optional.isPresent())
        {
            model.addAttribute("user", user_optional.get());
            return "edit-profile";  // Trả về trang edit-profile.html
        } else
        {
            model.addAttribute("error", "User not found");
            return "error";  // Trả về trang error.html
        }
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
            return "edit-profile"; // Quay lại trang sửa nếu có lỗi
        }

        Optional<User> user_optional = userService.findByUsername(user.getUsername());

        if (user_optional.isPresent())
        {
            User existingUser = user_optional.get();
            // Cập nhật các thông tin của user
            existingUser.setFullname(updatedUser.getFullname());
            existingUser.setAge(updatedUser.getAge());
            existingUser.setHometown(updatedUser.getHometown());
            existingUser.setSchool(updatedUser.getSchool());
            existingUser.setSex(updatedUser.getSex());
            // Thêm các trường khác nếu cần

            userService.save(existingUser); // Lưu thông tin cập nhật
            return "redirect:/profile/" + user.getUsername(); // Quay lại trang profile
        }
        return "error"; // Trả về trang error.html
    }
}

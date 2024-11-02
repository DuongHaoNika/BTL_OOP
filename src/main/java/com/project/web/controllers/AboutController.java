package com.project.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @GetMapping("/about")
    public String aboutPage(Model model)
    {
        model.addAttribute("title", "Nika | About");
        return "about";
    }
}

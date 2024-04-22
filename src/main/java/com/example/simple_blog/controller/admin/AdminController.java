package com.example.simple_blog.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {


    @GetMapping("/admin")
    public String admin() {
        return "adminPage";
    }
}

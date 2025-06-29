package com.example.servingwebcontent.pure_java_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrangChuController {

    @GetMapping("/")
    public String trangChu() {
        return "index"; 
    }
}
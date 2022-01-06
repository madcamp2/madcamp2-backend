package com.example.everytask.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class normalController {
    @GetMapping("welcome")
    public String normalPage(){
        return "welcome.html";
    }
}

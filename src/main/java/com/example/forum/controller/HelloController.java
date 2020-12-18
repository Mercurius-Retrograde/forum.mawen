package com.example.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class HelloController {
    @GetMapping("test")
    public String test(@RequestParam(name="name",required=false) String name,Model model){
        model.addAttribute("name",name);
        return "test";
    }
}

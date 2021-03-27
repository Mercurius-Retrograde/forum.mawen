package com.example.forum.controller;

import com.example.forum.dto.PaginationDTO;
import com.example.forum.mapper.UserMapper;
import com.example.forum.model.User;
import com.example.forum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;


    @GetMapping("/")
    public String index(Model model,
                        @RequestParam (name = "page",defaultValue = "1") Integer page,
                        @RequestParam (name = "size",defaultValue = "6") Integer size){

        PaginationDTO pagination = questionService.list(page,size);
        model.addAttribute("pagination",pagination);
        return "index";
    }
}

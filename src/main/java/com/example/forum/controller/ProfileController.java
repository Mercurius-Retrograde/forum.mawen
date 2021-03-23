package com.example.forum.controller;


import com.example.forum.dto.PaginationDTO;
import com.example.forum.mapper.QuestionMapper;
import com.example.forum.mapper.UserMapper;
import com.example.forum.model.User;
import com.example.forum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class ProfileController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;
    private Model pagination;

    @GetMapping("/profile/{action}")//设置访问profile时访问下方的地址
    public String profile(@PathVariable(name = "action") String action,//Sring profile返回对应的界面
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam (name = "size",defaultValue = "6") Integer size,
                          HttpServletRequest request,
                          Model model){
        User user = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user != null)
                        request.getSession().setAttribute("user", user);
                    break;
                }
            }
        }

        if(user == null){
            return "redirect:/";
        }
        questionService.listByUser(user.getId(),page,size);

        if("question".contains(action)){
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的提问");
        }else if("replies".contains(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }

        PaginationDTO paginationDTO = questionService.listByUser(user.getId(),page,size);
        model.addAttribute("pagination", paginationDTO);
        return "profile";
    }
}

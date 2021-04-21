package com.example.forum.controller;


import com.example.forum.dto.PaginationDTO;
import com.example.forum.mapper.UserMapper;
import com.example.forum.model.User;
import com.example.forum.service.NotificationService;
import com.example.forum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;


@Controller
public class ProfileController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private QuestionService questionService;

    //private Model pagination;

    @GetMapping("/profile/{action}")//设置访问profile时访问下方的地址
    public String profile(@PathVariable(name = "action") String action,//Sring profile返回对应的界面
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam (name = "size",defaultValue = "6") Integer size,
                          HttpServletRequest request,
                          Model model){

        User user = (User) request.getSession().getAttribute("user");

        if(user == null){
            return "redirect:/";
        }
        questionService.listByUser(user.getId(),page,size);

        if("question".contains(action)){
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的提问");

            //查询已登录用户提交问题列表，调用分页模块
            PaginationDTO paginationDTO = questionService.listByUser(user.getId(),page,size);
            model.addAttribute("pagination", paginationDTO);
        }else if("replies".contains(action)){
            PaginationDTO paginationDTO = notificationService.list(user.getId(),page,size);
//            Long unreadCount = notificationService.unreadCount(user.getId());
            model.addAttribute("section","replies");
            model.addAttribute("pagination",paginationDTO);
//            model.addAttribute("unreadCount",unreadCount);
            model.addAttribute("sectionName","最新回复");
        }
        return "profile";
    }
}

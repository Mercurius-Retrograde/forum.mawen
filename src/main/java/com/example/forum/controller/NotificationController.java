package com.example.forum.controller;

import com.example.forum.dto.NotificationDTO;
import com.example.forum.dto.PaginationDTO;
import com.example.forum.enums.NotificationTypeEnum;
import com.example.forum.mapper.NotificationMapper;
import com.example.forum.model.User;
import com.example.forum.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;


@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")//设置访问profile时访问下方的地址
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id) {

        User user = (User) request.getSession().getAttribute("user");

        if(user == null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id,user);
        if(NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()||
                NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()){
            return "redirect:/question/"+notificationDTO.getOuterid();
        }else {
            return "redirect:";
        }
    }
}

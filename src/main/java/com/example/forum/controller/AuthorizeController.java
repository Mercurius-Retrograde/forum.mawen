package com.example.forum.controller;

import com.example.forum.dto.AccessTokenDTO;
import com.example.forum.dto.GithubUser;
import com.example.forum.mapper.UserMapper;
import com.example.forum.model.User;
import com.example.forum.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.client.uri}")//回调url需要与github中设置的回调地址相同，注意http与https的不同
    private String clientUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                           HttpServletRequest req){
        AccessTokenDTO accesstokenDTO = new AccessTokenDTO();
        accesstokenDTO.setClient_id(clientId);
        accesstokenDTO.setClient_secret(clientSecret);
        accesstokenDTO.setCode(code);
        accesstokenDTO.setRedirect_uri(clientUri);
        accesstokenDTO.setState(state);

        String accesstoken = githubProvider.getAccessToken(accesstokenDTO);
        GithubUser githubUser = githubProvider.getUser(accesstoken);

        if(githubUser != null){
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(user.getGmtCreate());
            user.setGmtModified(user.getGmtModified());
            userMapper.insert(user);
            //登录成功，写cookie和session
            req.getSession().setAttribute("user", githubUser);
            return "redirect:/";
        }else{
            //登录失败，重新登录
        }
        return "index";
    }
}

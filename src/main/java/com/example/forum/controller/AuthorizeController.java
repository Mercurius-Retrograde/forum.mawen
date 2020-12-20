package com.example.forum.controller;

import com.example.forum.dto.AccessTokenDTO;
import com.example.forum.dto.GithubUser;
import com.example.forum.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id("bccffdfe93d559a50885");
        accessTokenDTO.setClient_secret("405fd54eb96ee3db2387406428f3ab80bc95796f");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("https://github.com/login/oauth/access_token");
        accessTokenDTO.setState(state);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());

        return "callback";
    }
}

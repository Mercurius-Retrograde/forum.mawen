package com.example.forum.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@EnableWebMvc会在启动时自动拦截掉项目中的静态资源文件（css)
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private SessionInterceptor sessionInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //registry.addInterceptor(new LocalInterceptor());
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
        //addPatterns控制拦截地址
        //excludePatterns控制过滤哪些地址
        //registry.addInterceptor(new SecurityInterceptor()).addPathPatterns("/secure/*");

    }
}

package com.forum.demo.interceptor;

import com.forum.demo.config.AppConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Value("cloud-intelligence-forum.login.url")
    private String defaultUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取session对象
        HttpSession session = request.getSession(false);
        //判断session是否有效
        if(session != null && session.getAttribute(AppConfig.USER_SESSION)!=null) {
            //登录校验通过
            return true;
        }
        //检验url是否正确
        if(!defaultUrl.startsWith("/")) {
            defaultUrl = "/" + defaultUrl;
        }
        //校验不通过,跳转登录界面
        response.sendRedirect(defaultUrl);
        //中断
        return false;
    }
}

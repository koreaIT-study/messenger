package com.teamride.messenger.client.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor{

    private final HttpSession httpSession;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("인터셉터 작동::"+request.getRequestURL().toString());
        if (request.getRequestURL().toString().contains("/login") 
        		|| request.getRequestURL().toString().contains("/sign_up")
                || request.getRequestURL().toString().contains("/kakao_login")
                || request.getRequestURL().toString().contains("/naver_login")
                || request.getRequestURL().toString().contains("/social_login")
                || request.getRequestURL().toString().contains("/oauth")
                || request.getRequestURL().toString().startsWith("https://kauth.kakao.com") 
                || request.getRequestURL().toString().startsWith("https://nid.naver.com") 
            || request.getRequestURL().toString().startsWith("https://kapi.kakao.com")) {
            return true;
        }
        
//        if (httpSession.getAttribute("userEmail") == null) {
//            response.sendRedirect("/login");
//            return false;
//        }
        
        return true;
    }
}

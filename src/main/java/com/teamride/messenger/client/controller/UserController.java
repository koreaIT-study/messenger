package com.teamride.messenger.client.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserController {
	private static final String REST_API_KEY = "dd0b678881f8ea6c47e2012b37fb8c90";
	private static final String REDIRECT_URI = "http://localhost:8081/oauth";

	@GetMapping("/login")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sign_in");

		return mv;
	}

	@GetMapping("/kakao_login")
	public void kakaoLogin(HttpServletResponse response) {
		String redirectUrl = "https://kauth.kakao.com/oauth/authorize?client_id="+REST_API_KEY + "&redirect_uri="+REDIRECT_URI+"&response_type=code";
		
		try {
			response.sendRedirect(redirectUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@GetMapping("oauth")
	public void responseOauth(String code) {
		log.info("kakao authorize code is :: " + code);
		// webClient로  https://kapi.kakao.com/v2/user/me의 경로로
		// 헤더에 Authorization란 key로 "Bearer {access_Token}"을 담아서 보내라고 나와있습니다.
	}
}

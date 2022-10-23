package com.teamride.messenger.client.controller;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import com.teamride.messenger.client.dto.AdminDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
	private final WebClient webClient;

	@GetMapping("/login")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sign_in");

		return mv;
	}

	@GetMapping("/sign_up")
	public ModelAndView signUp() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("sign_up");

		return mv;
	}

	@GetMapping("friend")
	public ModelAndView friend() {
		ModelAndView mv = new ModelAndView("friends");
		return mv;
	}
	
	public WebClient getWebClient() {
		return webClient.mutate()
				.build();
	}
}

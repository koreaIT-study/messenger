package com.teamride.messenger.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
	
	@GetMapping("/login")
	public String login() {
//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("sign_in");
		
		return "chat";
	}
}

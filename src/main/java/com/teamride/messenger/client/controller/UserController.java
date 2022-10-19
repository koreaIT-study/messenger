package com.teamride.messenger.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserController {
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

}

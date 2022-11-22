package com.teamride.messenger.client.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import com.teamride.messenger.client.config.Constants;
import com.teamride.messenger.client.dto.AdminDTO;
import com.teamride.messenger.client.utils.RestResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final WebClient webClient;
    private final HttpSession httpSession;

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sign_in");

        return mv;
    }

    @GetMapping("/sign_in")
    public ModelAndView singIn() {
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
    	log.info("login session :: {}", httpSession.getAttribute(Constants.LOGIN_SESSION));
        ModelAndView mv = new ModelAndView("friends");
        return mv;
    }

    public WebClient getWebClient() {
        return webClient.mutate()
                .build();
    }

    @ResponseBody
    @PostMapping("/loginAction")
    public RestResponse loginAction(@RequestBody AdminDTO adminDTO) {
    	try {
			final AdminDTO resp = webClient
			        .post()
			        .uri("/loginAction")
			        .contentType(MediaType.APPLICATION_JSON)
			        .accept(MediaType.APPLICATION_JSON)
			        .bodyValue(adminDTO)
			        .retrieve()
			        .bodyToMono(AdminDTO.class).block();
			if(resp == null) {
				return new RestResponse("NOT_FOUND");
			}
			
			httpSession.setAttribute(Constants.LOGIN_SESSION, resp.getId());
			return new RestResponse(resp);
		} catch (Exception e) {
			return new RestResponse(1, e.getLocalizedMessage(), null);
		}
    }

    @ResponseBody
    @GetMapping("/smtpRequest")
    public RestResponse smtpRequest(@RequestParam String email) {
        final String resp = webClient
                .get()
                .uri(t -> t.queryParam("email", email).build())
                .retrieve()
                .bodyToMono(String.class).block();

        return new RestResponse(resp);
    }

    @ResponseBody
    @PostMapping("/signUp")
    public RestResponse signUp(@RequestBody AdminDTO adminDTO) {
    	final Integer resp = webClient
                .post()
                .uri("/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(adminDTO)
                .retrieve()
                .bodyToMono(Integer.class).block();
        return new RestResponse(resp);
    }

}

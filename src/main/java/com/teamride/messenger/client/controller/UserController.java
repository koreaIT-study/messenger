package com.teamride.messenger.client.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import com.teamride.messenger.client.dto.AdminDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

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

    @PostMapping("/loginAction")
    public void loginAction(@RequestBody AdminDTO adminDTO) {
        Mono<AdminDTO> resp = webClient.mutate().baseUrl("http://localhost:12000")
                .build()
                .post()
                .uri("/loginAction")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(adminDTO)
                .retrieve()
                .bodyToMono(AdminDTO.class);
        log.debug("mono AdminDTO : {}", resp);
    }
}

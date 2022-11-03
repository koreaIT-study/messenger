package com.teamride.messenger.client.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import com.teamride.messenger.client.dto.AdminDTO;
import com.teamride.messenger.client.utils.RestResponse;

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
    public RestResponse loginAction(@RequestBody AdminDTO adminDTO) {
        Mono<RestResponse> resp = webClient.mutate().baseUrl("http://localhost:12000")
                .build()
                .post()
                .uri("/loginAction")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(adminDTO)
                .retrieve()
                .bodyToMono(RestResponse.class);
        log.debug("mono RestResponse : {}", resp);
        return resp.block();
    }

    @GetMapping("/smtpRequest")
    public RestResponse smtpRequest(@RequestParam String email) {
        log.debug("email {}", email);
        Mono<RestResponse> resp = webClient.mutate().baseUrl("http://localhost:12000")
                .build()
                .get()
                .uri(t -> t.queryParam("email", email).build())
                .retrieve()
                .bodyToMono(RestResponse.class);
        log.debug("mono RestResponse {}", resp);

        return resp.block();
    }

    @PostMapping("/signUp")
    public RestResponse signUp(@RequestBody AdminDTO adminDTO) {
        Mono<RestResponse> resp = webClient.mutate().baseUrl("http://localhost:12000")
                .build()
                .post()
                .uri("/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(adminDTO)
                .retrieve()
                .bodyToMono(RestResponse.class);
        return resp.block();
    }

}

package com.teamride.messenger.client.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import com.teamride.messenger.client.config.Constants;
import com.teamride.messenger.client.dto.FriendInfoDTO;
import com.teamride.messenger.client.dto.UserDTO;
import com.teamride.messenger.client.utils.RestResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.view.RedirectView;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final WebClient webClient;
    private final HttpSession httpSession;

    @GetMapping("/")
    public RedirectView index(){
        return new RedirectView("/login");
    }

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
        mv.addObject(Constants.LOGIN_SESSION, httpSession.getAttribute(Constants.LOGIN_SESSION));
        mv.addObject(Constants.LOGIN_SESSION_NAME, httpSession.getAttribute(Constants.LOGIN_SESSION_NAME));
        return mv;
    }

    public WebClient getWebClient() {
        return webClient.mutate()
            .build();
    }

    @PostMapping("/loginAction")
    public RestResponse loginAction(@RequestBody UserDTO userDTO) {
        try {
            final UserDTO resp = webClient.post()
                .uri("/loginAction")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, e -> Mono.error(new HttpClientErrorException(e.statusCode())))
                .onStatus(HttpStatus::is5xxServerError, e -> Mono.error(new HttpServerErrorException(e.statusCode())))
                .bodyToMono(UserDTO.class)
                .block();
            if (resp == null) {
                return new RestResponse("NOT_FOUND");
            }

            httpSession.setAttribute(Constants.LOGIN_SESSION, resp.getId());
            httpSession.setAttribute(Constants.LOGIN_SESSION_NAME, resp.getName());
            return new RestResponse(resp);
        } catch (Exception e) {
            return new RestResponse(1, e.getLocalizedMessage(), null);
        }
    }

    @GetMapping("/smtpRequest")
    public RestResponse smtpRequest(@RequestParam String email) {
        // TODO :: 구현 중
        final String resp = webClient.get()
            .uri(t -> t.queryParam("email", email)
                .build())
            .retrieve()
            .bodyToMono(String.class)
            .block();

        return new RestResponse(resp);
    }

    @PostMapping("/signUp")
    public RestResponse signUp(@RequestBody UserDTO userDTO) {
        // TODO :: 구현 중
        final Integer resp = webClient.post()
            .uri("/signUp")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(userDTO)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError, e -> Mono.error(new HttpClientErrorException(e.statusCode())))
            .onStatus(HttpStatus::is5xxServerError, e -> Mono.error(new HttpServerErrorException(e.statusCode())))
            .bodyToMono(Integer.class)
            .block();
        return new RestResponse(resp);
    }

    @PostMapping
    public RestResponse uploadImage(@RequestPart MultipartFile file) {
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", file);

            webClient.post()
                .uri("/uploadImage")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/getFriends")
    public RestResponse getFriends(@RequestParam int userId) {
        try {
            final List<FriendInfoDTO> resp = webClient.get()
                .uri(v -> v.path("/getFriends")
                    .queryParam(Constants.LOGIN_SESSION, userId)
                    .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, e -> Mono.error(new HttpClientErrorException(e.statusCode())))
                .onStatus(HttpStatus::is5xxServerError, e -> Mono.error(new HttpServerErrorException(e.statusCode())))
                .bodyToMono(new ParameterizedTypeReference<List<FriendInfoDTO>>() {
                })
                .block();
            return new RestResponse(resp);
        } catch (Exception e) {
            return new RestResponse(1, e.getLocalizedMessage(), null);
        }
    }

}

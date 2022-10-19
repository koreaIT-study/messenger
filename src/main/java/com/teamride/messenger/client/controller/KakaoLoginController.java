package com.teamride.messenger.client.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.teamride.messenger.client.config.ClientConfig;
import com.teamride.messenger.client.config.WebClientConfig;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class KakaoLoginController {
    private static final String REST_API_KEY = ClientConfig.getClientConfigInstance().getKakao().getRestApiKey();
    private static final String REDIRECT_URI = ClientConfig.getClientConfigInstance().getKakao().getRedirectUrl();
    
    @Autowired
    HttpSession session;
    
    @Autowired
    private WebClientConfig webClient;

    @GetMapping("/kakao_login")
    public void kakaoLogin(HttpServletResponse response) {
        StringBuilder url = new StringBuilder();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=" + REST_API_KEY);
        url.append("&redirect_uri="+REDIRECT_URI);
        url.append("&response_type=code");

        try {
            response.sendRedirect(url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/oauth", produces = "application/json", method = {RequestMethod.GET,
            RequestMethod.POST})
        public void kakaoLogin(@RequestParam("code") String code,
            HttpSession session) throws IOException {
        log.info("kakao authorize code is :: " + code);
            String accessToken = getKakaoAccessToken(code);
            session.setAttribute("access_token", accessToken); // 로그아웃할 때 사용된다
        
            getKakaoUserInfo(accessToken);
    }
        
       public String getKakaoAccessToken(String code) {
            // 카카오에 보낼 api
            WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
            
            // 카카오 서버에 요청 보내기 & 응답 받기
            JSONObject response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                    .path("/oauth/token")
                    .queryParam("grant_type", "authorization_code")
                    .queryParam("client_id", REST_API_KEY)
                    .queryParam("redirect_uri", REDIRECT_URI)
                    .queryParam("code", code)
                    .build())
                .retrieve().bodyToMono(JSONObject.class).block();
                
            log.info("response::"+response);
                return (String) response.get("access_token");
        }
       
       private JSONObject getKakaoUserInfo(String accessToken) {
           // 카카오에 요청 보내기 및 응답 받기
           WebClient webClient = WebClient.builder()
               .baseUrl("https://kapi.kakao.com")
               .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
               .build();
               
           JSONObject response = webClient.post()
               .uri(uriBuilder -> uriBuilder.path("/v2/user/me").build())
               .header("Authorization", "Bearer " + accessToken)
               .retrieve().bodyToMono(JSONObject.class).block();
           
           log.info("response::"+response);
           return response;
           // 받은 응답에서 원하는 정보 추출하기 (여기의 경우 회원 고유번호와 카카오 닉네임)
//           Integer id = (Integer) response.get("id");
//           Map<String, Object> map = (Map<String, Object>) 
//           response.get("kakao_account");
//           Map<String, Object> profile = (Map<String, Object>) map.get("profile");
//           String name = (String) profile.get("nickname");
           // 추출한 정보로 원하는 처리를 함
       }   
       
       
   @GetMapping(value = "/logout")
   public String kakaoLogout(HttpSession session) {
       String accessToken = (String) session.getAttribute("access_token");
       
       // 카카오에 요청 보내기
       WebClient webClient = WebClient.builder()
           .baseUrl("https://kapi.kakao.com")
           .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
           .build();
           
       JSONObject response = webClient.post()
           .uri(uriBuilder -> uriBuilder.path("/v1/user/logout").build())
           .header("Authorization", "Bearer " + accessToken)
           .retrieve().bodyToMono(JSONObject.class).block();
       log.info("logout response::"+response);
       // 로그아웃하면서 만료된 토큰을 세션에서 삭제
       session.removeAttribute("access_token");
       
       return "logout";
   }
   
   @GetMapping("/unlink")
   public void kakaoUnlink() {
    // 카카오에 요청 보내기
       WebClient webClient = WebClient.builder()
           .baseUrl("https://kapi.kakao.com")
           .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
           .build();
       
       JSONObject response = webClient.post()
               .uri(uriBuilder -> uriBuilder.path("/v1/user/logout").build())
               .header("Authorization", "Bearer " + session.getAttribute("access_token"))
               .retrieve().bodyToMono(JSONObject.class).block();
           log.info("unlink response::"+response);
   }

    
    @GetMapping("web-client-test")
    public Mono<String> test() {
        return webClient.getWebClient()
                .mutate()
                .baseUrl("http://localhost:12000")
                .build()
                .get()
                .uri("/test")
                .retrieve()
                .bodyToMono(String.class);
        
    }
}

package com.teamride.messenger.client.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.spi.http.HttpHandler;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.teamride.messenger.client.config.ClientConfig;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class NaverLoginController {
	private static final String REST_API_KEY = ClientConfig.getClientConfigInstance().getNaver().getRestApiKey();
	private static final String REDIRECT_URI = ClientConfig.getClientConfigInstance().getNaver().getRedirectUrl();
	private static final String CLIENT_SECRET = ClientConfig.getClientConfigInstance().getNaver().getClientSecret();

	@Autowired
	HttpSession httpSession;
	
	@Autowired
	HttpServletResponse httpServletResponse;

	/**
	 * 인가 코드 받기
	 * 
	 * @param resp
	 * @throws UnsupportedEncodingException
	 */
	@GetMapping("/naver_login")
	public void naverLogin() throws UnsupportedEncodingException {
		StringBuilder apiURL = new StringBuilder();
		String redirectURI = URLEncoder.encode(REDIRECT_URI, "UTF-8");
		SecureRandom random = new SecureRandom();
		String state = new BigInteger(130, random).toString();

		apiURL.append("https://nid.naver.com/oauth2.0/authorize?response_type=code");
		apiURL.append("&client_id=" + REST_API_KEY);
		apiURL.append("&redirect_uri=" + redirectURI);
		apiURL.append("&state=" + state);

		try {
			httpServletResponse.sendRedirect(apiURL.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 접근 토큰 발급 요청
	 * 
	 * @param code
	 * @param state
	 */
	@RequestMapping(value = "/api/naver/oauth", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = "application/json")
	public void callback(String code, String state) {
		try {
			WebClient webclient = WebClient.builder()
					.baseUrl("https://nid.naver.com")
					.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					.build();
			
			JSONObject response = webclient.post()
					.uri(uriBuilder-> uriBuilder
							.path("/oauth2.0/token")
							.queryParam("client_id", REST_API_KEY)
							.queryParam("client_secret", CLIENT_SECRET)
							.queryParam("grant_type", "authorization_code")
							.queryParam("state", state)
							.queryParam("code", code)
							.build())
					.retrieve().bodyToMono(JSONObject.class).block();
			
			String token = (String) response.get("access_token");
			getUserInfo(token);
			
			httpServletResponse.sendRedirect("/friend");
		} catch (Exception e) {
			 log.error("naver login error :: ", e);
             // TODO login에 error param넣어서 에러발생 popup 넣음
             // 어떤 error가 나는지 확인
			try {
				httpServletResponse.sendRedirect("/login");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 토큰으로 사용자 정보 가져오기
	 * @param accessToken
	 */
	public void getUserInfo(String accessToken) {
		WebClient webclient = WebClient.builder()
				.baseUrl("https://openapi.naver.com")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
		
		JSONObject jsonVal = webclient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/v1/nid/me")
						.build())
				.header("Authorization", "Bearer " + accessToken)
				.retrieve()
				.bodyToMono(JSONObject.class).block();
		
		// 원하는 정보 추출
		Map<String, Object> resp = (Map<String, Object>) jsonVal.get("response"); 
		String email = (String) resp.get("email");
		String name = (String) resp.get("name");
		
		log.info("email::" + email);
		log.info("name::" + name);
		
		// 서버쪽으로 email, nickname 전송 후 사용자 없으면 insert하고 로그인 처리
        httpSession.setAttribute("userEmail", email); // 로그아웃할 때 사용된다
        httpSession.setAttribute("name", name);
	}

}

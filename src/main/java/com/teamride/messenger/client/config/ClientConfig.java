package com.teamride.messenger.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.teamride.messenger.client.utils.BeanUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConfigurationProperties("messenger")
@Data
@Slf4j
public class ClientConfig {
    private Kakao kakao = new Kakao();
    
    @Data
    public class Kakao {
       private String restApiKey;
       private String redirectUrl;
    }
    
    public static ClientConfig getClientConfigInstance() {
        return BeanUtils.getBean(ClientConfig.class);
    }
}

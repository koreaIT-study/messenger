package com.teamride.messenger.client.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient getWebClient() {
        ConnectionProvider provider = ConnectionProvider.builder("custom-provider").maxConnections(100) // 유지할 Connection Pool의 수, default: (프로세서수  * 2)
                .maxIdleTime(Duration.ofSeconds(100)) // 사용하지 않는 상태(idle)의 Connection이 유지되는 시간. default : -1, 무제한
                .maxLifeTime(Duration.ofSeconds(100)) //  Connection Pool 에서의 최대 수명 시간. default : -1, 무제한
                .pendingAcquireTimeout(Duration.ofMillis(5000)) // Connection Pool에서 사용할 수 있는 Connection 이 없을때 (모두 사용중일때) Connection을 얻기 위해 대기하는 시간. default 45초
                .pendingAcquireMaxCount(-1) // Connection을 얻기 위해 대기하는 최대 수 default : -1 무제한
                .evictInBackground(Duration.ofSeconds(30)) // 백그라운드에서 만료된 connection을 제거하는 주기
                .lifo() //  마지막에 사용된 커넥션을 재 사용, fifo – 처음 사용된(가장오래된) 커넥션을 재 사용
                .build();

        //Memory 조정: 2M (default 256KB)
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs()
                .maxInMemorySize(2 * 1024 * 1024))
                .build();

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(reactor.netty.http.client.HttpClient.create(provider)))
                .exchangeStrategies(exchangeStrategies)
                .defaultHeader("user-agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Safari/537.3")
                .defaultCookie("httpclient-type", "webclient")
                .build();
    }
}

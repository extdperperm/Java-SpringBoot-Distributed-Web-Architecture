package es.dsw.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/******************************************************
 *                   WebClient                        *
 * Es la forma moderna de consumir servicios ApiRest. *
 ******************************************************/
@Configuration
public class WebClientConfig {

    @Bean
    WebClient webClient() {
        return WebClient.builder().build();
    }
}
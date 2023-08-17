package dev.siriuz.webfluxdemo.config;

import org.springframework.web.reactive.function.client.WebClient;

public class WikimediaClientConfig {

    public WebClient wikimediaClient(){
        return WebClient.builder()
                .baseUrl("https://stream.wikimedia.org")
                .build();
    }
}

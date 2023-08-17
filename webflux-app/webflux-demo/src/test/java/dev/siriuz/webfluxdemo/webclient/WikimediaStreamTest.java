package dev.siriuz.webfluxdemo.webclient;

import dev.siriuz.webfluxdemo.config.WikimediaClientConfig;
import dev.siriuz.webfluxdemo.util.SleepUtil;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

public class WikimediaStreamTest {

    @Test
    public void wikimediaTest(){
        WikimediaClientConfig config = new WikimediaClientConfig();
        WebClient webClient = config.wikimediaClient();

        webClient.get().uri("v2/stream/recentchange")
                .retrieve()
                .bodyToFlux(String.class)
                .subscribe(System.out::println);

        SleepUtil.sleepSeconds(20);


    }

}

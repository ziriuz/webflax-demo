package dev.siriuz.webfluxdemo.webclient;

import dev.siriuz.webfluxdemo.dto.MultiplyRequestDto;
import dev.siriuz.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class HeadersTest extends BaseTest{

    @Autowired
    private WebClient webClient;

    @Test
    public void sendHeaderTest(){
        Mono<Response> responseMono = webClient.post()
                .uri("reactive-math/multiply")
                .bodyValue(buildRequestDto(3, 4))
                .headers(h -> h.set("OP", "add"))
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseMono)
                .expectNextMatches(r -> r.getOutput() == 12)
                .verifyComplete();
        // Second client to check that token will be generated for every client
        StepVerifier.create(responseMono)
                .expectNextMatches(r -> r.getOutput() == 12)
                .verifyComplete();
    }

    private MultiplyRequestDto buildRequestDto(int a, int b){
        MultiplyRequestDto dto = new MultiplyRequestDto();
        dto.setFirst(a);
        dto.setSecond(b);
        return dto;
    }
}

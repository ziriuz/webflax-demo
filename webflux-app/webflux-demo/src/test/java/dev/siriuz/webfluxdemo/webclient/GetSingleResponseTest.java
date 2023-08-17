package dev.siriuz.webfluxdemo.webclient;

import dev.siriuz.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class GetSingleResponseTest extends BaseTest{

    @Autowired
    private WebClient webClient;

    @Test
    public void blockTest(){
        Response response = this.webClient.get()
                .uri("reactive-math/square/{number}", 5)
                .retrieve()
                .bodyToMono(Response.class)
                .block();
        System.out.println(response);
        Assertions.assertEquals(response.getOutput(), 25);
    }

    @Test
    public void stepVerifierTest(){
        Mono<Response> responseMono = webClient.get()
                .uri("reactive-math/square/{number}", 5)
                .retrieve()
                .bodyToMono(Response.class);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.getOutput() == 25)
                .verifyComplete();
    }
}

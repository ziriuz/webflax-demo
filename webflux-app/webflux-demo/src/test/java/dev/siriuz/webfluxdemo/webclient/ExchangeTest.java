package dev.siriuz.webfluxdemo.webclient;

import dev.siriuz.webfluxdemo.dto.InputFailedValidationResponse;
import dev.siriuz.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ExchangeTest extends BaseTest{

    @Autowired
    private WebClient webClient;

    @Test
    public void badRequestTest(){
        Mono<Object> responseMono = this.webClient
                .get()
                .uri("reactive-math/square/{number}/throw", 3)
                // exchange = retrieve + additional httpstatus code
                .exchangeToMono(this::exchange)
                .doOnNext(System.out::println)
                .doOnError(err -> System.out.println(err.getMessage()))
                ;

        responseMono.subscribe(
                r -> System.out.println(((Response) r).getOutput())
        );

        StepVerifier.create(responseMono)
              .expectNextCount(1)
              .verifyComplete();
    }

    private Mono<Object> exchange(ClientResponse cr){
        if(cr.rawStatusCode() == 400)
            return cr.bodyToMono(InputFailedValidationResponse.class);
        else
            return cr.bodyToMono(Response.class);
    }

}

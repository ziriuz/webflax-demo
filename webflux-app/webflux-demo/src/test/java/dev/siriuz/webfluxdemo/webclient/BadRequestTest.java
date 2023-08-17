package dev.siriuz.webfluxdemo.webclient;

import dev.siriuz.webfluxdemo.dto.Response;
import dev.siriuz.webfluxdemo.exception.InputValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BadRequestTest extends BaseTest{

    @Autowired
    private WebClient webClient;

    private Mono<InputValidationException> exchange(ClientResponse cr){
        return Mono.just(new InputValidationException(cr.rawStatusCode()));
    }
    @Test
    public void badRequestTest(){
        int input = 3;
        Mono<Response> responseMono = this.webClient
                .get()
                .uri("reactive-math/square/{number}/throw", input)
                .retrieve()
                //.onRawStatus(code -> code == 400,this::exchange)
                .bodyToMono(Response.class)
                .doOnNext(r -> System.out.println("doOnNext: " + r.getOutput()))
                .doOnError(err -> System.out.println("doOnError: " + err.getMessage()));

        responseMono.subscribe(
                r -> System.out.println("Next: " + r.getOutput()),
                e -> System.out.println("Bad request for input " + input + " " + e.getMessage())
        );

        StepVerifier.create(responseMono)
                        .verifyError(WebClientResponseException.BadRequest.class);
        //StepVerifier.create(responseMono)
          //      .expectNextCount(1)
          //      .verifyComplete();
    }
}

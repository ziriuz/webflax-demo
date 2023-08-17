package dev.siriuz.webfluxdemo.webclient;

import dev.siriuz.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class CalculatorTest extends BaseTest{

    @Autowired
    private WebClient webClient;

    @Test
    public void multyOpTest(){

        webClient.get()
                .uri("calc/10/12,2")
                .headers(h -> h.set("OP","+,*"))
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(System.out::println)
                .block();
    }

    @Test
    public void multyOp2Test(){
        String[] ops = {"+","-","*","/","-"};
        final String FORMAT = "%d %s %d = %d";
        final int A = 10;

        Flux<String> stringFlux =
        Flux.range(1,5)
                .flatMap(i -> Flux.just("+", "-", "*", "/")
                                .flatMap( op ->
        webClient.get()
                .uri("calc/{a}/{b}", A, i)
                .headers(h -> h.set("OP",op))
                .retrieve()
                .bodyToMono(Response.class)
                .map(response -> String.format(FORMAT,A,op,i,response.getOutput()))
                .doOnNext(System.out::println)
        )
        );

        StepVerifier.create(stringFlux)
                .expectNextCount(20)
                .verifyComplete();
    }
}

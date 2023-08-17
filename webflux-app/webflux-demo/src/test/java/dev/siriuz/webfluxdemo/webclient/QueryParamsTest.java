package dev.siriuz.webfluxdemo.webclient;

import dev.siriuz.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class QueryParamsTest extends BaseTest{

    @Autowired
    private WebClient webClient;

    String queryString = "http://localhost:8080/reactive-math/table?number={number}";

    @Test
    public void queryParamsFullUriTest(){
        URI uri = UriComponentsBuilder.fromUriString(queryString)
                .build(12);

        Flux<Response> integerFlux = this.webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(integerFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void queryParamsRelativePathTest(){
        Flux<Response> integerFlux = this.webClient
                .get()
                .uri( uriBuilder -> uriBuilder.path("reactive-math/table").query("number={number}").build(12))
                .retrieve()
                .bodyToFlux(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(integerFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test void queryParamsAsMapTest(){
        Map<String, Integer> params = new HashMap<>();
        params.put("number", 12);
        params.put("count", 2);

        Flux<Response> integerFlux = this.webClient
                .get()
                .uri( uriBuilder -> uriBuilder.path("reactive-math/table").query("number={number}").build(params))
                .retrieve()
                .bodyToFlux(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(integerFlux)
                .expectNextCount(10)
                .verifyComplete();
    }
}

package dev.siriuz.webfluxdemo.webtestclient;

import dev.siriuz.webfluxdemo.controller.ReactiveMathController;
import dev.siriuz.webfluxdemo.dto.Response;
import dev.siriuz.webfluxdemo.service.ReactiveMathService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/*
Unit testing of Controller in isolation (assuming other layers - Service and Repository are working fine)
No Spring context and no Beans are created
Mocking is used to replace calls to Service
 */
@WebFluxTest(ReactiveMathController.class)
public class ControllerGetTest {

    @Autowired
    WebTestClient client;

    @MockBean
    private ReactiveMathService reactiveMathService;

    @Test
    public void singleResponseTest(){

        Mockito.when(reactiveMathService.findSquare(Mockito.anyInt()))
                        .thenReturn(Mono.just(new Response(25)));
        client.get()
                .uri("/reactive-math/square/{number}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Response.class)
                .value(r -> Assertions.assertThat(r.getOutput()).isEqualTo(25));
    }

    @Test
    public void listResponseTest(){

        Flux<Response> flux = Flux.range(1,3)
                        .map(Response::new);
        Mockito.when(reactiveMathService.multiplicationTable(Mockito.anyInt()))
                .thenReturn(flux);
        // Error scenario
        // Mockito.when(reactiveMathService.multiplicationTable(Mockito.anyInt()))
        //        .thenReturn(Flux.error(new IllegalStateException()));
        client.get()
                .uri("/reactive-math/table/{number}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Response.class)
                .hasSize(3)
                //.value(list -> Assert list)
        ;
    }

    @Test
    public void streamingResponseTest(){

        Flux<Response> flux = Flux.range(1,3)
                .map(Response::new)
                .delayElements(Duration.ofMillis(100));
        Mockito.when(reactiveMathService.multiplicationTable(Mockito.anyInt()))
                .thenReturn(flux);
        client.get()
                .uri("/reactive-math/table/{number}/stream", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM_VALUE)
                .expectBodyList(Response.class)
                .hasSize(3)
        //.value(list -> Assert list)
        ;
    }

    @Test
    public void paramsTest(){

        final int input = 3;

        Flux<Response> flux = Flux.range(1,10)
                .map( i -> new Response(i * input));
        Mockito.when(reactiveMathService.multiplicationTable(Mockito.anyInt()))
                .thenReturn(flux);

        Map<String, Integer> params = new HashMap<>();
        params.put("number", input);

        this.client
                .get()
                .uri( uriBuilder -> uriBuilder.path("/reactive-math/table")
                        .query("number={number}")
                        .build(params)
                )
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Response.class)
                .hasSize(10)
                .contains(new Response(12))
                .value( list -> Assertions.assertThat(
                                list.get(9).getOutput()).isEqualTo(30)
                );

    }
}

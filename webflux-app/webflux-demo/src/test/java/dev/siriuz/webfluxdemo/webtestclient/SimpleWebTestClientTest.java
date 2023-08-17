package dev.siriuz.webfluxdemo.webtestclient;

import dev.siriuz.webfluxdemo.dto.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/*
SpringBootTest will create context, all Beans and bring up Netty
So it is kind of integration testing which includes all layers
Controller -> Service -> Repository -> DB
No mocking is used
*/
@SpringBootTest
@AutoConfigureWebTestClient
public class SimpleWebTestClientTest {

    @Autowired
    WebTestClient client;

    @Test
    public void stepVerifierTest(){
        Flux<Response> responseMono = client.get()
                .uri("/reactive-math/square/{number}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Response.class)
                .getResponseBody();


        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.getOutput() == 25)
                .verifyComplete();
    }

    @Test
    public void fluentAssertionTest(){
          client.get()
                .uri("/reactive-math/square/{number}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Response.class)
                .value(r -> Assertions.assertThat(r.getOutput()).isEqualTo(25));
    }


}

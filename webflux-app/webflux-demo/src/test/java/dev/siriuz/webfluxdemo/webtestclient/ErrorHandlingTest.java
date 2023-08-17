package dev.siriuz.webfluxdemo.webtestclient;

import dev.siriuz.webfluxdemo.controller.ReactiveMathValidationController;
import dev.siriuz.webfluxdemo.dto.MultiplyRequestDto;
import dev.siriuz.webfluxdemo.dto.Response;
import dev.siriuz.webfluxdemo.service.ReactiveMathService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(ReactiveMathValidationController.class)
public class ErrorHandlingTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private ReactiveMathService reactiveMathService;

    private static final String MSG = "allowed range is 10 - 20";
    private static final int ERROR_CODE = 100;

    @Test
    public void ReactiveMathValidationControllerFindSquareErrorHandlingTest(){

        Mockito.when(reactiveMathService.findSquare(Mockito.anyInt()))
                .thenReturn(Mono.just(new Response(25)));

        client.get()
                .uri("/reactive-math/square/{input}/throw", 5 ) //10-20 valid
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo(MSG)
                .jsonPath("$.errorCode").isEqualTo(ERROR_CODE)
                .jsonPath("$.input").isEqualTo(5);


    }
}

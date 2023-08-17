package dev.siriuz.webfluxdemo.webtestclient;

import dev.siriuz.webfluxdemo.controller.ReactiveMathController;
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

@WebFluxTest(controllers = {ReactiveMathController.class, ReactiveMathValidationController.class})
public class ControllerPostTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private ReactiveMathService reactiveMathService;

    @Test
    public void ReactiveMathControllerPostMultiplyTest(){

        Mockito.when(reactiveMathService.multiply(Mockito.any()))
                .thenReturn(Mono.just(new Response(12)));

        client.post()
                .uri("/reactive-math/multiply")
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBasicAuth("user","pass"))
                .headers(h -> h.set("someKey", "VALUE"))
                .bodyValue(new MultiplyRequestDto())
                .exchange()
                .expectStatus().is2xxSuccessful();

    }


}

package dev.siriuz.webfluxdemo.webtestclient;

import dev.siriuz.webfluxdemo.config.RequestHandler;
import dev.siriuz.webfluxdemo.config.RouterConfig;
import dev.siriuz.webfluxdemo.dto.Response;
import dev.siriuz.webfluxdemo.service.ReactiveMathService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {RouterConfig.class})
public class RouterFunctionTest {

    private WebTestClient client;

    // Option 1 - autowire all needed beans one by one
    @Autowired
    private RouterConfig config;

    // Option 2 - (better) autowire ApplicationContext
    @Autowired
    private ApplicationContext ctx;

    @MockBean
    private RequestHandler handler;


    @BeforeAll
    public void setClient(){
        // Option 1
        // this.client = WebTestClient.bindToRouterFunction(this.config.mathPathRouter())
        //        .build();
        // Option 2
        this.client = WebTestClient.bindToApplicationContext(ctx)
                .build();

        // WebTestClient can be used to talk to remote server
        // WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    }

    @Test
    public void routerFunctionMathPathRouterTest(){

        Mockito.when(handler.squareHandler(Mockito.any()))
                .thenReturn(ServerResponse.ok().bodyValue(new Response(25)));

        client.get()
                .uri("/router/square/{input}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Response.class)
                .value(r -> Assertions.assertEquals(r.getOutput(),25));
    }



}

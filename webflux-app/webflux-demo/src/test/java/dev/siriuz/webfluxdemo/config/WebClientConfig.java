package dev.siriuz.webfluxdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                // basic authentication
                // .defaultHeaders(h -> h.setBasicAuth("user","password"))
                // token authentication
                // .filter(this::sessionToken)
                // authentication selected based on attribute value
                .filter(this::sessionAuth)
                .build();
    }

    private Mono<ClientResponse> sessionToken(ClientRequest request, ExchangeFunction exchFunc){
        System.out.println(">>> Generating session token");
        ClientRequest clientRequest = ClientRequest.from(request)
                .headers(h -> h.setBearerAuth("my-token")).build();
        return exchFunc.exchange(clientRequest);
    }

    private Mono<ClientResponse> sessionAuth(ClientRequest request, ExchangeFunction exchFunc){
        System.out.println(">>> Authentication: " + request.attribute("auth").orElse("None"));
        ClientRequest clientRequest = request.attribute("auth")
                .map(v -> v.equals("basic") ? withBasicAuth(request): withOAuth(request))
                .orElse(request);
        return exchFunc.exchange(clientRequest);
    }

    private ClientRequest withBasicAuth(ClientRequest request){
        return ClientRequest.from(request)
                .headers(h -> h.setBasicAuth("user","password"))
                .build();
    };

    private ClientRequest withOAuth(ClientRequest request){
        return ClientRequest.from(request)
                .headers(h -> h.setBearerAuth("my-token-jwt(json-web-token)"))
                .build();

    };
}

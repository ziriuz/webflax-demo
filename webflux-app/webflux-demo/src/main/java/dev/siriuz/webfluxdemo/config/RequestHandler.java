package dev.siriuz.webfluxdemo.config;

import dev.siriuz.webfluxdemo.dto.MultiplyRequestDto;
import dev.siriuz.webfluxdemo.dto.Response;
import dev.siriuz.webfluxdemo.exception.InputValidationException;
import dev.siriuz.webfluxdemo.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RequestHandler {
    @Autowired
    private ReactiveMathService mathService;

    /*
     *  Simple example to handle GET request with path variable
     *  and return Mono response
     */
    public Mono<ServerResponse> squareHandler(ServerRequest serverRequest){

        int input = Integer.parseInt(serverRequest.pathVariable("input"));

        Mono<Response> responseMono = mathService.findSquare(input);

        return ServerResponse.ok().body(responseMono, Response.class);
    }

    /*
     *  Simple example to handle GET request with path variable
     *  and return Flux response.
     *  Note: ServerResponse is Mono but actual Response is a Flux publisher which is included into body
     */
    public Mono<ServerResponse> multiplicationTableHandler(ServerRequest serverRequest){

        int input = Integer.parseInt(serverRequest.pathVariable("input"));

        Flux<Response> responseFlux = mathService.multiplicationTable(input);

        return ServerResponse.ok().body(responseFlux, Response.class);
    }

    /*
     * Example how to expose event stream
     */
    public Mono<ServerResponse> multiplicationTableStreamHandler(ServerRequest serverRequest){

        int input = Integer.parseInt(serverRequest.pathVariable("input"));

        Flux<Response> responseFlux = mathService.multiplicationTable(input);

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(responseFlux, Response.class);
    }

    /*
     *  Simple example to handle POST request with data passed as body of request
     *  and return Mono response
     */
    public Mono<ServerResponse> multiplyHandler(ServerRequest serverRequest){

        Mono<MultiplyRequestDto> requestDtoMono = serverRequest.bodyToMono(MultiplyRequestDto.class);

        Mono<Response> responseMono = this.mathService.multiply(requestDtoMono);

        serverRequest.headers().asHttpHeaders()
                .forEach((k,v) -> System.out.println("header: " + k + " = " + v));

        return ServerResponse.ok()
                //.contentType(MediaType.TEXT_EVENT_STREAM)
                .body(responseMono, Response.class);
    }

    /*
     *  Simple example to handle GET request with validation and Exception
     *
     */
    public Mono<ServerResponse> squareHandlerWithValidation(ServerRequest serverRequest){

        int input = Integer.parseInt(serverRequest.pathVariable("input"));

        if(input < 10 || input > 20){
            /* //Error response is generated inside handler
            InputFailedValidationResponse response = new InputFailedValidationResponse();
            response.setErrorCode(200);
            response.setMessage("Invalid input value, should be in range from 10 to 20");
            response.setInput(input);
            return ServerResponse.badRequest().bodyValue(response);*/


            // Better option in Reactive style using Exception
            return Mono.error(new InputValidationException(input));
        }

        Mono<Response> responseMono = mathService.findSquare(input);

        return ServerResponse.ok().body(responseMono, Response.class);
    }
}

package dev.siriuz.webfluxdemo.config;

import dev.siriuz.webfluxdemo.dto.InputFailedValidationResponse;
import dev.siriuz.webfluxdemo.exception.InputValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Configuration
public class RouterConfig {

    @Autowired
    private RequestHandler requestHandler;

    @Bean
    public RouterFunction<ServerResponse> serverResponseRouterFunction(){
        // === Single route ===
        // return RouterFunctions.route(
        //       GET("router/square/{input}"),requestHandler::squareHandler);
        // === Multiple routes ===
        return RouterFunctions.route()
                .GET("router/square/{input}", requestHandler::squareHandler)
                .GET("router/table/{input}", requestHandler::multiplicationTableHandler)
                .GET("router/table/{input}/stream", requestHandler::multiplicationTableStreamHandler)
                .POST("router/multiply", requestHandler::multiplyHandler)
                .GET("router/square/{input}/validation", requestHandler::squareHandlerWithValidation)
                .onError(InputValidationException.class, exceptionHandler())
                .build();
    }

    /*
     * Path config example
     */
    @Bean
    public RouterFunction<ServerResponse> mathPathRouter() {
        return RouterFunctions.route()
                .path("math", this::mathRouter)
                .build();
    }
    private RouterFunction<ServerResponse> mathRouter(){
        return RouterFunctions.route()
                .GET("square/{input}", requestHandler::squareHandler)
                .GET("square/{input}/validation", requestHandler::squareHandlerWithValidation)
                .GET("table/{input}", requestHandler::multiplicationTableHandler)
                .GET("table/{input}/stream", requestHandler::multiplicationTableStreamHandler)
                .POST("multiply", requestHandler::multiplyHandler)
                .onError(InputValidationException.class, exceptionHandler())
                .build();
    }

    /*
     * Using predicates example
     */
    @Bean
    public RouterFunction<ServerResponse> mathPathPredicateRouter() {
        return RouterFunctions.route()
                .path("math-test", this::mathWithPredicateRouter)
                .build();
    }
    private RouterFunction<ServerResponse> mathWithPredicateRouter(){
        return RouterFunctions.route()
                .GET("square/{input}",
                            RequestPredicates.path("*/1?")
                        .or(RequestPredicates.path("*/20"))
                        , requestHandler::squareHandler)
                .GET("square/{input}", req -> ServerResponse.badRequest().bodyValue("only 10-20 allowed"))
                .build();
    }
    private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler(){
        return (err, req) -> {
            InputValidationException ex = (InputValidationException) err;
            InputFailedValidationResponse response = new InputFailedValidationResponse();
            response.setErrorCode(ex.getErrorCode());
            response.setMessage(ex.getMessage());
            response.setInput(ex.getInput());
            return ServerResponse.badRequest().bodyValue(response);
        };
    }

    /*
     * The same as exceptionHandler function, but using traditional implementation as class
     * The same can be implemented using regular not nested class
     */
    private static class ValidationErrorHandler implements BiFunction<Throwable, ServerRequest, Mono<ServerResponse>>{
        @Override
        public Mono<ServerResponse> apply(Throwable throwable, ServerRequest serverRequest) {
            InputValidationException ex = (InputValidationException) throwable;
            InputFailedValidationResponse response = new InputFailedValidationResponse();
            response.setErrorCode(ex.getErrorCode());
            response.setMessage(ex.getMessage());
            response.setInput(ex.getInput());
            return ServerResponse.badRequest().bodyValue(response);
        }
    }
}

package dev.siriuz.webfluxdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

import java.util.Objects;

@Configuration
public class CalcRouterConfig {

    @Autowired
    CalculatorRequestHandler calculatorRequestHandler;

    @Bean
    public RouterFunction<ServerResponse> calculatorRouter(){
        return RouterFunctions.route()
                .GET("calculator/{x}/{y}",
                        req -> !req.headers().asHttpHeaders().containsKey("OP"),
                        req -> ServerResponse.badRequest().bodyValue("OP header is missing"))
                .GET("calculator/{x}/{y}",
                        req -> getHeaderValue(req, "OP").equals("+")
                        , calculatorRequestHandler::add)
                .GET("calculator/{x}/{y}",
                        req -> getHeaderValue(req, "OP").equals("-")
                        , calculatorRequestHandler::subtract)
                .GET("calculator/{x}/{y}",
                        req -> getHeaderValue(req, "OP").equals("*")
                        , calculatorRequestHandler::multiply)
                .GET("calculator/{x}/{y}",
                        req -> getHeaderValue(req, "OP").equals("/")
                        , calculatorRequestHandler::divideHandler)
                .GET("calculator/{x}/{y}",
                        req -> ServerResponse.badRequest()
                                .bodyValue("Invalid header : Unsupported operation " + getHeaderValue(req, "OP")))
                .onError(NumberFormatException.class,
                        (err, req) -> ServerResponse.badRequest().bodyValue("Invalid path variable: " + err.getMessage()))
                .build();
    }

    private String getHeaderValue(ServerRequest request, String key){
        if (!request.headers().asHttpHeaders().containsKey(key))
            return "null";
        return Objects.requireNonNull(request.headers().header(key).get(0));
    }

    /*
     * Same logic, but better code
     */
    private RequestPredicate isOperation(String operation){
        return RequestPredicates.headers(
                headers -> headers.asHttpHeaders().containsKey("OP") &&
                        headers.asHttpHeaders().toSingleValueMap().get("OP").equals(operation));
    }
    private RequestPredicate containsOperation(){
        return RequestPredicates.headers(
                headers -> headers.asHttpHeaders().containsKey("OP"));
    }
    @Bean
    public RouterFunction<ServerResponse> calcRouter(){
        return RouterFunctions.route()
                //.GET("calc/{x}/{y}",containsOperation().negate(),
                //        req -> ServerResponse.badRequest().bodyValue("OP header is missing"))
                .GET("calc/{x}/{y}", isOperation("+"), calculatorRequestHandler::add)
                .GET("calc/{x}/{y}", isOperation("-"), calculatorRequestHandler::subtract)
                .GET("calc/{x}/{y}", isOperation("*"), calculatorRequestHandler::multiply)
                .GET("calc/{x}/{y}", isOperation("/"), calculatorRequestHandler::divideHandler)
                .GET("calc/{x}/{y}", containsOperation(),
                        req -> calculatorRequestHandler.calcAll(req, getHeaderValue(req,"OP")))
                .GET("calc/{x}/{y}",  req -> ServerResponse.badRequest()
                                .bodyValue("Invalid header : Unsupported operation " + getHeaderValue(req, "OP")))
                .onError(NumberFormatException.class,
                        (err, req) -> ServerResponse.badRequest().bodyValue("Invalid path variable: " + err.getMessage()))
                .build();
    }
}

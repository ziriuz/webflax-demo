package dev.siriuz.webfluxdemo.controller;

import dev.siriuz.webfluxdemo.dto.Response;
import dev.siriuz.webfluxdemo.exception.InputValidationException;
import dev.siriuz.webfluxdemo.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("reactive-math")
public class ReactiveMathValidationController {

    @Autowired
    ReactiveMathService mathService;

    @GetMapping("square/{input}/throw")
    public Mono<Response> findSquare(@PathVariable int input){
        if(input < 10 || input > 20)
            throw new InputValidationException(input);
        return this.mathService.findSquare(input);
    }

    @GetMapping("square/{input}/error")
    public Mono<Response> findSquareErr(@PathVariable int input){
        // Validation is done as a part of pipeline (reactive style)
        // Exception is handled by @ControllerAdvice,InputValidationHandler class
        return Mono.just(input)
                .handle((i , sync) -> {
                    if(i >= 10 && i <= 20)
                        sync.next(i);
                    else
                        sync.error(new InputValidationException(i));
                })
                .cast(Integer.class)
                .flatMap(mathService::findSquare);
    }

    @GetMapping("square/{input}/range")
    public Mono<ResponseEntity<Response>> findSquarePure(@PathVariable int input){
        // Validation is done as a part of pipeline (reactive style)
        // Empty body in response
        return Mono.just(input)
                .filter(i -> i >= 10 && i <= 20)
                .flatMap(mathService::findSquare)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build())
                ;
    }
}

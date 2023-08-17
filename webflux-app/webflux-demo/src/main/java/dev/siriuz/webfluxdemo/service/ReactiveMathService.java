package dev.siriuz.webfluxdemo.service;

import dev.siriuz.webfluxdemo.dto.MultiplyRequestDto;
import dev.siriuz.webfluxdemo.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;

@Service
public class ReactiveMathService {

    @Autowired
    MathService mathService;
    public Mono<Response> findSquare(int input){
        return Mono.fromSupplier(() -> new Response(input * input));
        // return Mono.fromSupplier(() -> input * input)
        //      .map(Response::new);
    }

    public Flux<Response> multiplicationTable(int input){
        return Flux.range(1,10)
                //.doOnNext(x -> SleepUtil.sleepSeconds(1))
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(x -> System.out.println(new Date() + "ReactiveMathService processing mult " + input + " by " + x))
                .map(x -> x * input)
                .map(Response::new);

    }

    public Mono<Response> multiply(Mono<MultiplyRequestDto> dtoMono){
        return dtoMono.map(req -> new Response(req.getFirst() * req.getSecond()));
    }

    public Mono<Response> add(int x, int y){
        return Mono.fromSupplier(() -> mathService.add(x, y));
    }
    public Mono<Response> subtract(int x, int y){
        return Mono.fromSupplier(() -> mathService.subtract(x, y));
    }
    public Mono<Response> multiply(int x, int y){
        return Mono.fromSupplier(() -> mathService.multiply(x, y));
    }
    public Mono<Response> divide(int x, int y){
        return Mono.fromSupplier(() -> mathService.divide(x, y));
    }
}

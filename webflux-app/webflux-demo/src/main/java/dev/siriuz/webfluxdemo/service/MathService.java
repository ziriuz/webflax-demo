package dev.siriuz.webfluxdemo.service;

import dev.siriuz.webfluxdemo.dto.Response;
import dev.siriuz.webfluxdemo.util.SleepUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MathService {
    public Response findSquare(int input) {
        return new Response(input * input);
    }

    public List<Response> multiplicationTable(int input){
        return IntStream.rangeClosed(1,10)
                .peek(i -> SleepUtil.sleepSeconds(1)) // simulation of time-consuming operation
                .peek(i -> System.out.println(new Date() + "MathService processing mult " + input + " by " + i))
                .mapToObj(i -> new Response(i * input))
                .collect(Collectors.toList());
    }

    public Response add(int x, int y) {
        return new Response(x + y);
    }
    public Response subtract(int x, int y) {
        return new Response(x - y);
    }
    public Response multiply(int x, int y) {
        return new Response(x * y);
    }
    public Response divide(int x, int y) {
        return new Response(x / y);
    }

}

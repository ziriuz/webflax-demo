package dev.siriuz.webfluxdemo.config;

import dev.siriuz.webfluxdemo.dto.Response;
import dev.siriuz.webfluxdemo.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Service
public class CalculatorRequestHandler {

    @Autowired
    ReactiveMathService mathService;

    public Mono<ServerResponse> add(ServerRequest request){
        try {
            int x = Integer.parseInt(request.pathVariable("x"));
            int y = Integer.parseInt(request.pathVariable("y"));
            return ServerResponse.ok().body(mathService.add(x,y),Response.class);
        }
        catch(NumberFormatException ex){
            return Mono.error(ex);
        }
        // Validation that input variables are numbers should be added
        // The same but more beautiful code is in below methods
    }
    public Mono<ServerResponse> subtract(ServerRequest request){
        return ServerResponse.ok().body(process(request, (x,y) -> mathService.subtract(x,y)), Response.class);
    }
    public Mono<ServerResponse> multiply(ServerRequest request){
        return ServerResponse.ok().body(process(request, (x,y) -> mathService.multiply(x,y)), Response.class);
    }
    public Mono<ServerResponse> divideHandler(ServerRequest request){
        return process2(request, (x,y) ->
                   y != 0 ? ServerResponse.ok().body(mathService.divide(x,y), Response.class):
                            Mono.error(new NumberFormatException("Division by 0"))
        );
    }

    private Mono<Response> process(ServerRequest request,
                                   BiFunction<Integer, Integer,Mono<Response>> operation){
        int x = getValue(request, "x");
        int y = getValue(request, "y");
        return operation.apply(x, y);
    }

    private Mono<ServerResponse> process2(ServerRequest request,
                                   BiFunction<Integer, Integer,Mono<ServerResponse>> operation){
        int x = getValue(request, "x");
        int y = getValue(request, "y");
        return operation.apply(x, y);
    }

    private int getValue(ServerRequest request, String key){
        return Integer.parseInt(request.pathVariable(key));
    }

    public Mono<ServerResponse> calcAll(ServerRequest request, String op) {
        int x = getValue(request, "x");
        String y = request.pathVariable("y");
        String [] values = y.split(",");
        String [] ops = op.split(",");


        StringBuilder sb =new StringBuilder();
        for (int i = 0; i < ops.length; i++) {
            int result = x + Integer.parseInt(values[i]);
            sb.append(x).append(" ").append(ops[i]).append(" ").append(values[i]).append(" = ").append(result)
                    .append("\n");
        }

        return ServerResponse.ok().body(Mono.just(sb.toString()), String.class);
    }
}

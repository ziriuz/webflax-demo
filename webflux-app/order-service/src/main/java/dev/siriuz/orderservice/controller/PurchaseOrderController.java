package dev.siriuz.orderservice.controller;

import dev.siriuz.orderservice.dto.PurchaseOrderRequestDto;
import dev.siriuz.orderservice.dto.PurchaseOrderResponseDto;
import dev.siriuz.orderservice.service.OrderFulfillmentService;
import dev.siriuz.orderservice.service.OrderQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
public class PurchaseOrderController {

    @Autowired
    OrderFulfillmentService orderFulfillmentService;
    @Autowired
    OrderQueryService orderQueryService;

    /*@PostMapping
    public Mono<PurchaseOrderResponseDto> createPurchaseOrder(@RequestBody Mono<PurchaseOrderRequestDto> requestDto){
        return orderFulfillmentService.processOrder(requestDto);
    }*/
    @PostMapping
    public Mono<ResponseEntity<PurchaseOrderResponseDto>> createPurchaseOrderWithErrorHandling(
            @RequestBody Mono<PurchaseOrderRequestDto> requestDto){
        return orderFulfillmentService.processOrder(requestDto)
                .map(ResponseEntity::ok)
                .onErrorReturn(WebClientResponseException.class,
                        ResponseEntity.badRequest().build())
                .onErrorReturn(WebClientRequestException.class,
                        ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build())
                ;
    }

    @GetMapping("user/{userId}")
    public Flux<PurchaseOrderResponseDto> getOrdersByUserId (@PathVariable int userId){
        return orderQueryService.getOrdersByUserId(userId);
    }

    @PostMapping("kafka")
    public Mono<ResponseEntity<PurchaseOrderResponseDto>> createPurchaseOrder(
            @RequestBody Mono<PurchaseOrderRequestDto> requestDto) {
        return orderFulfillmentService.processKafkaOrder(requestDto)
                .map(ResponseEntity::ok)
                .onErrorReturn(WebClientResponseException.class,
                        ResponseEntity.badRequest().build())
                .onErrorReturn(WebClientRequestException.class,
                        ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
    }
}

package dev.siriuz.orderservice;

import dev.siriuz.orderservice.client.ProductClient;
import dev.siriuz.orderservice.client.UserClient;
import dev.siriuz.orderservice.dto.ProductDto;
import dev.siriuz.orderservice.dto.PurchaseOrderRequestDto;
import dev.siriuz.orderservice.dto.PurchaseOrderResponseDto;
import dev.siriuz.orderservice.service.OrderFulfillmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderFulfillmentService orderService;

    @Test
    public void CreateOrderTest() throws InterruptedException {

        Flux<PurchaseOrderResponseDto> dtoFlux = userClient.getAllUsers()
                .flatMap(userDto -> productClient.getAllProducts()
                        .map(productDto -> createOrder(userDto.getId(), productDto.getId()))
                )
                .flatMap(orderDto -> orderService.processOrder(Mono.just(orderDto)))
                .doOnNext(System.out::println);

        StepVerifier.create(dtoFlux)
                .expectNextCount(12)
                .verifyComplete();

    }

    @Test
    public void CreateOrderUsingZip(){
        Flux<PurchaseOrderResponseDto> dtoFlux = Flux.zip(userClient.getAllUsers(), productClient.getAllProducts())
                .map(tuple2 -> createOrder(tuple2.getT1().getId(), tuple2.getT2().getId()))
                .flatMap(orderDto -> orderService.processOrder(Mono.just(orderDto)))
                .doOnNext(
                        System.out::println
                );

        StepVerifier.create(dtoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    private PurchaseOrderRequestDto createOrder(int userId, String productId){
        PurchaseOrderRequestDto dto = new PurchaseOrderRequestDto();
        dto.setUserId(userId);
        dto.setProductId(productId);
        return dto;
    }
}

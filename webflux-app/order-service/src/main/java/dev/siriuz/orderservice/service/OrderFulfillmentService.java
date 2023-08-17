package dev.siriuz.orderservice.service;

import dev.siriuz.orderservice.client.ProductClient;
import dev.siriuz.orderservice.client.UserClient;
import dev.siriuz.orderservice.dto.*;
import dev.siriuz.orderservice.entity.PurchaseOrder;
import dev.siriuz.orderservice.repository.PurchaseOrderRepository;
import dev.siriuz.orderservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class OrderFulfillmentService {

    @Autowired
    private PurchaseOrderRepository orderRepository;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    KafkaService kafkaService;

    public Mono<PurchaseOrderResponseDto> processOrder(Mono<PurchaseOrderRequestDto> requestDtoMono) {

        return requestDtoMono.map(RequestContext::new)
                .flatMap(this::addProductToContext)
                .doOnNext(EntityDtoUtil::setTransactionRequestDto)
                .flatMap(this::addTransactionResponseToContext)
                .map(EntityDtoUtil::getPurchaseOrder)
                .map(orderRepository::save) //blocking
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                // !!! since JPA save is used, which is blocking method, it will affect whole flow to be blocking
                // so, it should be executed in dedicated threadpool, not to impact performance
                // it is kind making blocking method to be non-blocking
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<RequestContext> addProductToContext(RequestContext rc){
        return productClient.getProductById(rc.getPurchaseOrderRequestDto().getProductId())
                .doOnNext(rc::setProductDto)
                // Adding resilience to service. Retry 5 times with 1 sec delay
                // when product client is not working properly
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)))
                .thenReturn(rc);
    }

    private Mono<RequestContext> addTransactionResponseToContext(RequestContext rc){
        return userClient.authorizeTransaction(rc.getTransactionRequestDto())
                .doOnNext(rc::setTransactionResponseDto)
                .thenReturn(rc);
    }

    public Mono<PurchaseOrderResponseDto> processKafkaOrder(Mono<PurchaseOrderRequestDto> requestDtoMono) {

        return requestDtoMono.map(RequestContext::new)
                .doOnNext(rc -> kafkaService.sendProductRequest(rc.getPurchaseOrderRequestDto().getProductId()))
                .flatMap(this::addProductToContext)
                .doOnNext(EntityDtoUtil::setTransactionRequestDto)
                .flatMap(this::addTransactionResponseToContext)
                .map(EntityDtoUtil::getPurchaseOrder)
                .map( order -> {
                    System.out.println("before save order id: " + order.getId());
                    PurchaseOrder savedOrder = orderRepository.save(order);
                    System.out.println("after save order id: " + savedOrder.getId());
                    kafkaService.publishNewOrder(savedOrder);
                    return savedOrder;
                }) //blocking
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public PurchaseOrderResponseDto processOrder(PurchaseOrderRequestDto requestDto) {

        RequestContext context = new RequestContext(requestDto);
        addProductToContext(context).subscribe(); // call Product API

        EntityDtoUtil.setTransactionRequestDto(context); // build request for User API
        addTransactionResponseToContext(context); // call User API to authorise transaction

        PurchaseOrder order = EntityDtoUtil.getPurchaseOrder(context); // Transform Context to order entity

        order = orderRepository.save(order); // save order to DB

        PurchaseOrderResponseDto purchaseOrderResponseDto = EntityDtoUtil.getPurchaseOrderResponseDto(order); // build Response

        return purchaseOrderResponseDto;
    }


}

package dev.siriuz.orderservice.service;

import dev.siriuz.orderservice.dto.PurchaseOrderResponseDto;
import dev.siriuz.orderservice.entity.PurchaseOrder;
import dev.siriuz.orderservice.repository.PurchaseOrderRepository;
import dev.siriuz.orderservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class OrderQueryService {

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    public Flux<PurchaseOrderResponseDto> getOrdersByUserId(int userId){

        // --- this is blocking operation because we are using JPA ---
        // List<PurchaseOrder> orders = purchaseOrderRepository.findByUserId(userId);
        // return Flux.fromIterable(orders)
        //        .map(EntityDtoUtil::getPurchaseOrderResponseDto);

        // Correct way is to do lazily in reactive way
        return Flux.fromStream(() -> purchaseOrderRepository.findByUserId(userId).stream())
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic()); // we need it because JPA call is blocking

    }
}

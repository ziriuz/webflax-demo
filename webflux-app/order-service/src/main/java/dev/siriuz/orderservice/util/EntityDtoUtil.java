package dev.siriuz.orderservice.util;

import dev.siriuz.orderservice.dto.*;
import dev.siriuz.orderservice.entity.PurchaseOrder;
import dev.siriuz.orderservice.repository.PurchaseOrderRepository;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {


    public static PurchaseOrderResponseDto getPurchaseOrderResponseDto(PurchaseOrder order){
        PurchaseOrderResponseDto dto = new PurchaseOrderResponseDto();
        BeanUtils.copyProperties(order, dto);
        dto.setOrderId(order.getId());
        /*dto.setProductId(order.getProductId());
        dto.setUserId(order.getUserId());
        dto.setAmount(order.getAmount());
        dto.setStatus(order.getStatus());*/
        return dto;
    }
    public static void setTransactionRequestDto(RequestContext requestContext){
        TransactionRequestDto dto = new TransactionRequestDto();
        dto.setUserId(requestContext.getPurchaseOrderRequestDto().getUserId());
        dto.setAmount((int) Math.round(
                requestContext.getProductDto().getPrice()
        ));
        requestContext.setTransactionRequestDto(dto);

    }

    public static PurchaseOrder getPurchaseOrder(RequestContext requestContext){
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setProductId(requestContext.getPurchaseOrderRequestDto().getProductId());
        purchaseOrder.setUserId(requestContext.getPurchaseOrderRequestDto().getUserId());
        purchaseOrder.setAmount((int) Math.round(
                requestContext.getProductDto().getPrice()
        ));
        OrderStatus status =
                TransactionStatus.APPROVED.equals(
                    requestContext.getTransactionResponseDto().getStatus()
                ) ? OrderStatus.COMPLETED : OrderStatus.FAILED;

        purchaseOrder.setStatus(status);

        return purchaseOrder;
    }
}

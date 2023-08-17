package dev.siriuz.orderservice.service;

import dev.siriuz.orderservice.dto.OrderStatus;
import dev.siriuz.orderservice.entity.PurchaseOrder;
import org.springframework.stereotype.Service;

@Service
public class OrderManagementService {

    public PurchaseOrder confirm(PurchaseOrder orderPayment, PurchaseOrder orderStock) {
        OrderStatus status;

        if (orderPayment.getStatus().equals(OrderStatus.ACCEPT) &&
                orderStock.getStatus().equals(OrderStatus.ACCEPT)) {
            status = OrderStatus.CONFIRMED;
        } else if (orderPayment.getStatus().equals(OrderStatus.REJECT) &&
                orderStock.getStatus().equals(OrderStatus.REJECT)) {
            status = OrderStatus.REJECTED;
        } else {
            String source = orderPayment.getStatus().equals(OrderStatus.REJECT)
                    ? "PAYMENT" : "STOCK";
            status = OrderStatus.ROLLBACK;
        }

        return new PurchaseOrder(
                orderPayment.getId(),
                orderPayment.getUserId(),
                orderPayment.getProductId(),
                orderPayment.getAmount(),
                status);
    }
}

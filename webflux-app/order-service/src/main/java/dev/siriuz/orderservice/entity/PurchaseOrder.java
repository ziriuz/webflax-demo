package dev.siriuz.orderservice.entity;

import dev.siriuz.orderservice.dto.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@ToString
@NoArgsConstructor
@Entity
public class PurchaseOrder {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer userId;
    private String productId;
    private Integer amount;
    private OrderStatus status;

    public PurchaseOrder(Integer id, Integer userId, String productId, Integer amount, OrderStatus status) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.amount = amount;
        this.status = status;
    }
}

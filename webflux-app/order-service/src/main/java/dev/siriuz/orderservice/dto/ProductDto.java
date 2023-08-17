package dev.siriuz.orderservice.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductDto {
    private String id;
    private String name;
    private double price;
}

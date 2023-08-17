package dev.siriuz.webfluxservice.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProductDto {
    private String id;
    private String name;
    private double price;
}

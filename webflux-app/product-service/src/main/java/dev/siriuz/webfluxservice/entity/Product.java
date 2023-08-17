package dev.siriuz.webfluxservice.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
public class Product {
    @Id
    private String id;
    private String name;
    private double price;
}

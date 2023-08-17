package dev.siriuz.webfluxservice.controller;

import dev.siriuz.webfluxservice.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("product")
public class ProductStreamController {

    @Autowired
    Flux<ProductDto> streamProducts;

    @GetMapping(value = "stream/{price}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> streamNewProduct(@PathVariable int price){
        return streamProducts
                .filter(productDto -> productDto.getPrice() < price);
    }

}

package dev.siriuz.webfluxservice.controller;


import dev.siriuz.webfluxservice.dto.ProductDto;
import dev.siriuz.webfluxservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("all")
    public Flux<ProductDto> all(){
        return service.getAll();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<ProductDto>> getProductById(@PathVariable String id) {
        return service.getProductById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ProductDto> createProduct(@RequestBody Mono<ProductDto> productDtoMono){
        return service.insertProduct(productDtoMono);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<ProductDto>> updateProduct(
            @PathVariable String id,
            @RequestBody Mono<ProductDto> productDtoMono){
        return service.updateProduct(id, productDtoMono)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteProduct(@PathVariable String id){
        return service.deleteProduct(id);
    }

    @GetMapping("price-range")
    public Flux<ProductDto> getByPriceRangeMongo(
            @RequestParam("min") int minPrice,
            @RequestParam("max") int maxPrice){
        return service.getProductByPriceRange(minPrice, maxPrice);
    }

    private void throwRandomException(){
        int i = (int) (Math.random() * 10);
        if (i>5)
            throw new RuntimeException("Random exception simulation");
    }


}

package dev.siriuz.webfluxservice.service;

import dev.siriuz.webfluxservice.dto.ProductDto;
import dev.siriuz.webfluxservice.entity.Product;
import dev.siriuz.webfluxservice.repository.ProductRepository;
import dev.siriuz.webfluxservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private Sinks.Many<ProductDto> sink;

    public Flux<ProductDto> getAll(){
        return repository.findAll()
                .map(EntityDtoUtil::toDto);
    }
    public Flux<ProductDto> getProductByPriceRange(int minPrice, int maxPrice){
        return repository.findByPriceBetween(Range.closed(minPrice, maxPrice))
                .map(EntityDtoUtil::toDto);
    }
    public Mono<ProductDto> getProductById(String id){
        return repository.findById(id)
                .map(EntityDtoUtil::toDto);
    }

    public Mono<ProductDto> insertProduct(Mono<ProductDto> productDtoMono){
        return productDtoMono
                .map(EntityDtoUtil::toEntity)
                .flatMap(product -> repository.insert(product))
                .map(EntityDtoUtil::toDto)
                .doOnNext(
                        productDto -> sink.tryEmitNext(productDto)
                );
    }

    public Mono<ProductDto> updateProduct(String id, Mono<ProductDto> productDtoMono){
        return repository.findById(id)
                .flatMap(foundProduct ->
                           productDtoMono.map( dto -> {
                                Product product = EntityDtoUtil.toEntity(dto);
                                product.setId(foundProduct.getId());
                                return product;
                            })
                )
                .flatMap( product -> repository.save(product))
                .map(EntityDtoUtil::toDto);
    }

    public Mono<Void> deleteProduct(String id){
        return repository.deleteById(id);
    }
}

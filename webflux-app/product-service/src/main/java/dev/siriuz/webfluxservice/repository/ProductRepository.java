package dev.siriuz.webfluxservice.repository;

import dev.siriuz.webfluxservice.entity.Product;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    // this way min and max are excluded from range
    // Flux<Product> findByPriceBetween(double minPrice, double maxPrice);

    Flux<Product> findByPriceBetween(Range<Integer> range);
}

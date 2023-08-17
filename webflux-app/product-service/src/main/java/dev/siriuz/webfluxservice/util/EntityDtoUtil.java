package dev.siriuz.webfluxservice.util;

import dev.siriuz.webfluxservice.dto.ProductDto;
import dev.siriuz.webfluxservice.entity.Product;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {

    public static ProductDto toDto(Product product){
        ProductDto dto = new ProductDto();
        // there are more efficient utils
        BeanUtils.copyProperties(product, dto);
        return dto;
    }

    public static Product toEntity(ProductDto dto){
        Product product = new Product();
        // there are more efficient utils
        BeanUtils.copyProperties(dto, product);
        return product;
    }
}

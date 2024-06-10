package com.example.barter.service;

import com.example.barter.dto.ProductDto;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductService {

    public Flux<ProductDto> getAllProductsByPageable(Pageable pageable);


    public Mono<ProductDto> getProductById(UUID id);
}

package com.example.barter.service;

import com.example.barter.dto.input.SaveProductInput;
import com.example.barter.dto.response.ProductDto;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductService {

     Mono<Void> save(SaveProductInput saveProductInput);

     Flux<ProductDto> getAllByPageable(Pageable pageable);

     Flux<ProductDto> getByUserId(UUID userId);

     Mono<ProductDto> getById(String id);

     Mono<Void> deleteById(String id);



}


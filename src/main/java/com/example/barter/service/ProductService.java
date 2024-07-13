package com.example.barter.service;

import com.example.barter.dto.input.SaveProductInput;
import com.example.barter.dto.response.ProductResponse;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductService {

     Mono<Void> save(SaveProductInput saveProductInput);

     Flux<ProductResponse> getAllByPageable(Pageable pageable);

     Flux<ProductResponse> getByFilter(String subject);

     Flux<ProductResponse> getByQuery(String q);

     Flux<ProductResponse> getByUserId(UUID userId);


     Mono<ProductResponse> getById(String id);

     Mono<Void> deleteById(String id);

     Mono<ProductResponse> findByIsbn(SaveProductInput saveProductInput);


}





package com.example.barter.service;

import com.example.barter.dto.input.SaveProductInput;
import com.example.barter.dto.response.ProductResponse;
import com.example.barter.dto.response.UserProductResponse;
import com.example.barter.exception.customexception.ImageUploadFailed;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductService {

     Mono<Void> save(SaveProductInput saveProductInput, MultipartFile file) throws ImageUploadFailed;

     Flux<UserProductResponse> getAllByPageable(Pageable pageable);

     Flux<UserProductResponse> getByFilter(String subject);

     Flux<UserProductResponse> getByQuery(String q);

     Flux<UserProductResponse> getByUserId(String userId);

     Mono<List<ProductResponse>> getSearchResults(String q);

     Mono<UserProductResponse> getById(String id);

     Mono<Void> deleteById(String id);

//     Mono<UserProductResponse> findByIsbn(SaveProductInput saveProductInput);


}





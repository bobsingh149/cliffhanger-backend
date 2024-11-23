package com.example.barter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.barter.dto.input.CommentInput;
import com.example.barter.dto.input.LikeInput;
import com.example.barter.dto.input.SaveProductInput;
import com.example.barter.dto.models.FilterType;
import com.example.barter.dto.response.PostResponse;
import com.example.barter.dto.response.ProductResponse;
import com.example.barter.exception.customexception.ImageUploadFailed;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Mono<Void> save(SaveProductInput saveProductInput, MultipartFile file) throws ImageUploadFailed;

    Flux<PostResponse> getAllByPageable(String userId, Pageable pageable, FilterType filterType);

    Flux<PostResponse> getByUserId(String userId);

    Flux<ProductResponse> getSearchResults(String q);

    Mono<PostResponse> getById(String id);

    Mono<Void> deleteById(String id);

    Mono<Void> saveComment(CommentInput commentInput);

    Mono<Void> saveLike(LikeInput likeInput);

    Flux<PostResponse> getByBarterFilter(String city, String userId, Pageable pageable);

    Flux<PostResponse> getBySearch(String q, String userId, Pageable pageable);

    Mono<Void> incrementCommentLikeCount(String productId, String commentId);

}





package com.example.barter.service;

import com.example.barter.dto.model.CommentsWithUserBasicInfo;
import com.example.barter.dto.response.CommentResponse;
import com.example.barter.dto.response.UserProfileResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.barter.dto.input.CommentInput;
import com.example.barter.dto.input.LikeInput;
import com.example.barter.dto.input.SaveProductInput;
import com.example.barter.dto.model.FilterType;
import com.example.barter.dto.response.PostResponse;
import com.example.barter.dto.response.ProductResponse;
import com.example.barter.exception.customexception.ImageUploadFailed;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    Mono<Void> save(SaveProductInput saveProductInput, MultipartFile file) throws ImageUploadFailed;

    Flux<PostResponse> getAllByPageable(String userId, Pageable pageable);

    Mono<UserProfileResponse> getUserProfile(String userId);

    Flux<PostResponse> getByUserId(String userId);

    Mono<List<ProductResponse>> getSearchResults(String q);

    Mono<PostResponse> getById(UUID id);

    Mono<Void> deleteById(UUID id);

    Mono<Void> saveComment(CommentInput commentInput);

    Mono<Void> saveLike(LikeInput likeInput);

    Flux<PostResponse> getByBarterFilter(String userId, Pageable pageable);

    Flux<PostResponse> getBySearch(String q, Pageable pageable);

    Mono<Void> incrementCommentLikeCount(UUID productId, String commentId);

    Mono<List<CommentResponse>> getComments(UUID productId);

}





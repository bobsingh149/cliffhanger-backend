package com.example.barter.service;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.barter.config.OpenLibBooksApiProperties;
import com.example.barter.dto.entity.ProductEntity;
import com.example.barter.dto.input.CommentInput;
import com.example.barter.dto.input.LikeInput;
import com.example.barter.dto.input.SaveProductInput;
import com.example.barter.dto.model.FilterType;
import com.example.barter.dto.response.PostResponse;
import com.example.barter.dto.response.ProductResponse;
import com.example.barter.exception.customexception.ImageUploadFailed;
import com.example.barter.repository.ProductRepository;
import com.example.barter.utils.CloudinaryUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Profile("bookprofile")
public class BookService implements ProductService {

    private final ProductRepository productRepository;
    private final OpenLibBooksApiProperties booksApiProperties;
    private final WebClient webClient;
    private final CloudinaryUtils cloudinaryUtils;


    @Autowired
    public BookService(ProductRepository productRepository, OpenLibBooksApiProperties booksApiProperties, WebClient webClient, CloudinaryUtils cloudinaryUtils) {

        this.productRepository = productRepository;
        this.booksApiProperties = booksApiProperties;
        this.webClient = webClient;
        this.cloudinaryUtils = cloudinaryUtils;
    }


    @Override
    public Flux<PostResponse> getAllByPageable(String userId, Pageable pageable, FilterType filterType) {
        try {
            return productRepository.getAllByPageable(userId, pageable.getOffset(), pageable.getPageSize()).map(PostResponse::fromProductEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public Flux<PostResponse> getByUserId(String userId) {
        return productRepository.getByUserId(userId).map(PostResponse::fromProductEntity);
    }

    @Override
    public Mono<PostResponse> getById(String id) {
        return productRepository.findById(id).map(PostResponse::fromProductEntity);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return productRepository.deleteById(id);
    }


    public String getId(String str) {
        int idx = str.lastIndexOf('/');
        return str.substring(idx + 1);

    }


    @Override
    public Mono<Void> save(SaveProductInput saveProductInput, MultipartFile file) throws ImageUploadFailed {

        try {

            final ProductEntity productEntity = ProductEntity.fromProductInput(saveProductInput, file, cloudinaryUtils);
            return productRepository.save(productEntity.getUserId(), productEntity.getTitle(), productEntity.getAuthors(), productEntity.getCoverImages(), productEntity.getSubjects(), productEntity.getScore(), productEntity.getCaption(), productEntity.getPostImage(), productEntity.getCategory().name());
        } catch (Exception e) {
            throw new ImageUploadFailed(e.getMessage());
        }
    }

    private String extractIdFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String query = uri.getQuery();
            if (query != null) {
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    String[] keyValue = pair.split("=");
                    if (keyValue.length == 2 && "id".equals(keyValue[0])) {
                        return keyValue[1];
                    }
                }
            }
        } catch (Exception e) {
            return UUID.randomUUID().toString();
        }
        return UUID.randomUUID().toString();

    }

    public ProductResponse uploadAndGetUrl(ProductResponse productResponse) {

        if (productResponse.getCoverImages().isEmpty()) {
            return null;
        }

        String imageLink = productResponse.getCoverImages().get(1);

        try {
            final String url = cloudinaryUtils.uploadFromNetworkAndGetLink(imageLink, "cover_images");

            productResponse.setCoverImages(List.of(url));

            return productResponse;
        } catch (IOException e) {
            return null;
        }

    }

    @Override
    public Flux<ProductResponse> getSearchResults(String q) {

        String uri = UriComponentsBuilder.fromUriString(booksApiProperties.getUrl()).queryParam("q", q).queryParam("limit", booksApiProperties.getQuery().getLimit()).queryParam("fields", booksApiProperties.getQuery().getFields()).build().toUriString();

        final var booksApiResponse = webClient.get().uri(uri).retrieve().bodyToMono(String.class);

        final var productResponseMono = booksApiResponse.map(ProductResponse::fromJson);

        return productResponseMono
        .flux()
        .flatMap(Flux::fromIterable)
        .parallel()
        .runOn(Schedulers.boundedElastic())
        .map(this::uploadAndGetUrl)
        .sequential();
    }

    @Override
    public Mono<Void> saveComment(CommentInput commentInput) {
        return productRepository.saveComment(commentInput.productId(), commentInput.comment());
    }

    @Override
    public Mono<Void> saveLike(LikeInput likeInput) {
        return productRepository.likePost(likeInput.productId(), likeInput.userId());
    }

    @Override
    public Flux<PostResponse> getByBarterFilter(String city, String userId, Pageable pageable) {
        return productRepository.getByBarterFilter(city, userId, pageable.getOffset(), pageable.getPageSize()).map(PostResponse::fromProductEntity);
    }

    @Override
    public Flux<PostResponse> getBySearch(String q, String userId, Pageable pageable) {
        return productRepository.getBySearch(q, userId, pageable.getOffset(), pageable.getPageSize())
                .map(PostResponse::fromProductEntity);
    }

    @Override
    public Mono<Void> incrementCommentLikeCount(String productId, String commentId) {
        return productRepository.incrementCommentLikeCount(productId, commentId);
    }

}

package com.example.barter.service;

import com.example.barter.config.OpenLibBooksApiProperties;
import com.example.barter.dto.entity.ProductEntity;
import com.example.barter.dto.input.CommentInput;
import com.example.barter.dto.input.LikeInput;
import com.example.barter.dto.input.SaveProductInput;
import com.example.barter.dto.model.CommentModel;
import com.example.barter.dto.model.CommentsWithUserBasicInfo;
import com.example.barter.dto.model.FilterType;
import com.example.barter.dto.response.CommentResponse;
import com.example.barter.dto.response.PostResponse;
import com.example.barter.dto.response.ProductResponse;
import com.example.barter.dto.response.UserProfileResponse;
import com.example.barter.exception.customexception.ImageUploadFailed;
import com.example.barter.repository.ProductRepository;
import com.example.barter.utils.CloudinaryUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Profile("bookprofile")
public class BookService implements ProductService {

    private final ProductRepository productRepository;
    private final OpenLibBooksApiProperties booksApiProperties;
    private final WebClient webClient;
    private final CloudinaryUtils cloudinaryUtils;
    private final UserService userService;


    @Autowired
    public BookService(ProductRepository productRepository, OpenLibBooksApiProperties booksApiProperties, WebClient webClient, CloudinaryUtils cloudinaryUtils, UserService userService) {

        this.productRepository = productRepository;
        this.booksApiProperties = booksApiProperties;
        this.webClient = webClient;
        this.cloudinaryUtils = cloudinaryUtils;
        this.userService = userService;
    }


    @Override
    public Flux<PostResponse> getAllByPageable(String userId, Pageable pageable) {
        try {
            return productRepository.getAllByPageable(userId, pageable.getOffset(), pageable.getPageSize())
                .flatMap(productEntity -> userService.getUser(productEntity.getUserId())
                    .map(userResponse -> PostResponse.fromProductEntity(productEntity, userResponse)));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Mono<UserProfileResponse> getUserProfile(String userId) {
        return userService.getUser(userId).flatMap(userResponse -> getByUserId(userId).collectList().map(posts -> UserProfileResponse.getInstance(userResponse, posts)));

    }

    @Override
    public Flux<PostResponse> getByUserId(String userId) {
        return productRepository.getByUserId(userId)
            .flatMap(productEntity -> userService.getUser(productEntity.getUserId())
                .map(userResponse -> PostResponse.fromProductEntity(productEntity, userResponse)));
    }

    @Override
    public Mono<PostResponse> getById(UUID id) {
        return productRepository.getById(id)
                .flatMap(productEntity -> userService.getUser(productEntity.getUserId())
                        .map(userResponse -> PostResponse.fromProductEntity(productEntity, userResponse))

        );


    }

    @Override
    public Mono<Void> deleteById(UUID id) {
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

            if(productEntity.getCoverImages().length>=3) {
                final String uploadedCoverImage = cloudinaryUtils.uploadFromNetworkAndGetLink(productEntity.getCoverImages()[2], "cover_images");
                productEntity.setCoverImage(uploadedCoverImage);
            }

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
    public Mono<List<ProductResponse>> getSearchResults(String q) {

        String uri = UriComponentsBuilder.fromUriString(booksApiProperties.getUrl()).queryParam("q", q).queryParam("limit", booksApiProperties.getQuery().getLimit()).queryParam("fields", booksApiProperties.getQuery().getFields()).build().toUriString();

        final var booksApiResponse = webClient.get().uri(uri).retrieve().bodyToMono(String.class);

        return booksApiResponse.map(ProductResponse::fromJson);

    }


    public Flux<ProductResponse> getSearchResultsParallel(String q) {

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
        CommentModel commentModel = CommentModel.builder().id(UUID.randomUUID().toString()).text(commentInput.text()).likeCount(0).timestamp(LocalDateTime.now()).userId(commentInput.userId()).build();
        return productRepository.saveComment(commentInput.productId(), commentModel, commentModel.getUserId());
    }

    @Override
    public Mono<Void> saveLike(LikeInput likeInput) {
        return productRepository.likePost(likeInput.productId(), likeInput.userId());
    }

    @Override
    public Flux<PostResponse> getByBarterFilter(String userId, Pageable pageable) {
        return productRepository.getByBarterFilter(userId, pageable.getOffset(), pageable.getPageSize())
            .flatMap(productEntity -> userService.getUser(productEntity.getUserId())
                .map(userResponse -> PostResponse.fromProductEntity(productEntity, userResponse)));
    }

    @Override
    public Flux<PostResponse> getBySearch(String q, Pageable pageable) {
        return productRepository.getBySearch(q, pageable.getOffset(), pageable.getPageSize())
            .flatMap(productEntity -> userService.getUser(productEntity.getUserId())
                .map(userResponse -> PostResponse.fromProductEntity(productEntity, userResponse)));
    }

    @Override
    public Mono<Void> incrementCommentLikeCount(UUID productId, String commentId) {
        return productRepository.incrementCommentLikeCount(productId, commentId);
    }


    @Override
   public Mono<List<CommentResponse>> getComments(UUID productId)
    {
        return productRepository
                .getCommentsByProductId(productId)
                .map(response-> {
                    Map<String, CommentsWithUserBasicInfo.UserBasicInfo> basicInfoMap = new HashMap<>();

                    response.getUserBasicInfos().forEach(userBasicInfo -> basicInfoMap.put(userBasicInfo.getId(), userBasicInfo));

                  return  response.getComments().stream()
                            .map(commentModel -> CommentResponse.fromCommentModel(commentModel,basicInfoMap))
                            .toList();


                });
    }


}

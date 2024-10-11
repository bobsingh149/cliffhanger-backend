package com.example.barter.service;

import com.example.barter.config.BooksApiProperties;
import com.example.barter.dto.entity.ProductEntity;
import com.example.barter.dto.input.SaveProductInput;
import com.example.barter.dto.response.ProductResponse;
import com.example.barter.dto.response.UserProductResponse;
import com.example.barter.exception.customexception.ImageUploadFailed;
import com.example.barter.repository.ProductRepository;
import com.example.barter.utils.CloudinaryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@Profile("bookprofile")
public class BookService implements ProductService {

    private final ProductRepository productRepository;
    private final BooksApiProperties booksApiProperties;
 private final WebClient webClient;
    private final CloudinaryUtils cloudinaryUtils;


    @Autowired
    public BookService(ProductRepository productRepository, BooksApiProperties booksApiProperties,WebClient webClient, CloudinaryUtils cloudinaryUtils) {

        this.productRepository = productRepository;
        this.booksApiProperties = booksApiProperties;
        this.webClient = webClient;
        this.cloudinaryUtils = cloudinaryUtils;
    }


    public Flux<UserProductResponse> getAllByPageable(Pageable pageable) {
        try {

            return productRepository.getAllByPageable(pageable.getOffset(), pageable.getPageSize()).map(UserProductResponse::fromProductEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public Flux<UserProductResponse> getByUserId(String userId) {
        return productRepository.getByUserId(userId).map(UserProductResponse::fromProductEntity);
    }

    public Mono<UserProductResponse> getById(String id) {
        return productRepository.findById(id).map(UserProductResponse::fromProductEntity);
    }

    public Mono<Void> deleteById(String id) {
        return productRepository.deleteById(id);
    }


    public String getIsbnUri(long isbn) {


        return UriComponentsBuilder.fromUriString(booksApiProperties.getIsbnEndpoint())

                .queryParam("bibkeys", "ISBN:" + isbn).queryParam("jscmd", booksApiProperties.getQuery().getJscmd())

                .queryParam("format", booksApiProperties.getQuery().getFormat()).build().encode().toUriString();
    }

    public String getId(String str) {
        int idx = str.lastIndexOf('/');
        return str.substring(idx + 1);

    }


    int getRanking(Map<String, Integer> counts) {
        return counts.get("already_read") * 35 + counts.get("currently_reading") * 35 + counts.get("already_read") * 30;
    }

//    public Mono<Void> setRankingAndSave(ProductEntity productEntity) {
//
//        final String workId = getId(productEntity.getWorks().get(0).get("key"));
//
//
//        String bookshelfUri = UriComponentsBuilder.fromUriString(booksApiProperties.getBookshelfEndpoint()).buildAndExpand(workId).toUriString();
//
//        return webClient.get().uri(bookshelfUri).retrieve().bodyToMono(BookshelfResponse.class).flatMap(bookshelfResponse -> {
//            productEntity.setScore(getRanking(bookshelfResponse.getCounts()));
//            return productRepository.save(productEntity.getId(), productEntity.getIsbn(), productEntity.getUserId(), productEntity.getWorks(), productEntity.getTitle(), productEntity.getAuthors(), productEntity.getDescription(), productEntity.getCoverImages(), productEntity.getSubjects(), productEntity.getScore(), productEntity.getUserImagesWrapper());
//        });
//
//    }


    public Mono<Void> save(SaveProductInput saveProductInput, MultipartFile file) throws ImageUploadFailed {

        try {

            final ProductEntity productEntity = ProductEntity.fromProductInput(saveProductInput, file, cloudinaryUtils);
            return productRepository.save(productEntity.getUserId(), productEntity.getTitle(), productEntity.getAuthors(), productEntity.getCoverImages(), productEntity.getSubjects(), productEntity.getScore(), productEntity.getCaption(), productEntity.getPostImage(), productEntity.getCategory().name());
        }
        catch (Exception e)
        {
            throw new ImageUploadFailed(e.getMessage());
        }
    }

//    public Mono<UserProductResponse> findByIsbn(SaveProductInput saveProductInput) {
//
//        String isbnUri = getIsbnUri(saveProductInput.isbn());
//
//        final var booksApiResponse = webClient.get().uri(isbnUri).retrieve().bodyToMono(String.class);
//
//        return booksApiResponse.map(jsonStr -> ProductEntity.fromProductInput(jsonStr, saveProductInput, cloudinaryUtils)).map(UserProductResponse::fromProductEntity);
//
//    }




    public Mono<List<ProductResponse>> getSearchResults(String q) {


        String uri = UriComponentsBuilder.fromUriString(booksApiProperties.getUrl())
                .queryParam("q", q)
                .queryParam("limit", booksApiProperties.getQuery().getLimit())
                .queryParam("fields",booksApiProperties.getQuery().getFields())
                .build().toUriString();

        final var booksApiResponse = webClient.get().uri(uri)
                .retrieve().bodyToMono(String.class);

        return booksApiResponse.map(ProductResponse::fromJson);

    }

    public Flux<UserProductResponse> getByQuery(String q) {
        q = q + "%";
        return productRepository.getByQuery(q).map(UserProductResponse::fromProductEntity);
    }


    public Flux<UserProductResponse> getByFilter(String subject) {

        return productRepository.getByFilter(subject).map(UserProductResponse::fromProductEntity);
    }


}

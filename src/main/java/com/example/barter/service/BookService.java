package com.example.barter.service;

import com.example.barter.config.BooksApiProperties;
import com.example.barter.dto.api.BookshelfResponse;
import com.example.barter.dto.input.SaveProductInput;
import com.example.barter.dto.response.ProductResponse;
import com.example.barter.dto.entity.ProductEntity;
import com.example.barter.repository.ProductRepository;
import com.example.barter.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;
import java.util.UUID;

@Service
@Profile("bookprofile")
public class BookService implements ProductService {

    private final ProductRepository productRepository;
    private final BooksApiProperties booksApiProperties;
    private final WebClient webClient;


    @Autowired
    public BookService(ProductRepository productRepository, BooksApiProperties booksApiProperties, WebClient webClient) {

        this.productRepository = productRepository;
        this.booksApiProperties = booksApiProperties;
        this.webClient = webClient;
    }


    public Flux<ProductResponse> getAllByPageable(Pageable pageable) {
        try {

            return productRepository.getAllByPageable(pageable.getOffset(), pageable.getPageSize()).map(ProductResponse::fromProductEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public Flux<ProductResponse> getByUserId(UUID userId) {
        return productRepository.getByUserId(userId).map(ProductResponse::fromProductEntity);
    }

    public Mono<ProductResponse> getById(String id) {
        return productRepository.findById(id).map(ProductResponse::fromProductEntity);
    }

    public Mono<Void> deleteById(String id) {
        return productRepository.deleteById(id);
    }


    public String getIsbnUri(SaveProductInput saveProductInput) {
        return UriComponentsBuilder.
                fromUriString(booksApiProperties.getIsbnEndpoint())

                .queryParam("bibkeys", "ISBN:" + saveProductInput.getIsbn())
                .queryParam("jscmd", booksApiProperties.getQuery().getJscmd())

                .queryParam("format", booksApiProperties.getQuery().getFormat())
                .build()
                .encode()
                .toUriString();
    }

    public String getId(String str) {
        int idx = str.lastIndexOf('/');
        return str.substring(idx + 1);

    }


    int getRanking(Map<String, Integer> counts) {
        return counts.get("already_read") * 35 +
                counts.get("currently_reading") * 35 +
                counts.get("already_read") * 30;
    }

    public Mono<Void> setRankingAndSave(ProductEntity productEntity) {

       final String workId = getId(productEntity.getWorks().get(0).get("key"));


        String bookshelfUri = UriComponentsBuilder.
                fromUriString(booksApiProperties.getBookshelfEndpoint())
                .buildAndExpand(workId)
                .toUriString();


        return webClient.get()
                .uri(bookshelfUri)
                .retrieve().
                bodyToMono(BookshelfResponse.class)
                .flatMap(
                        bookshelfResponse -> {
                            productEntity.setScore(getRanking(bookshelfResponse.getCounts()));

                            return productRepository.save(productEntity.getId(), productEntity.getIsbn(), productEntity.getUserId(), CommonUtils.convertToJson(productEntity.getWorks()),productEntity.getTitle(), CommonUtils.convertToJson(productEntity.getAuthors()), productEntity.getDescription(), productEntity.getImage(), CommonUtils.convertToJson(productEntity.getSubjects()), productEntity.getScore());

                                }
                );

    }


    public Mono<Void> save(SaveProductInput saveProductInput) {

        String isbnUri = getIsbnUri(saveProductInput);


        final var booksApiResponse = webClient.get().
                uri(isbnUri).
                retrieve().bodyToMono(String.class);


        return booksApiResponse
                .map(jsonStr -> ProductEntity.fromJson(jsonStr, saveProductInput))
                .flatMap(this::setRankingAndSave);

    }


}

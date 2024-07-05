//package com.example.barter.dto.utils.service;
//
//import com.example.barter.dto.api.BookshelfResponse;
//import com.example.barter.dto.entity.ProductEntity;
//import com.example.barter.dto.input.SaveProductInput;
//import org.springframework.web.util.UriComponentsBuilder;
//import reactor.core.publisher.Mono;
//
//import java.util.Map;
//
//public class ProductServiceUtils {
//
//
//
//    public String getIsbnUri(SaveProductInput saveProductInput) {
//        return UriComponentsBuilder.
//                fromUriString(booksApiProperties.getIsbnEndpoint())
//                .queryParam("bibkeys", saveProductInput.getIsbn())
//                .queryParam("jscmd", booksApiProperties.getQuery().getJscmd())
//                .queryParam("format", booksApiProperties.getQuery().getFormat())
//                .toUriString();
//    }
//
//    public String getId(String str) {
//        int idx = str.lastIndexOf('/');
//        return str.substring(idx + 1);
//
//    }
//
//
//    int getRanking(Map<String, Integer> counts) {
//        return counts.get("already_read") * 35 +
//                counts.get("currently_reading") * 35 +
//                counts.get("already_read") * 30;
//    }
//
//    public Mono<ProductEntity> setRankingAndSave(ProductEntity productEntity) {
//        String workId = getId(productEntity.getWorks().get(0).get("key"));
//
//
//        String bookshelfUri = UriComponentsBuilder.
//                fromPath(booksApiProperties.getBookshelfEndpoint())
//                .buildAndExpand(workId)
//                .toUriString();
//
//        return webClient.get()
//                .uri(bookshelfUri)
//                .retrieve().
//                bodyToMono(BookshelfResponse.class)
//                .flatMap(
//                        bookshelfResponse -> {
//                            productEntity.setRanking(getRanking(bookshelfResponse.getCounts()));
//
//                            return productRepository.save(productEntity);
//                        }
//                );
//
//
//    }
//
//
//}

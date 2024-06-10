package com.example.barter.service;

import com.example.barter.dto.ProductDto;
import com.example.barter.entity.ProductEntity;
import com.example.barter.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Flux<ProductDto> getAllProductsByPageable(Pageable pageable) {
        try {
            return productRepository.getAllByPageable(pageable.getOffset(), pageable.getPageSize()).map(productEntity -> ProductDto.builder().id(productEntity.getId()).name(productEntity.getName()).description(productEntity.getDescription()).image(productEntity.getImage()).category(productEntity.getCategory()).ranking(productEntity.getRanking()).build());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Mono<ProductDto> getProductById(UUID id) {
        return productRepository.findById(id).map(productEntity -> ProductDto.builder().id(productEntity.getId()).name(productEntity.getName()).description(productEntity.getDescription()).image(productEntity.getImage()).category(productEntity.getCategory()).ranking(productEntity.getRanking()).build());
    }

    public Mono<Void> deleteProductById(UUID id) {
        return productRepository.deleteById(id);
    }

    public void saveProduct(ProductDto productDto) {
        productRepository.save(ProductEntity.builder().name(productDto.getName()).image(productDto.getImage()).ranking(productDto.ranking()).category(productDto.getCategory()).description(productDto.description()).build());
    }


}

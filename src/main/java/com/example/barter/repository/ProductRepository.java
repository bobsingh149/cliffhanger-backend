package com.example.barter.repository;

import com.example.barter.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface ProductRepository extends R2dbcRepository<ProductEntity, UUID> {

        @Query("SELECT * FROM product ORDER BY ranking OFFSET = :offset LIMIT = :limit")
        public Flux<ProductEntity> getAllByPageable(long offset, int limit);
}

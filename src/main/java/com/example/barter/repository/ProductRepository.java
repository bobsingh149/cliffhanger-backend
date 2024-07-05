package com.example.barter.repository;

import com.example.barter.dto.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Repository
public interface ProductRepository extends R2dbcRepository<ProductEntity, String> {

        @Query("SELECT * FROM product ORDER BY score DESC OFFSET :offset LIMIT :limit")
        public Flux<ProductEntity> getAllByPageable(long offset, int limit);

        @Query("SELECT * FROM product where userid = :userId ORDER BY score DESC")
        public Flux<ProductEntity> getByUserId(UUID userId);

        @Query("INSERT INTO product (id, isbn, userid,works,title,authors,description,image,subjects,score) VALUES(:id, :isbn, :userId, :works ::jsonb ,:title,:authors ::jsonb, :description,:image, :subjects ::jsonb ,:score)")
        public Mono<Void> save(String id, long isbn,  UUID userId,String works,String title, String authors,String description, String image, String subjects, long score);

}

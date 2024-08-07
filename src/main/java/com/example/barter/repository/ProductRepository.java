package com.example.barter.repository;

import com.example.barter.dto.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@Repository
public interface ProductRepository extends R2dbcRepository<ProductEntity, String> {

        @Query("SELECT * FROM product ORDER BY score DESC OFFSET :offset LIMIT :limit")
        public Flux<ProductEntity> getAllByPageable(long offset, int limit);

        @Query("SELECT * FROM product where userid = :userId ORDER BY score DESC")
        public Flux<ProductEntity> getByUserId(UUID userId);

        @Query("""
                SELECT * from product where
                title ILIKE :q
                OR
                EXISTS
                (
                    SELECT 1 from UNNEST(authors) as author
                    where author ILIKE :q
                )
                OR
                EXISTS
                (
                    SELECT 1 from UNNEST(subjects) as subject
                    where subject ILIKE :q
                )
                """
        )
        public Flux<ProductEntity> getByQuery(String q);


        @Query("""
                SELECT * from product where EXISTS
                (
                    SELECT 1 from UNNEST(subjects) as subject
                    where subject = :subject
                )
                """)
        public Flux<ProductEntity> getByFilter(String subject);


        @Query("""
                with insert_product as
                (
                INSERT INTO product (id, isbn, userid,works,title,authors,description,image,subjects,score)
                VALUES(:id, :isbn, :userId, :works ,:title,:authors, :description,:image, :subjects ,:score)
                returning id
                )
                
                update users set products = array_append(products, (select id from insert_product))
                where id = :userId
            
                """)
        public Mono<Void> save(String id, long isbn, UUID userId, List<Map<String,String>> works, String title, String[] authors, String description, String[] image, String[] subjects, long score);

}

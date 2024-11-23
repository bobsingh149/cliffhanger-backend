package com.example.barter.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.barter.dto.entity.ProductEntity;
import com.example.barter.dto.model.CommentModel;
import com.example.barter.dto.model.CommentsWithUserBasicInfo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface ProductRepository extends R2dbcRepository<ProductEntity, String> {

        @Query("""
                with liked_users as
                (
                    select connections as ids
                    from users
                    where id = :userId
                ),
                
                  with connected_users as
                (
                    select liked_users as ids
                    from users
                    where id = :userId
                ),
                
                
                users_with_subjects as
                (
                    select u.*,
                    (
                        select  array_agg(distinct subject) as user_subjects
                        from product as book, unnest(book.subjects) as subject
                
                        where book.id = any(array_cat(u.products, u.likedproducts))
                    )
                    from users as u
                ),
                cur_user_subjects as
                (
                    select uws.user_subjects as subjects
                    from users_with_subjects as uws
                    where uws.id = :userId
                ),
                users_with_score as
                (
                select uws.id,
                (
                    select count(*)
                    from unnest(uws.user_subjects) as subject, cur_user_subjects
                    where subject = any(cur_user_subjects.subjects)
                ) as common_subject_count
                
                from users_with_subjects as uws
                ),
           
                 ranked_posts AS (
                  SELECT
                    product.*,
                    -- Base score
                    (array_length(product.likes) + (array_length(product.comments) * 2)) AS base_score,
                
                    (
                    select users_with_score.common_subject_count from users_with_subjects where users_with_subjects.id = product.userid 
                    ) as common_subjects,
                
  
                    -- Time decay (using hours)
                    1.0 / (1 + LN(GREATEST(EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - product.created_at)) / 3600, 1))) AS time_decay,
                
                    -- Relative engagement
                    (array_length(product.likes) + (afrray_length(product.comments) * 2)) / GREATEST(EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - product.created_at)) / 3600 * 0.5, 1) AS relative_engagement,
                 
                    (
                    case 
                    when product.userid = any(connected_users.ids)
                    then 3.0
                   
                    when product.userid = any(liked_users.ids)
                    then 2.0
                    
                    else
                    1.0
               
                    end
                    ) as boost
                    
                    
                  FROM
                    product , best_users, connected_users, liked_users
                
                )
                
                select id,title,authors
                 (base_score  * (1 + common_subjects)*0.1 *  time_decay * relative_engagement * boost) AS final_score
                FROM
                  ranked_posts
                ORDER BY
                  final_score DESC
                offset :offset LIMIT :limit
                """)
         Flux<ProductEntity> getAllByPageable(String userId, long offset, int limit);


        @Query("""
                select * from product
                where post_category = 'barter' and city = :city and userId != :userId
                order by score desc
                offset :offset limit :limit;
                """)
         Flux<ProductEntity> getByBarterFilter(String city, String userId, long offset, int limit);

        @Query("""
                
                with product_with_subject as
                (
                    select *,
                    (
                    select subject from unnest(subjects as subject)
                    where subject ilike :q || '%'
                    order by length(subject) limit 1
                    ) as query_subject,
                    (
                    case
                
                    when title ilike :q || '%'
                    then title
                    else
                    NULL
                
                    end
                    ) as query_title
            
                    from product
                )
                
                subject_with_query(
                select *,
                (
                  case
        
                when query_subject != NULL and query_title != NULL
                then (
                case 
                when length(query_title)<= then query_title else query_subject 
                end)
                
                when query_title != NULL
                then query_title
                else
                query_subject
                end
                ) as query_word
                
                from product_with_subject
                where query_subject != NULL or query_title != NULL        
                )
                
                SELECT
                    id,
                    userid,
                    title,
                    authors,
                    cover_images,
                    description,
                    subjects,
                    score,
                    caption,
                    post_image,
                    category,
                    likes,
                    comments,
                    created_at
                from subject_with_query
                order by length(query_word)
                offset :offset limit :limit
                """)
        Flux<ProductEntity> getBySearch(String q, String userId, long offset, int limit);

        @Query("SELECT * FROM product where userid = :userId ORDER BY created_at DESC")
         Flux<ProductEntity> getByUserId(String userId);


        @Query("""
                with user_name_cte as
                (
                select name from users where id = :userId
                ),
                
                 insert_product as (
                INSERT INTO product (userid, username, title,authors,cover_images,subjects,score,caption,post_image, category)
                VALUES(:userId, (select name from user_name_cte)  ,:title,:authors,:coverImages, :subjects ,:score, :caption, :postImage, :category) returning id
                )
               
                update users set products = array_append(products, (select id from insert_product))
                where id = :userId
                """)
        Mono<Void> save(String userId, String title, String[] authors, String[] coverImages, String[] subjects, long score, String caption, String postImage, String category);


        @Query("""
                update product
                set comments = comments || :commentModel
                where id = :id;
                """)
        Mono<Void> saveComment(String id, CommentModel commentModel);

    @Query("""
                update product
                set likes = array_append(likes,:userId)
                where id = :id;
                """)
    Mono<Void> likePost(String id, String userId);

    @Query("""
            UPDATE product 
            SET comments = (
                SELECT jsonb_agg(
                    CASE 
                        WHEN comment->>'id' = :commentId 
                        THEN jsonb_set(comment, '{likeCount}', (COALESCE((comment->>'likeCount')::int, 0) + 1)::text::jsonb)
                        ELSE comment 
                    END
                )
                FROM jsonb_array_elements(comments) comment
            )
            WHERE id = :productId
            """)
    Mono<Void> incrementCommentLikeCount(String productId, String commentId);

 
    @Query("""
            WITH comment_users AS (
                SELECT DISTINCT comment->>'userId' as user_id
                FROM product,
                jsonb_array_elements(comments) comment
                WHERE id = :productId
            )
            SELECT 
                (SELECT jsonb_agg(comment ORDER BY (comment->>'likeCount')::int DESC)
                 FROM product p,
                 jsonb_array_elements(p.comments) comment
                 WHERE p.id = :productId) as comments,
                (SELECT jsonb_agg(jsonb_build_object(
                    'id', u.id,
                    'name', u.name,
                    'profileImage', u.profile_image
                 ))
                 FROM users u
                 WHERE u.id IN (SELECT user_id FROM comment_users)
                ) as user_infos
            """)
    Mono<CommentsWithUserBasicInfo> getCommentsByProductId(String productId);

}

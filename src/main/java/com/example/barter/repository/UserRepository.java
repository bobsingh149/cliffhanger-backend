package com.example.barter.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.barter.dto.entity.UserEntity;
import com.example.barter.dto.model.BookBuddyModel;
import com.example.barter.dto.model.ConversationModel;
import com.example.barter.dto.model.RequestModel;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, String> {

    @Query("""
             update users set name = :name, age = :age, profile_image = :profileImage, bio = :bio, city = :city where id = :id
            """)
    Mono<Void> updateUser(String name, int age, String profileImage, String bio, String city);

    @Query("insert into users(id,name,age,profile_image,bio,city) values (:id,:name,:age,:profileImage,:bio,:city)")
    Mono<Void> saveUser(String id, String name, int age, String profileImage, String bio, String city);

    @Query("""
            update users
            set connections =
            case
            when id = :id
            then ( select array( select distinct array_append(connections,:connectionId) ) )
            when id = :connectionId
            then ( select array( select distinct array_append(connections,:id) ) )
            else
            connections
            end;
            """)
    Mono<Void> saveConnection(String id, String connectionId);

    @Query("""
            update users
            set connections =
            case
            when id = :id
            then (select array(select distinct unnest(array_cat(connections,:ids))))
            else
            connections
            end;
            """)
    Mono<Void> saveGroupConnection(String id, String[] ids);

    @Query("""
            update users
            set conversations = conversations || :conversationModel
            where id = :id1 or id = :id2;
           
            """)
    Mono<Void> saveConversation(String id1, String id2, ConversationModel conversationModel);

    @Query("""
            update users
            set conversations = conversations || :conversationModel
            where id = :id;
            """)
    Mono<Void> saveConversationGroup(String id, ConversationModel conversationModel);

    @Query("update users set requests = requests || :requestModel where id = :id")
    Mono<Void> saveRequest(String id, RequestModel requestModel);

    @Query("""
            update users 
            set requests = (
                select jsonb_agg(request)
                from jsonb_array_elements(requests) request
                where (request->>'userId') != :requestId
            )
            where id = :id
            """)
    Mono<Void> removeRequest(String id, String requestId);

    @Query("""
            with user_connections as
            (select connections from users where id = :id)
            select u.*
            from users as u, user_connections as uc
            where u.id = any(uc.connections);
            """)
    Flux<UserEntity> getConnections(String id);


    @Query("""
            with user_requests as
            (select requests from users where id = :id)
            select u.*
            from users as u, user_requests as ur
            where u.id = any(ur.requests);
            """)
    Flux<UserEntity> getRequests(String id);


    @Query("""
            with user_products as
            (select products from users where id = :id)
            ,
            user_connections as
            (select connections from users where id = :id)
            ,
            common_products as(
            select u.*,
            (select count(*) from unnest(u.products) as product where product = any(up.products)) as common_count,
            (select array_agg(product) from unnest(u.products) as product where product = any(up.products)) as common_books
            from users as u, user_products as up, user_connections as uc
            where not u.id = :id
            and not u.id = any(uc.connections)
            )
            select cp.*
            from common_products as cp, users as u
            order by cp.common_count desc,u.score desc offset :offset limit :size;
            """)
    Flux<UserEntity> getCommonUsers(String id, long offset, int size);

    @Query(
            """
                    with discard_users as
                    (
                        select array_cat(connections,book_buddies) as ids
                        from users
                        where id = 'userId'
                    ),
                 
                    users_with_subjects as
                    (
                        select u.*,
                        (
                            select  array_agg(distinct subject) as subjects
                            from product as book, unnest(book.subjects) as subject
                            where book.id = any(array_cat(u.products, u.likedproducts))
                        )
              
                        from users as u
                    ),
                 
                    my_subjects as
                    (
                        select uws.user_subjects as subjects
                        from users_with_subjects as uws
                        where uws.id = 'userId'
                    ),
                 
                    users_with_score as
                    (
                    select uws.id,
                    (
                        select count(*)
                        from unnest(uws.user_subjects) as subject, my_subjects
                        where subject = any(my_subjects.subjects)
                    ) + 10 as common_subject_count
                 
                    from users_with_subjects as uws ,discard_users
                    where uws.id != any(discard_users.ids) and uws.id != :userId
                    ),
                 
                    SELECT
                           id,
                           name,
                           age,
                           bio,
                           city,
                           products,
                           liked_products,
                           commented_products,
                           profile_image,
                           connections,
                           requests,
                           book_buddies
                 
                    from users_with_score
                    order by common_subject_count desc  offset 0 limit 1
                 """
    )
    Flux<UserEntity> getBookBuddy(String userId);

    @Query("""
                    update users
                    set book_buddies = book_buddies || :bookBuddyModel
                    where id = :id
            """)
    Mono<Void> addBookBuddy(String id, BookBuddyModel bookBuddyModel);


    @Query("""
            select * from users where id = any(:connectionIds) or id = any(:requestIds) or id = any(:bookIds)
            """)
    Flux<UserEntity> getUserInfoFromIds(String[] connectionIds, String[] requestIds, String[] bookIds);

    
    @Query("""

with book_buddy_ids as
(
     select  jsonb_array_elements(book_buddies)->>'userId' as id,
     (jsonb_array_elements(book_buddies)->>'common_subject_count')::int as common_subject_count
     from users where id = :id
)

select * from users,book_buddy_ids where users.id in (book_buddy_ids.id)
order by book_buddy_ids.common_subject_count desc;

       """)
    Flux<UserEntity> getBookBuddiesOrderByScore(String id);

}



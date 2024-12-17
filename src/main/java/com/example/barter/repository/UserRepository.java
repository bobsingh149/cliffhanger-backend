package com.example.barter.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.barter.dto.entity.BookBuddyEntity;
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
    Mono<Void> updateUser(String id,String name, int age, String profileImage, String bio, String city);

    @Query("insert into users(id,name,age,profile_image,bio,city) values (:id,:name,:age,:profileImage,:bio,:city)")
    Mono<Void> saveUser(String id, String name, int age, String profileImage, String bio, String city);

    @Query("""
            update users
            set connections =
            case
            when id = :id
            then (select array( select distinct array_append(connections,:connectionId) ) )
            when id = :connectionId
            then (select array( select distinct array_append(connections,:id) ) )
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
            set conversations = 
            case 
                when not exists (
                    select 1 
                    from jsonb_array_elements(conversations) as conversation 
                    where (conversation->>'conversationId') = (:conversationModel->>'conversationId')
                )
                then conversations || jsonb_build_array(:conversationModel)
                else conversations
            end
            where id = :id;
            """)
    Mono<Void> saveConversation(String id, ConversationModel conversationModel);

    @Query("""
            update users
            set conversations = conversations || jsonb_build_array(:conversationModel)
            where id = :id;
            """)
    Mono<Void> saveConversationGroup(String id, ConversationModel conversationModel);

    @Query("""
            update users
            set requests = case 
                when not exists (
                    select 1 
                    from jsonb_array_elements(coalesce(requests, '[]'::jsonb)) as request 
                    where (request->>'userId') = (:requestModel->>'userId')
                )
                then coalesce(requests, '[]'::jsonb) || jsonb_build_array(:requestModel)
                else requests
            end
            where id = :id
            """)
    Mono<Void> saveRequest(String id, RequestModel requestModel);

    @Query("""
            update users
            set requests = coalesce(
                (
                    select jsonb_agg(request)
                    from jsonb_array_elements(requests) request
                    where (request->>'userId') != :requestId
                ),
                '[]'::jsonb
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
                
                                                    with book_buddy_ids as
                                                        (
                                                        select ARRAY_AGG(buddy_id->>'userId') as ids
                                                        from users, jsonb_array_elements(book_buddies) as buddy_id
                                                        where users.id = :userId
                                                        ),
                                                       
                                    
                                                        connection_ids as
                                                        (
                                                            select ARRAY_AGG(connection->>'userId') as ids
                                                            from users, jsonb_array_elements(conversations) as connection
                                                            where users.id = :userId
                                                        ),
                                    
                                                        discard as
                                                        (
                                                                select array(select distinct unnest(
                                                                 array_cat(:userIds, array_cat(connection_ids.ids,book_buddy_ids.ids)))) as ids
                                                                from book_buddy_ids, connection_ids
                                                        ),
                                     
                                                        discard_users as
                                                        (
                                                            select(
                                                            select array_agg(id)
                                                            from unnest(discard.ids) as id
                                                            where id is not null
                                                            ) as ids
                                                            from discard
                                                        ),

                                     
                                                            users_with_subjects as
                                                            (
                                                                select u.*,
                                                                (
                                                                    select  array_agg(distinct subject) as user_subjects
                                                                    from product as book, unnest(book.subjects) as subject
                                                                    where book.id = any(array_cat(u.products, u.liked_products))
                                                                )
                                    
                                                                from users as u
                                                            ),
                                     
                                                        filtered_users as
                                                        (
                                                            select *
                                                            from users_with_subjects, discard_users
                                                            where id != ALL(discard_users.ids)
                                                        ),
                                                       
                                                            my_subjects as
                                                            (
                                                                select uws.user_subjects as subjects
                                                                from users_with_subjects as uws
                                                                where uws.id = :userId
                                                            ),
                                                       
                                                            users_with_score as
                                                            (
                                                            select fu.*,
                                                            (
                                                                select count(*)
                                                                from unnest(fu.user_subjects) as subject, my_subjects
                                                                where subject = any(my_subjects.subjects)
                                                            ) + 0 as common_subject_count
                                                       
                                                            from filtered_users as fu
                                                            )
                                                        
                                                            SELECT
                                                                   id,
                                                                   name,
                                                                   age,
                                                                   bio,
                                                                   city,
                                                                   profile_image,
                                                                   common_subject_count
                                                       
                                                            from users_with_score as uws
                                                            order by uws.common_subject_count desc  offset 0 limit 1;                              

                 """
    )
    Flux<BookBuddyEntity> getBookBuddy(String userId, String[] userIds);

    @Query("""
                    update users
                    set book_buddies = book_buddies || jsonb_build_array(:bookBuddyModel)
                    where id = :id
            """)
    Mono<Void> addBookBuddy(String id, BookBuddyModel bookBuddyModel);


    @Query("""
            select * from users where id = any(:connectionIds) or id= any(:members) or  id = any(:requestIds) or id = any(:bookIds)
            """)
    Flux<UserEntity> getUserInfoFromIds(String[] connectionIds, String[] members, String[] requestIds, String[] bookIds);

    
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

    @Query("""
        update users
        set conversations = coalesce(
            (
                select jsonb_agg(conversation)
                from jsonb_array_elements(conversations) as conversation
                where (conversation->>'conversationId') != :conversationId
            ),
            '[]'::jsonb
        )
        where id = :id
        """)
    Mono<Void> removeConversation(String id, String conversationId);

}



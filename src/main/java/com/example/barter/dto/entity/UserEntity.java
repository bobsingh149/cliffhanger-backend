package com.example.barter.dto.entity;

import com.example.barter.dto.input.SaveUserInput;
import com.example.barter.dto.model.BookBuddyModel;
import com.example.barter.dto.model.ConversationModel;
import com.example.barter.dto.model.RequestModel;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;


@Data
@Table("users")
@Builder
public class UserEntity {

    @Id
  private final String id;

    @NonNull
  private final String name;

   private final int age;


  private  final  String city;

  private  final  String bio;

  @Column("profile_image")
  private final String profileImage;


    private final   UUID[] products;

    @Column("liked_products")
    private final   UUID[] likedProducts;

    @Column("commented_products")
    private final   String[] commentedProducts;


    private final String[] connections;

    @Column("requests")
    private final RequestWrapper requestWrapper;

    @Column("conversations")
    private final ConversationWrapper conversationWrapper;

    @Builder
    public static record ConversationWrapper(List<ConversationModel> conversations){}

    @Column("book_buddies")
    private final BookBuddyWrapper bookBuddyWrapper;

    @Builder
    public static record BookBuddyWrapper(List<BookBuddyModel> bookBuddies){}

    @Builder
    public static record RequestWrapper(List<RequestModel> requests){}



    public static UserEntity fromSaveUserInput(final SaveUserInput saveUserInput, String profileImage)
    {


        return UserEntity.builder()
                .id(saveUserInput.id())
                .name(saveUserInput.name())
                .city(saveUserInput.city())
                .age(saveUserInput.age())
                .bio(saveUserInput.bio())
                .profileImage(profileImage)
                .build();

    }

}

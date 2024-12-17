package com.example.barter.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.barter.dto.entity.UserEntity;
import com.example.barter.dto.input.SaveConversationInput;
import com.example.barter.dto.input.SaveRequestInput;
import com.example.barter.dto.input.SaveUserInput;
import com.example.barter.dto.model.BookBuddyModel;
import com.example.barter.dto.model.ConversationModel;
import com.example.barter.dto.model.RequestModel;
import com.example.barter.dto.response.DetailedUserResponse;
import com.example.barter.dto.response.UserBookBuddyResponse;
import com.example.barter.dto.response.UserResponse;
import com.example.barter.exception.customexception.UserNotFoundException;
import com.example.barter.repository.UserRepository;
import com.example.barter.utils.CloudinaryUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class UserService {

    final UserRepository userRepository;
    final CloudinaryUtils cloudinaryUtils;

    @Autowired
    public UserService(UserRepository userRepository, CloudinaryUtils cloudinaryUtils) {
        this.userRepository = userRepository;
        this.cloudinaryUtils=cloudinaryUtils;
    }



    public Mono<Void> saveUser(SaveUserInput saveUserInput, MultipartFile file) throws IOException {


        final String profileImage = file != null
                                    ?cloudinaryUtils.uploadFileAndGetLink(file,"profile_images")
                                    :null;

        final var userEntity = UserEntity.fromSaveUserInput(saveUserInput,profileImage);

        return userRepository.saveUser(userEntity.getId(), userEntity.getName(),
                userEntity.getAge(), userEntity.getProfileImage(), userEntity.getBio(),
                userEntity.getCity());
    }

    public Mono<Void> updateUser(SaveUserInput saveUserInput, MultipartFile file) throws IOException {


        final String profileImage = file != null
                ?cloudinaryUtils.uploadFileAndGetLink(file,"profile_images")
                :null;

        final var userEntity = UserEntity.fromSaveUserInput(saveUserInput,profileImage);

        return userRepository.updateUser(userEntity.getId(),
                userEntity.getName(),
                userEntity.getAge(), userEntity.getProfileImage(), userEntity.getBio(),
                userEntity.getCity());
    }



    public Mono<UserResponse> getUser(String id) {
        return userRepository.findById(id).map(UserResponse::fromUserEntity);
    }

    public Mono<Void> deleteUser(String id) {
        return userRepository.deleteById(id);
    }

    public Mono<Void> saveConnection(String id, SaveConversationInput saveConversationInput) {
        String conversationId = saveConversationInput.isGroup() 
            ? UUID.randomUUID().toString()
            : (id.compareTo(saveConversationInput.getUserId()) < 0 
                ? id + "-" + saveConversationInput.getUserId()
                : saveConversationInput.getUserId() + "-" + id);
        
        saveConversationInput.setConversationId(conversationId);
        
        ConversationModel conversationModel = ConversationModel.fromSaveConversationInput(saveConversationInput);
        String otherId = conversationModel.getUserId();
        ConversationModel otherConversationModel = ConversationModel.fromSaveConversationInput(saveConversationInput);
        otherConversationModel.setUserId(id);

        return conversationModel.isGroup() 
            ? userRepository.saveConversationGroup(id, conversationModel)
            : userRepository.saveConversation(id, conversationModel)
                .then(userRepository.saveConversation(otherId, otherConversationModel))
                .then(userRepository.removeRequest(id, otherId));
    }

    public Mono<Void> saveRequest(SaveRequestInput saveRequestInput) {
        return userRepository.saveRequest(saveRequestInput.requestId(),
            RequestModel.builder()
                .userId(saveRequestInput.id())
                .timestamp(LocalDateTime.now())
                .build());
    }

    public Mono<Void> removeRequest(SaveRequestInput saveRequestInput) {
        return userRepository.removeRequest(saveRequestInput.id(), saveRequestInput.requestId());
    }


    public Flux<UserResponse> getCommonUsers(String id, PageRequest pageRequest) {
        return userRepository.getCommonUsers(id, pageRequest.getOffset(),
                pageRequest.getPageSize()).map(UserResponse::fromUserEntity);
    }


    public Flux<UserBookBuddyResponse> getBookBuddy(String id) {

        return userRepository.getBookBuddy(id, new String[]{id})
            .flatMap(bookBuddyEntity -> 
                userRepository.addBookBuddy(id, BookBuddyModel.builder()
                    .userId(bookBuddyEntity.getId())
                    .timestamp(LocalDateTime.now())
                                .commonSubjectCount(bookBuddyEntity.getCommonSubjectCount())
                    .build())
                .thenReturn(bookBuddyEntity)
            )
            .map(UserBookBuddyResponse::fromBookBuddyEntity);
    }


    public Mono<DetailedUserResponse> fillUserInfo(UserEntity userEntity) {

        if(userEntity==null)
        {
            throw new UserNotFoundException("user not found");
        }

        return userRepository.getUserInfoFromIds
                (
                userEntity.getConversationWrapper().conversations().stream().map(ConversationModel::getUserId).toArray(String[]::new),
                        userEntity.getConversationWrapper().conversations().stream()
                                .flatMap(conversationModel -> conversationModel.getMembers().stream())
                                .toArray(String[]::new),
                        userEntity.getRequestWrapper().requests().stream().map(RequestModel::getUserId).toArray(String[]::new),
                userEntity.getBookBuddyWrapper().bookBuddies().stream().map(BookBuddyModel::getUserId).toArray(String[]::new)

                )
                .collectList()
                .map(userInfoList -> {
                    Map<String,UserEntity> userEntityMap = new HashMap<>();
                    userInfoList.forEach(userInfo->userEntityMap.put(userInfo.getId(),userInfo));
                    return DetailedUserResponse.fromUserEntity(userEntity,userEntityMap);
        });

    }



    public Mono<DetailedUserResponse> getUserSetup(String id) {

        return userRepository.findById(id)
                .flatMap(this::fillUserInfo);
    }

 

}

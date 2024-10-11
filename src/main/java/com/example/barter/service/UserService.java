package com.example.barter.service;

import com.example.barter.dto.entity.UserEntity;
import com.example.barter.dto.input.SaveConnectionInput;
import com.example.barter.dto.input.SaveRequestInput;
import com.example.barter.dto.input.SaveUserInput;
import com.example.barter.dto.response.UserResponse;
import com.example.barter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class UserService {

    final UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository)
    {
        this.userRepository=userRepository;
    }


    public Mono<Void> saveUser(SaveUserInput saveUserInput)
    {
        final var userEntity = UserEntity.fromSaveUserInput(saveUserInput);

      return   userRepository.saveUser(userEntity.getId(), userEntity.getName(), userEntity.getAge(), userEntity.getProducts(),userEntity.getProfileImage(),userEntity.getConnections());
    }


    public Mono<UserResponse> getUser(String id)
    {
         return userRepository.findById(id)
                    .map(UserResponse::fromUserEntity);
    }


    public Mono<UserResponse> updateUser(SaveUserInput saveUserInput)
    {
        return userRepository.updateUser(saveUserInput.id(),saveUserInput.name(),saveUserInput.age(),saveUserInput.profileImage())
                .map(UserResponse::fromUserEntity);
    }

    public Mono<Void> deleteUser(String id)
    {
        return userRepository.deleteById(id);
    }

    public Mono<Void> saveConnection(SaveConnectionInput saveConnectionInput)
    {
        return userRepository.saveConnection(saveConnectionInput.id(), saveConnectionInput.connectionId());
    }


    public Flux<UserResponse> getConnections(String id)
    {
        return userRepository.getConnections(id).map(UserResponse::fromUserEntity);
    }



    public Mono<Void> saveRequest(SaveRequestInput saveRequestInput)
    {
        return userRepository.saveRequest(saveRequestInput.id(), saveRequestInput.requestId());
    }


    public Flux<UserResponse> getRequests(String id)
    {
        return userRepository.getRequests(id).map(UserResponse::fromUserEntity);
    }



    public Flux<UserResponse> getCommonUsers(String id, PageRequest pageRequest)
    {
        return userRepository.getCommonUsers(id, pageRequest.getOffset(),pageRequest.getPageSize()).map(UserResponse::fromUserEntity);
    }
}

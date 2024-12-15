package com.example.barter.utils;

import com.example.barter.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public class ControllerUtils {

      public static  <T> ResponseEntity<ApiResponse<Object>> mapToResponseEntity(T data, ResponseMessage message, HttpStatus httpStatus) {

        final var response= ApiResponse.builder().data(data).status(message).build();
        return new ResponseEntity<>(response,httpStatus);

    }
    public static  <T> ResponseEntity<Mono<ApiResponse<Object>>> mapMonoToResponseEntity(Mono<T> mono, ResponseMessage message, HttpStatus httpStatus) {

        final var apiResponseMono= mono.map(data -> ApiResponse.builder().data(data).status(message).build());
        return new ResponseEntity<>(apiResponseMono,httpStatus);

    }

    public static  <T> ResponseEntity<Mono<ApiResponse<Object>>> mapFLuxToResponseEntity(Flux<T> flux, ResponseMessage message, HttpStatus httpStatus) {

     final var mono =  flux.collectList();

     return mapMonoToResponseEntity(mono,message,httpStatus);

    }
    public static  <T> ResponseEntity<Flux<CompletableFuture<ApiResponse<Object>>>> mapFLuxAsyncToResponseEntity(Flux<CompletableFuture<T>> flux, ResponseMessage message, HttpStatus httpStatus) {

        final var apiResponseflux = flux.map(asyncData -> asyncData.thenApply(data->ApiResponse.builder().data(data).status(message).build()));
        return new ResponseEntity<>(apiResponseflux,httpStatus);
    }

    public enum ResponseMessage
    {
        success,
        failure,
        server_under_maintenance,
    }
}

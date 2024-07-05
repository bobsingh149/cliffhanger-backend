package com.example.barter.utils;

import com.example.barter.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ControllerUtils {

    public static  <T> ResponseEntity<Mono<ApiResponse<Object>>> mapMonoToResponseEntitiy(Mono<T> mono, String message, HttpStatus httpStatus) {

        final var apiResponseMono= mono.map(data -> ApiResponse.builder().data(data).message(message).build());
        return new ResponseEntity<>(apiResponseMono,httpStatus);

    }

    public static  <T> ResponseEntity<Flux<ApiResponse<Object>>> mapFLuxToResponseEntitiy(Flux<T> flux, String message, HttpStatus httpStatus) {

        final var apiResponseflux= flux.map(data -> ApiResponse.builder().data(data).message(message).build());
        return new ResponseEntity<>(apiResponseflux,httpStatus);

    }



}

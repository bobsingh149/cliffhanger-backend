package com.example.barter.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response<T> {

    String message;
    T data;
}

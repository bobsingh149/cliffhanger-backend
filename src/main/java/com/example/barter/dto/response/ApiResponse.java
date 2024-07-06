package com.example.barter.dto.response;

import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class ApiResponse<T> {

    String message;
    T data;
}


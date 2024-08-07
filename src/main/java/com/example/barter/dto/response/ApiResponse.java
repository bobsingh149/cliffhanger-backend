package com.example.barter.dto.response;

import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class ApiResponse<T> {

    private final String message;
  private final  T data;
}


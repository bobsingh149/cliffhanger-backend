package com.example.barter.dto.response;

import lombok.Builder;
import lombok.Data;
import com.example.barter.utils.ControllerUtils;

@Data
@Builder
public class ApiResponse<T> {
  private final ControllerUtils.ResponseMessage status;
  private final  T data;
}


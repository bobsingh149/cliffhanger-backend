package com.example.barter.exception;

import com.example.barter.utils.ControllerUtils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private final String errorMessage;
    private final ControllerUtils.ResponseMessage status;
}

package com.example.barter.dto.input;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SaveProductInput {

    private String isbn;
    private UUID userId;
}



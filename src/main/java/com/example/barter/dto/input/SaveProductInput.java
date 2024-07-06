package com.example.barter.dto.input;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SaveProductInput {

    private long isbn;
    private UUID userId;
}



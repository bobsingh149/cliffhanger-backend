package com.example.barter.dto.input;
import java.util.UUID;

public record ChatInput(UUID from,
                        UUID to,
                        String message,
                        boolean isRead,
                        boolean isEdited) {}

package com.example.barter.dto.input;
import java.util.UUID;


public record SaveUserInput(UUID id, String name, String profileImage, int age) {
}

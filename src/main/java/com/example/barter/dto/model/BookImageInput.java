package com.example.barter.dto.model;

import org.springframework.web.multipart.MultipartFile;

public record BookImageInput(MultipartFile image, String caption) {
}
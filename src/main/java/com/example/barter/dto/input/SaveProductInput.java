package com.example.barter.dto.input;
import com.example.barter.dto.model.BookImageInput;
import com.example.barter.dto.model.PostCategory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


public record SaveProductInput(
        String title,
        int score,
        List<String> authors,
        List<String> coverImages,
        List<String> subjects,
        String userId,
        String caption,
        PostCategory category)
{


}



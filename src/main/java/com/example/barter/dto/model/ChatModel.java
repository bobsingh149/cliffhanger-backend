package com.example.barter.dto.model;

import lombok.Builder;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Builder
public record ChatModel(UUID from, UUID to, String message, LocalDateTime timestamp, boolean isRead, boolean isEdited) {


    public Map<String,Object> convertToMap()
    {
        Map<String ,Object> map  = new HashMap<>();

        Field[] fields = this.getClass().getFields();

        Arrays.stream(fields).forEach(
         field -> {
             try {
                 map.put(field.getName(),field.get(this));
             } catch (IllegalAccessException e) {
                 throw new RuntimeException(e);
             }
         }
        );

        return map;

    }
}

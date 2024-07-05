package com.example.barter.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtils {


    public static <T> String convertToJson( T object) {

        try {
            final var jsonStr = new ObjectMapper().writeValueAsString(object);
            return  jsonStr;
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }

    }
}

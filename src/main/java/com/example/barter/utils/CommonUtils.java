package com.example.barter.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Stream;

public class CommonUtils {


    public static  String[] toArray(List<String> list)
    {
        if(list == null)
        {
            return null;
        }

        return list.toArray(new String[0]);
    }

    public static  String[] toArray(Stream<String> stream)
    {
        if(stream == null)
        {
            return null;
        }

        return stream.toArray(String[]::new);
    }



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

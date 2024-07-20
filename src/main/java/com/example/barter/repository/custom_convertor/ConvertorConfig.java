package com.example.barter.repository.custom_convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class ConvertorConfig {

    private final ObjectMapper objectMapper;

    @Autowired
    public ConvertorConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public R2dbcCustomConversions customConversions() {
        List<Converter<?,?>> converters = new ArrayList<>();
        converters.add(new JsonToListOfMapConverter(objectMapper));
        converters.add(new ListOfMapToJsonConverter(objectMapper));
        converters.add(new JsonToListOfStringConverter(objectMapper));
        converters.add(new ListOfStringToJsonConverter(objectMapper));
        converters.add(new JsonToMapConverter(objectMapper));
        converters.add(new MapToJsonConverter(objectMapper));

        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
    }

    @ReadingConverter
    static class JsonToListOfMapConverter implements Converter<Json,List<Map<String,String>> > {

        private final ObjectMapper objectMapper;

        public JsonToListOfMapConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public List<Map<String,String>> convert(Json json) {
            try {
                return objectMapper.readValue(json.asString(), new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                log.error("Problem while parsing JSON: {}", json, e);
            }
            return new ArrayList<>();
        }
    }

    @WritingConverter
    static class ListOfMapToJsonConverter implements Converter<List<Map<String,String>>, Json> {

        private final ObjectMapper objectMapper;

        public ListOfMapToJsonConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Json convert(List<Map<String,String>> source) {
            try {
                return Json.of(objectMapper.writeValueAsString(source));
            } catch (JsonProcessingException e) {
                log.error("Error occurred while serializing map to JSON: {}", source, e);
            }
            return Json.of("");
        }
    }


    @ReadingConverter
    static class JsonToListOfStringConverter implements Converter<Json,List<String> > {

        private final ObjectMapper objectMapper;

        public JsonToListOfStringConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public List<String> convert(Json json) {
            try {
                return objectMapper.readValue(json.asString(), new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                log.error("Problem while parsing JSON: {}", json, e);
            }
            return new ArrayList<>();
        }
    }

    @WritingConverter
    static class ListOfStringToJsonConverter implements Converter<List<String>, Json> {

        private final ObjectMapper objectMapper;

        public ListOfStringToJsonConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Json convert(List<String> source) {
            try {
                return Json.of(objectMapper.writeValueAsString(source));
            } catch (JsonProcessingException e) {
                log.error("Error occurred while serializing map to JSON: {}", source, e);
            }
            return Json.of("");
        }
    }


    @ReadingConverter
    static class JsonToMapConverter implements Converter<Json,Map<String,Object>> {

        private final ObjectMapper objectMapper;

        public JsonToMapConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Map<String,Object> convert(Json json) {
            try {
                return objectMapper.readValue(json.asString(), new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                log.error("Problem while parsing JSON: {}", json, e);
            }

            return Map.of();
        }
    }

    @WritingConverter
    static class MapToJsonConverter implements Converter<Map<String,Object>, Json> {

        private final ObjectMapper objectMapper;

        public MapToJsonConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Json convert(Map<String,Object> source) {
            try {
                return Json.of(objectMapper.writeValueAsString(source));
            } catch (JsonProcessingException e) {
                log.error("Error occurred while serializing map to JSON: {}", source, e);
            }
            return Json.of("");
        }
    }
}
package com.example.barter.repository.custom_convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;

import com.example.barter.dto.entity.ConversationEntity;
import com.example.barter.dto.entity.ProductEntity;
import com.example.barter.dto.entity.UserEntity;
import com.example.barter.dto.model.BookBuddyModel;
import com.example.barter.dto.model.ChatModel;
import com.example.barter.dto.model.CommentModel;
import com.example.barter.dto.model.ConversationModel;
import com.example.barter.dto.model.CommentsWithUserBasicInfo;
import com.example.barter.dto.model.RequestModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import lombok.extern.slf4j.Slf4j;

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
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new JsonToListOfMapConverter(objectMapper));
        converters.add(new ListOfMapToJsonConverter(objectMapper));
        converters.add(new JsonToListOfStringConverter(objectMapper));
        converters.add(new ListOfStringToJsonConverter(objectMapper));
        converters.add(new JsonToMapConverter(objectMapper));
        converters.add(new MapToJsonConverter(objectMapper));
        converters.add(new JsonToChatModelConverter(objectMapper));
        converters.add(new ChatModelToJsonConvertor(objectMapper));
        converters.add(new JsonToChatWrapperConverter(objectMapper));
        converters.add(new ConversationModelToJsonConvertor(objectMapper));
        converters.add(new JsonToCoversationModelConverter(objectMapper));
        converters.add(new JsonToCommentsWrapperConverter(objectMapper));
        converters.add(new JsonToConversationsWrapperConverter(objectMapper));
        converters.add(new JsonToBookBuddyModelConverter(objectMapper));
        converters.add(new BookBuddyModelToJsonConverter(objectMapper));
        converters.add(new JsonToCommentModelConverter(objectMapper));
        converters.add(new CommentModelToJsonConverter(objectMapper));
        converters.add(new JsonToCommentsWithUserBasicInfoConverter(objectMapper));
        converters.add(new JsonToBookBuddyWrapperConverter(objectMapper));
        converters.add(new JsonToRequestModelConverter(objectMapper));
        converters.add(new RequestModelToJsonConverter(objectMapper));
        converters.add(new JsonToRequestWrapperConverter(objectMapper));

        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
    }

    @ReadingConverter
    static class JsonToListOfMapConverter implements Converter<Json, List<Map<String, String>>> {

        private final ObjectMapper objectMapper;

        public JsonToListOfMapConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public List<Map<String, String>> convert(Json json) {
            try {
                return objectMapper.readValue(json.asString(), new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                log.error("Problem while parsing JSON: {}", json, e);
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @WritingConverter
    static class ListOfMapToJsonConverter implements Converter<List<Map<String, String>>, Json> {

        private final ObjectMapper objectMapper;

        public ListOfMapToJsonConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Json convert(List<Map<String, String>> source) {
            try {
                return Json.of(objectMapper.writeValueAsString(source));
            } catch (JsonProcessingException e) {
                log.error("Error occurred while serializing map to JSON: {}", source, e);
                throw new RuntimeException(e.getMessage());

            }
        }
    }


    @ReadingConverter
    static class JsonToListOfStringConverter implements Converter<Json, List<String>> {

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
                throw new RuntimeException(e.getMessage());

            }
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
                throw new RuntimeException(e.getMessage());

            }
        }
    }


    @ReadingConverter
    static class JsonToMapConverter implements Converter<Json, Map<String, Object>> {

        private final ObjectMapper objectMapper;

        public JsonToMapConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Map<String, Object> convert(Json json) {
            try {
                return objectMapper.readValue(json.asString(), new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                log.error("Problem while parsing JSON: {}", json, e);
                throw new RuntimeException(e.getMessage());

            }


        }
    }

    @WritingConverter
    static class MapToJsonConverter implements Converter<Map<String, Object>, Json> {

        private final ObjectMapper objectMapper;

        public MapToJsonConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Json convert(Map<String, Object> source) {
            try {
                return Json.of(objectMapper.writeValueAsString(source));
            } catch (JsonProcessingException e) {
                log.error("Error occurred while serializing map to JSON: {}", source, e);
                throw new RuntimeException(e.getMessage());

            }
        }
    }

    @ReadingConverter
    static class JsonToChatModelConverter implements Converter<Json, ChatModel> {

        private final ObjectMapper objectMapper;

        public JsonToChatModelConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public ChatModel convert(Json json) {

            try {
                return objectMapper.readValue(json.asString(), ChatModel.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @WritingConverter
    static class ChatModelToJsonConvertor implements Converter<ChatModel, Json> {

        private final ObjectMapper objectMapper;

        public ChatModelToJsonConvertor(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Json convert(ChatModel source) {
            try {
                return Json.of(objectMapper.writeValueAsString(source));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @ReadingConverter
    static class JsonToCoversationModelConverter implements Converter<Json, ConversationModel> {

        private final ObjectMapper objectMapper;

        public JsonToCoversationModelConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public ConversationModel convert(Json json) {

            try {
                return objectMapper.readValue(json.asString(), ConversationModel.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @WritingConverter
    static class ConversationModelToJsonConvertor implements Converter<ConversationModel, Json> {

        private final ObjectMapper objectMapper;

        public ConversationModelToJsonConvertor(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Json convert(ConversationModel source) {
            try {
                return Json.of(objectMapper.writeValueAsString(source));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @ReadingConverter
    static class JsonToChatWrapperConverter implements Converter<Json, ConversationEntity.ChatWrapper> {

        private final ObjectMapper objectMapper;

        public JsonToChatWrapperConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public ConversationEntity.ChatWrapper convert(Json json) {

            try {
                final List<ChatModel> chats = objectMapper.readValue(json.asString(), new TypeReference<>() {
                });

                return ConversationEntity.ChatWrapper.builder().chats(chats).build();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }


    @ReadingConverter
    static class JsonToConversationsWrapperConverter implements Converter<Json, UserEntity.ConversationWrapper> {

        private final ObjectMapper objectMapper;

        public JsonToConversationsWrapperConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public UserEntity.ConversationWrapper convert(Json json) {

            try {
                final List<ConversationModel> conversationModels = objectMapper.readValue(json.asString(), new TypeReference<>() {
                });

                return UserEntity.ConversationWrapper.builder().conversations(conversationModels).build();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

    }

    @ReadingConverter
    static class JsonToCommentsWrapperConverter implements Converter<Json, ProductEntity.CommentsWrapper> {

        private final ObjectMapper objectMapper;

        public JsonToCommentsWrapperConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public ProductEntity.CommentsWrapper convert(Json json) {

            try {
                final List<CommentModel> comments = objectMapper.readValue(json.asString(), new TypeReference<>() {
                });

                return ProductEntity.CommentsWrapper.builder().comments(comments).build();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    
    @ReadingConverter
    static class JsonToBookBuddyWrapperConverter implements Converter<Json, UserEntity.BookBuddyWrapper> {

        private final ObjectMapper objectMapper;

        public JsonToBookBuddyWrapperConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public UserEntity.BookBuddyWrapper convert(Json json) {

            try {
                final List<BookBuddyModel> bookBuddies = objectMapper.readValue(json.asString(), new TypeReference<>() {
                });

                return UserEntity.BookBuddyWrapper.builder().bookBuddies(bookBuddies).build();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

    }


    @ReadingConverter
    static class JsonToBookBuddyModelConverter implements Converter<Json, BookBuddyModel> {

        private final ObjectMapper objectMapper;

        public JsonToBookBuddyModelConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public BookBuddyModel convert(Json json) {
            try {
                return objectMapper.readValue(json.asString(), BookBuddyModel.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @WritingConverter
    static class BookBuddyModelToJsonConverter implements Converter<BookBuddyModel, Json> {

        private final ObjectMapper objectMapper;

        public BookBuddyModelToJsonConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Json convert(BookBuddyModel source) {
            try {
                return Json.of(objectMapper.writeValueAsString(source));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @ReadingConverter
    static class JsonToCommentModelConverter implements Converter<Json, CommentModel> {

        private final ObjectMapper objectMapper;

        public JsonToCommentModelConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public CommentModel convert(Json json) {
            try {
                return objectMapper.readValue(json.asString(), CommentModel.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @WritingConverter
    static class CommentModelToJsonConverter implements Converter<CommentModel, Json> {

        private final ObjectMapper objectMapper;

        public CommentModelToJsonConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Json convert(CommentModel source) {
            try {
                return Json.of(objectMapper.writeValueAsString(source));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @ReadingConverter
    static class JsonToCommentsWithUserBasicInfoConverter implements Converter<Row, CommentsWithUserBasicInfo> {
        private final ObjectMapper objectMapper;

        public JsonToCommentsWithUserBasicInfoConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public CommentsWithUserBasicInfo convert(Row row) {
            try {
                String commentsJson = ((Json) row.get("comments")).asString();
                String userInfosJson = ((Json) row.get("user_infos")).asString();
                
                List<CommentModel> comments = objectMapper.readValue(commentsJson, 
                    new TypeReference<List<CommentModel>>() {});
                List<CommentsWithUserBasicInfo.UserBasicInfo> userInfos = objectMapper.readValue(userInfosJson, 
                    new TypeReference<List<CommentsWithUserBasicInfo.UserBasicInfo>>() {});

                return CommentsWithUserBasicInfo.builder()
                    .comments(comments)
                    .userBasicInfos(userInfos)
                    .build();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @ReadingConverter
    static class JsonToRequestWrapperConverter implements Converter<Json, UserEntity.RequestWrapper> {
        private final ObjectMapper objectMapper;

        public JsonToRequestWrapperConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public UserEntity.RequestWrapper convert(Json json) {
            try {
                final List<RequestModel> requests = objectMapper.readValue(json.asString(), new TypeReference<>() {});
                return UserEntity.RequestWrapper.builder().requests(requests).build();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @ReadingConverter
    static class JsonToRequestModelConverter implements Converter<Json, RequestModel> {
        private final ObjectMapper objectMapper;

        public JsonToRequestModelConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public RequestModel convert(Json json) {
            try {
                return objectMapper.readValue(json.asString(), RequestModel.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @WritingConverter
    static class RequestModelToJsonConverter implements Converter<RequestModel, Json> {
        private final ObjectMapper objectMapper;

        public RequestModelToJsonConverter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public Json convert(RequestModel source) {
            try {
                return Json.of(objectMapper.writeValueAsString(source));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}



//
//    @ReadingConverter
//    static class JsonToUserImagesWrapperConverter implements Converter<Json, ProductEntity.UserImagesWrapper> {
//
//        private final ObjectMapper objectMapper;
//
//        public JsonToUserImagesWrapperConverter(ObjectMapper objectMapper) {
//            this.objectMapper = objectMapper;
//        }
//
//        @Override
//        public ProductEntity.UserImagesWrapper convert(Json json) {
//
//            try {
//                final List<BookImage> bookImages = objectMapper.readValue(json.asString(),
//                        new TypeReference<List<BookImage>>() {});
//
//                return ProductEntity.UserImagesWrapper.builder().userImages(bookImages).build();
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e.getMessage());
//            }
//        }
//    }

//    @WritingConverter
//    static class UserImagesWrapperToJsonConvertor implements Converter<ProductEntity.UserImagesWrapper, Json> {
//
//        private final ObjectMapper objectMapper;
//
//        public UserImagesWrapperToJsonConvertor(ObjectMapper objectMapper) {
//            this.objectMapper = objectMapper;
//        }
//
//        @Override
//        public Json convert(ProductEntity.UserImagesWrapper userImagesWrapper) {
//            try {
//                return Json.of(objectMapper.writeValueAsString(userImagesWrapper.userImages()));
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }



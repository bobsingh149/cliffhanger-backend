package com.example.barter.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.barter.dto.input.CommentInput;
import com.example.barter.dto.input.LikeInput;
import com.example.barter.dto.input.SaveProductInput;
import com.example.barter.dto.response.ApiResponse;
import com.example.barter.exception.customexception.ImageUploadFailed;
import com.example.barter.service.BookService;
import com.example.barter.service.ProductService;
import com.example.barter.utils.ControllerUtils;
import com.example.barter.utils.ControllerUtils.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

  
    @Autowired
    public ProductController(BookService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    @GetMapping(path = "/getAllProducts/{userId}")
    public ResponseEntity<Mono<ApiResponse<Object>>> getAllProduct(
            @PathVariable String userId, @RequestParam int page, @RequestParam int size) {
        
        final var response = productService.getAllByPageable(userId, PageRequest.of(page, size));
        return ControllerUtils.mapFLuxToResponseEntity(response, ResponseMessage.success, HttpStatus.OK);
    }

    @GetMapping(path = "/getProductById/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>> getById(@PathVariable UUID id) {
        final var response = productService.getById(id);
        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success, HttpStatus.OK);
    }

    @GetMapping(path = "/getUserProfile/{userId}")
    public ResponseEntity<Mono<ApiResponse<Object>>> getUserProfile(@PathVariable String userId) {
        final var response = productService.getUserProfile(userId);
        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success, HttpStatus.OK);
    }

    @GetMapping(path = "/getProductByUserId/{userId}")
    public ResponseEntity<Mono<ApiResponse<Object>>> getByUserId(@PathVariable String userId) {
        final var response = productService.getByUserId(userId);
        return ControllerUtils.mapFLuxToResponseEntity(response, ResponseMessage.success, HttpStatus.OK);
    }


    @DeleteMapping(path = "/deleteProductById/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>> deleteById(@PathVariable UUID id) {
        final var response = productService.deleteById(id);
        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success, HttpStatus.OK);
    }

    @PostMapping(path = "/saveProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Mono<ApiResponse<Object>>> saveProduct(@RequestParam(value = "data") String data, @RequestParam(value = "file", required = false) MultipartFile file) throws ImageUploadFailed, IOException, ServletException {

        SaveProductInput saveProductInput = objectMapper.readValue(data, SaveProductInput.class);
        final var response = productService.save(saveProductInput, file);
        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success, HttpStatus.CREATED);
    }


    @GetMapping(path = "/getSearchResults")
    public ResponseEntity<Mono<ApiResponse<Object>>> getSearchResults(@RequestParam String q) throws IOException {
        q = q.trim();
        final var response = productService.getSearchResults(q);
        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success, HttpStatus.OK);
    }

    @PostMapping("/postComment")
    public ResponseEntity<Mono<ApiResponse<Object>>> postComment(@RequestBody CommentInput commentInput) {
        final var response = productService.saveComment(commentInput);
        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success, HttpStatus.CREATED);
    }

    @PostMapping("/postLike")
    public ResponseEntity<Mono<ApiResponse<Object>>> postLike(@RequestBody LikeInput likeInput) {
        final var response = productService.saveLike(likeInput);
        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success, HttpStatus.CREATED);
    }

    @GetMapping(path = "/getByBarterFilter")
    public ResponseEntity<Mono<ApiResponse<Object>>> getByBarterFilter(
            @RequestParam String userId,
            @RequestParam int page,
            @RequestParam int size) {
        final var response = productService.getByBarterFilter(userId, PageRequest.of(page, size));
        return ControllerUtils.mapFLuxToResponseEntity(response, ResponseMessage.success, HttpStatus.OK);
    }

    @GetMapping(path = "/getPostsBySearch")
    public ResponseEntity<Mono<ApiResponse<Object>>> getPostsBySearch(
            @RequestParam String q,
            @RequestParam int page,
            @RequestParam int size) {
        q=q.toLowerCase();
        final var response = productService.getBySearch(q,PageRequest.of(page, size));
        return ControllerUtils.mapFLuxToResponseEntity(response, ResponseMessage.success, HttpStatus.OK);
    }

    @PostMapping("/likeComment")
    public ResponseEntity<Mono<ApiResponse<Object>>> likeComment(
            @RequestParam UUID productId,
            @RequestParam String commentId) {
        final var response = productService.incrementCommentLikeCount(productId, commentId);
        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success, HttpStatus.OK);
    }




    @GetMapping("/getComments/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>> getComments(@PathVariable UUID id)
    {
        final var response = productService.getComments(id);
        return ControllerUtils.mapMonoToResponseEntity(response,ResponseMessage.success,HttpStatus.OK);
    }

}

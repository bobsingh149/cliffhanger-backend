package com.example.barter.controller;

import com.example.barter.dto.input.SaveProductInput;
import com.example.barter.dto.response.ApiResponse;
import com.example.barter.exception.customexception.EmptyQueryException;
import com.example.barter.utils.ControllerUtils;
import com.example.barter.utils.ControllerUtils.ResponseMessage;
import com.example.barter.service.BookService;
import com.example.barter.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    /**
     *
     */
    @Autowired
    public ProductController(BookService productService) {
        this.productService = productService;
    }

    @GetMapping(path ="/getAllProducts")
    public ResponseEntity<Flux<ApiResponse<Object>>>  getAllProduct(@RequestParam int page, @RequestParam int size)
    {
         final var response= productService.getAllByPageable(PageRequest.of(page, size));
         return ControllerUtils.mapFLuxToResponseEntity(response, ResponseMessage.success,HttpStatus.OK);
    }

    @GetMapping(path ="/getProductsByQuery")
    public ResponseEntity<Flux<ApiResponse<Object>>>  getByQuery(@RequestParam String q) throws EmptyQueryException {
        if(q.isBlank() || q.equals("\"\""))
        {
            throw new EmptyQueryException("query parameter cannot be empty");
        }
      final var response =   productService.getByQuery(q);
      return ControllerUtils.mapFLuxToResponseEntity(response,ResponseMessage.success,HttpStatus.OK);
    }

    @GetMapping(path ="/getProductsByFilter")
    public ResponseEntity<Flux<ApiResponse<Object>>>  getByFilter(@RequestParam String subject)
    {
        final var response =   productService.getByFilter(subject);
        return ControllerUtils.mapFLuxToResponseEntity(response,ResponseMessage.success,HttpStatus.OK);
    }


    @GetMapping(path ="/getProductById/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>>  getById(@PathVariable String id)
    {
        final var response= productService.getById(id);
        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success,HttpStatus.OK);
    }

    @GetMapping(path ="/getProductByUserId/{userId}")
    public ResponseEntity<Flux<ApiResponse<Object>>>  getByUserId(@PathVariable UUID userId)
    {
        final var response= productService.getByUserId(userId);

    return ControllerUtils.mapFLuxToResponseEntity(response, ResponseMessage.success,HttpStatus.OK);
    }

    @DeleteMapping(path ="/deleteProductById/{id}")
    public ResponseEntity<Mono<ApiResponse<Object>>>  deleteById(@PathVariable String id)
    {
        final var response= productService.deleteById(id);
        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success,HttpStatus.OK);
    }

    @PostMapping(path = "/saveProduct")
    public ResponseEntity<Mono<ApiResponse<Object>>> saveProduct(@RequestBody SaveProductInput saveProductInput)  {
        final var response = productService.save(saveProductInput);
        return ControllerUtils.mapMonoToResponseEntity(response, ResponseMessage.success,HttpStatus.CREATED);
    }

    @PostMapping(path = "/findByIsbn")
    public ResponseEntity<Mono<ApiResponse<Object>>> findByIsbn(@RequestBody SaveProductInput saveProductInput)
    {
        final var response = productService.findByIsbn(saveProductInput);

        return ControllerUtils.mapMonoToResponseEntity(response,ResponseMessage.success,HttpStatus.OK);
    }

}

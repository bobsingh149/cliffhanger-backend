package com.example.barter.controller;

import com.example.barter.response.Response;
import com.example.barter.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path ="/getAllProducts",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Response<Object>> getAllProduct(@RequestParam("page") int page, @RequestParam("size") int size)
    {
        try {
         final var products=productService.getAllProductsByPageable(PageRequest.of(page, size));
         final Response<Object> response = Response.builder().data(products).message("product saved success").build();
         return new ResponseEntity<>(response,HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            final Response<Object> response = Response.builder().data(null).message(e.getMessage()).build();
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

package com.example.barter;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.json.JSONObject;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {

        JSONObject response = new JSONObject();

        response.put("message", "Hello World!");

        return response.toString();
    }
}

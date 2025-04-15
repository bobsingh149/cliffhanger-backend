package com.example.barter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ConfigurationPropertiesScan
public class BarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarterApplication.class, args);
	}

}

/*

todo
1. add log for the routes it should log status code
2. add spring security
3. multihthreading for the simantanoeus uplaod of 5 images

routes remaining:-
-> home page
-> barter filter
-> search by title, author or subjects
-> liking and commenting
-> getting the comments
-> Great

 */
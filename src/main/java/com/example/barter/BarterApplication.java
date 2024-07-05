package com.example.barter;

import com.example.barter.config.BooksApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({BooksApiProperties.class})
public class BarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarterApplication.class, args);
	}

}

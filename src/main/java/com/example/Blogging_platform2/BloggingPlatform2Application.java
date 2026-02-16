package com.example.Blogging_platform2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BloggingPlatform2Application {

	public static void main(String[] args) {
		SpringApplication.run(BloggingPlatform2Application.class, args);
	}

}


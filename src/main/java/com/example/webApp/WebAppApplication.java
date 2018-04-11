package com.example.webApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class WebAppApplication {
    //change2
	public static void main(String[] args) {
		SpringApplication.run(WebAppApplication.class, args);
	}
}

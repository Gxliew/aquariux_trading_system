package com.example.aquariux_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AquariuxTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AquariuxTestApplication.class, args);
	}

}

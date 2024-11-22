package com.learnbetter.LearnBetterApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class LearnBetterApiApplication {

	public static final String API_PATH = "/api/v1";

	public static void main(String[] args){
		SpringApplication.run(LearnBetterApiApplication.class, args);
	}

}

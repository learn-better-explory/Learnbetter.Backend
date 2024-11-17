package com.learnbetter.LearnBetterApi;

import com.sun.tools.javac.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SpringBootApplication
public class LearnBetterApiApplication {

	public static void main(String[] args){
		SpringApplication.run(LearnBetterApiApplication.class, args);
	}

}

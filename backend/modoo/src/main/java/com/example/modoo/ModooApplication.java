package com.example.modoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication (exclude = SecurityAutoConfiguration.class) // 이게 뭔지 잘 모르겠음
public class ModooApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModooApplication.class, args);
	}

}

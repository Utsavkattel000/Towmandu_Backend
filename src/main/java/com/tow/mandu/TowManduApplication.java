package com.tow.mandu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class TowManduApplication {

	public static void main(String[] args) {
		SpringApplication.run(TowManduApplication.class, args);
	}

}

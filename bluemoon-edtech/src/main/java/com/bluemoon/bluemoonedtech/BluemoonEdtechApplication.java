package com.bluemoon.bluemoonedtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BluemoonEdtechApplication {

	public static void main(String[] args) {
		SpringApplication.run(BluemoonEdtechApplication.class, args);
	}

}

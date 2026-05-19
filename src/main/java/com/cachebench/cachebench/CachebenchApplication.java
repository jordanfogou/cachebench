package com.cachebench.cachebench;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CachebenchApplication {

	public static void main(String[] args) {
		SpringApplication.run(CachebenchApplication.class, args);
	}
}
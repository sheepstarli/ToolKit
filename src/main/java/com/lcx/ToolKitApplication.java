package com.lcx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.lcx")
public class ToolKitApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToolKitApplication.class, args);
	}
}

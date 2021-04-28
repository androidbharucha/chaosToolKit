package org.common.chaos.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.common.chaos")
public class ChaostestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChaostestApplication.class, args).registerShutdownHook();
		
	}

}

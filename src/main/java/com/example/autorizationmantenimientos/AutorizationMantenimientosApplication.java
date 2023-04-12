package com.example.autorizationmantenimientos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AutorizationMantenimientosApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutorizationMantenimientosApplication.class, args);
	}

}

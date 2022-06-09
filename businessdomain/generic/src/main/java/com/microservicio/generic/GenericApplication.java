package com.microservicio.generic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EntityScan(value = { "com.commons.utils.models.entities" })
public class GenericApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenericApplication.class, args);
	}

}

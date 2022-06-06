package com.microservicio.dependencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EntityScan({ "com.commons.utils.models.entities" })
@EnableEurekaClient
public class DependenciaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DependenciaApplication.class, args);
	}

}
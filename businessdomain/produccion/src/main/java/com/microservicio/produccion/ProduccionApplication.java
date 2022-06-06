package com.microservicio.produccion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@EnableEurekaClient
@EntityScan({ "com.commons.utils.models.entities", "com.microservicio.produccion.models.entities" })
public class ProduccionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProduccionApplication.class, args);
	}

}

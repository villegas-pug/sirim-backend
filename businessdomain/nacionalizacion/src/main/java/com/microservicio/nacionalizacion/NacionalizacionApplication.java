package com.microservicio.nacionalizacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@EntityScan(value = { "com.commons.utils.models.entities",  "com.microservicio.nacionalizacion.models.entities"})
public class NacionalizacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(NacionalizacionApplication.class, args);
	}

}

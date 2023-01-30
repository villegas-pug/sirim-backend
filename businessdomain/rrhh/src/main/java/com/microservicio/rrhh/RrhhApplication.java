package com.microservicio.rrhh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EntityScan(value = { "com.commons.utils.models.entities", "com.microservicio.rrhh.models.entities" })
public class RrhhApplication {

	public static void main(String[] args) {
		SpringApplication.run(RrhhApplication.class, args);
	}

}

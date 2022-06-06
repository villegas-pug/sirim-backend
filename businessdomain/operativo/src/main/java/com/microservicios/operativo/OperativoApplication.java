package com.microservicios.operativo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCircuitBreaker
@EnableEurekaClient
@EnableFeignClients
@EntityScan({ "com.commons.utils.models.entities", "com.microservicios.operativo.models.entities" })
@SpringBootApplication
public class OperativoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperativoApplication.class, args);
	}

}

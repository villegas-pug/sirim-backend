package com.microservicios.interpol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@EntityScan({ 
	"com.commons.utils.models.entities",
	"com.microservicios.operativo.models.entities", 
	"com.microservicios.interpol.models.entity" })
public class InterpolApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterpolApplication.class, args);
	}

}

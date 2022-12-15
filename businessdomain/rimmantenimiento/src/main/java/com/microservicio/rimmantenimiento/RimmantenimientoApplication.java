package com.microservicio.rimmantenimiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@EntityScan(value = { "com.commons.utils.models.entities", "com.microservicio.rimmantenimiento.models.entities" })
public class RimmantenimientoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RimmantenimientoApplication.class, args);
	}

}

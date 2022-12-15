package com.microservicio.rimctrlcalidad;

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
@EntityScan(value = { "com.commons.utils.models.entities" })
public class RimctrlcalidadApplication {

	public static void main(String[] args) {
		SpringApplication.run(RimctrlcalidadApplication.class, args);
	}

}

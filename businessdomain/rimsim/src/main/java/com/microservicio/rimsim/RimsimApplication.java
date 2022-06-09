package com.microservicio.rimsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RimsimApplication {

	public static void main(String[] args) {
		SpringApplication.run(RimsimApplication.class, args);
	}

}

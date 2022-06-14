package com.microservicio.rimsim;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@EntityScan(value = {"com.commons.utils.models.entities", "com.microservicio.rimsim.models.entities"})
@SpringBootApplication
public class RimsimApplication {

	public static void main(String[] args) {
		SpringApplication.run(RimsimApplication.class, args);
	}

}

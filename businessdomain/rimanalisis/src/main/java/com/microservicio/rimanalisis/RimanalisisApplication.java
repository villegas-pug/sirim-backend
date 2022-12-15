package com.microservicio.rimanalisis;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@EntityScan(value = { "com.commons.utils.models.entities" })
public class RimanalisisApplication {

	public static void main(String[] args) {
		SpringApplication.run(RimanalisisApplication.class, args);
	}

	@Bean
   public ModelMapper modelMapper(){
      return new ModelMapper();
   }

}

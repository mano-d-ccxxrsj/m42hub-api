package com.m42hub.m42hub_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class M42hubApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(M42hubApiApplication.class, args);
	}

}

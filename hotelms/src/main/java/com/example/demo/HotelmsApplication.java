package com.example.demo;

import java.text.SimpleDateFormat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableEurekaClient
@EnableJms
public class HotelmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelmsApplication.class, args);
	}
	
	@Bean
	public SimpleDateFormat getSimepleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}
}

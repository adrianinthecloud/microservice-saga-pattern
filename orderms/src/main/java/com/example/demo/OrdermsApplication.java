package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.example.demo.model.entity.OrderStatus;
import com.example.demo.repository.OrderRepository;

@SpringBootApplication
@EnableEurekaClient
public class OrdermsApplication {
	@Autowired
	OrderRepository orderRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(OrdermsApplication.class, args);
	}

	@Bean("PENDING")
	public OrderStatus findPendingOrderStatus() {
		Optional<OrderStatus> event = orderRepository.findOrderStatusByName("PENDING");
		return event.isPresent() ? event.get() : null;
	}
	
	@Bean("APPROVED")
	public OrderStatus findApprovedOrderStatus() {
		Optional<OrderStatus> event = orderRepository.findOrderStatusByName("APPROVED");
		return event.isPresent() ? event.get() : null;
	}
	
	@Bean("ABORT")
	public OrderStatus findABORTOrderStatus() {
		Optional<OrderStatus> event = orderRepository.findOrderStatusByName("ABORT");
		return event.isPresent() ? event.get() : null;
	}
	
	@Bean
	public SimpleDateFormat getSimepleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}
}

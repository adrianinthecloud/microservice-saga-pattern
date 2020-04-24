package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.entity.TravelOrder;
import com.example.demo.model.service.OrderDTO;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;

@SpringBootTest
class OrdermsApplicationTests {
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Test
	void contextLoads() {
		OrderDTO orderDTO = OrderDTO.builder()
								.userId(11L)
								.flightNum("CZ325")
								.hotelName("Hilton")
								.carName("BMW")
								.date("2020-03-15")
								.build();
								
		TravelOrder order = orderService.createOrder(orderDTO);
		order.setCarName("BMW 420i");
		
		orderRepository.save(order);
		
		System.out.println("new order = " + order);
	}

}

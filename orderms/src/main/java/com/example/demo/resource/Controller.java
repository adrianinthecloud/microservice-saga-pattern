package com.example.demo.resource;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.model.entity.TravelOrder;
import com.example.demo.model.service.OrderDTO;
import com.example.demo.service.OrderService;

@RestController
public class Controller {
	private final OrderService orderService;
	
	public Controller(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@PostMapping("/order")
	public @ResponseBody ResponseEntity addOrder(@RequestBody OrderDTO orderDTO) {
		TravelOrder order = orderService.createOrder(orderDTO);
		
		if (order != null) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest()
	                .path("/{id}")
	                .buildAndExpand(order.getId())
	                .toUri();
			return ResponseEntity
					.created(location)
					.body(order);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
}

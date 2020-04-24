package com.example.demo.service;

import java.util.Map;

import com.example.demo.model.entity.TravelOrder;
import com.example.demo.model.service.OrderDTO;

public interface OrderService {
	TravelOrder createOrder(OrderDTO orderDTO);
	void confirmOrder(Map<String, String> map);
	void abortOrder(Map<String, String> map);
}

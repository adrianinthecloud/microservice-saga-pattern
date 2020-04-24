package com.example.demo.utils;

import org.modelmapper.PropertyMap;

import com.example.demo.model.entity.TravelOrder;
import com.example.demo.model.service.OrderDTO;

public class OrderMapper extends PropertyMap<OrderDTO, TravelOrder> {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		map().setId(null);
	}
}

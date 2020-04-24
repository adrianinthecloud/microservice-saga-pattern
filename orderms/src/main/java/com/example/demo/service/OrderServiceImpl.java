package com.example.demo.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.OrderStatus;
import com.example.demo.model.entity.TravelOrder;
import com.example.demo.model.service.OrderDTO;
import com.example.demo.repository.OrderRepository;
import com.example.demo.utils.OrderMapper;

import ch.qos.logback.classic.Logger;
import lombok.SneakyThrows;

@Service
public class OrderServiceImpl implements OrderService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(OrderServiceImpl.class);

	private OrderRepository orderRepository;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private SimpleDateFormat sdf;
	
	@Resource(name="PENDING")
	private OrderStatus PENDING_ORDER;
	
	@Resource(name="APPROVED")
	private OrderStatus APPROVED_ORDER;
	
	@Resource(name="ABORT")
	private OrderStatus ABORT_ORDER;
	
	
	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public TravelOrder createOrder(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		logger.info("createOrder(OrderDTO orderDTO) execution");
		ModelMapper modelMapper = new ModelMapper();
		OrderMapper orderMapper = new OrderMapper();
		
		TravelOrder order = modelMapper.addMappings(orderMapper).map(orderDTO);
		order.setStatus(PENDING_ORDER);
		TravelOrder newOrder = orderRepository.save(order);
		
		Map<String, String> payload = new HashMap();
		payload.put("orderId", String.valueOf(newOrder.getId()));
		payload.put("flightNum", newOrder.getFlightNum());
		payload.put("hotelName", newOrder.getHotelName());
		payload.put("carName", newOrder.getCarName());
		payload.put("date", sdf.format(newOrder.getDate()));
		jmsTemplate.convertAndSend("TRAVEL_BOOKING", payload, processor -> {
			processor.setStringProperty("_type", "ORDER_SUBMITTED_EVENT");
			return processor;
		});
		
		return newOrder;
	}

	@Override
	@SneakyThrows
	@JmsListener(destination = "TRAVEL_BOOKING", selector = "_type = 'ORDER_CONFIRMATION'")
	public void confirmOrder(Map<String, String> map) {
		// TODO Auto-generated method stub
		logger.info("cconfirmOrder(Map<String, String> map execution");
		Long orderId = Long.parseLong(map.get("orderId"));
		String flightId = map.get("flightId");
		String hotelId = map.get("hotelId");
		String carId = map.get("carId");
		String sagaId = map.get("sagaId");
		
		Optional<TravelOrder> order = orderRepository.findById(orderId);
		if (order.isPresent()) {
			TravelOrder orderObj = order.get();
			orderObj.setFlightId(Long.parseLong(flightId));
			orderObj.setHotelId(Long.parseLong(hotelId));
			orderObj.setCarId(Long.parseLong(carId));
			orderObj.setStatus(APPROVED_ORDER);
			orderRepository.save(orderObj);
			
			jmsTemplate.convertAndSend("TRAVEL_BOOKING", sagaId, processor -> {
				processor.setStringProperty("_type", "ORDER_SUCCESS");
				return processor;
			});
		} else {
			try {
				throw new Exception("Cannot find order with id: " + orderId);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	@Override
	@SneakyThrows
	@JmsListener(destination = "TRAVEL_BOOKING", selector = "_type = 'ORDER_ABORT'")
	public void abortOrder(Map<String, String> map) {
		// TODO Auto-generated method stub
		logger.info("cconfirmOrder(Map<String, String> map execution");
		Long orderId = Long.parseLong(map.get("orderId"));
		
		Optional<TravelOrder> order = orderRepository.findById(orderId);
		if (order.isPresent()) {
			TravelOrder orderObj = order.get();
			orderObj.setStatus(ABORT_ORDER);
			orderRepository.save(orderObj);
		} else {
			try {
				throw new Exception("Cannot find order with id: " + orderId);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

}

package com.example.demo.utils;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.entity.CarApprovalEvent;
import com.example.demo.model.entity.CarRejectEvent;
import com.example.demo.model.entity.FlightApprovalEvent;
import com.example.demo.model.entity.FlightRejectEvent;
import com.example.demo.model.entity.HotelApprovalEvent;
import com.example.demo.model.entity.HotelRejectEvent;
import com.example.demo.model.entity.OrderSubmittedEvent;
import com.example.demo.model.entity.OrderSuccessEvent;
import com.example.demo.model.entity.Saga;
import com.example.demo.model.entity.SagaEvent;
import com.example.demo.repository.SagaRepository;
import com.example.demo.service.SagaService;
import com.example.demo.service.SagaServiceImpl;

import ch.qos.logback.classic.Logger;

@Component
public class MessageEventMapper {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(MessageEventMapper.class);
	
	@Autowired
	private SagaService sagaService;
	
	public SagaEvent getEventFromMsg(String destination, String type, Map<String, Object> map) {
		logger.info("getEventFromMsg with destination("+destination+"), type("+type+"), map("+map+")");
		
		SagaEvent event = null;
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setSkipNullEnabled(true).setAmbiguityIgnored(true);
		
		Saga saga = null;
		if (map.containsKey("sagaId")) {
			saga = sagaService.findById(Long.parseLong(map.get("sagaId").toString()));
			logger.info("saga = " + saga);
			if (saga == null) {
				logger.error("Cannot find Saga with Id: " + map.get("sagaId"));
				try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}
		
		if (destination.equalsIgnoreCase("TRAVEL_BOOKING")) {
			switch (type) {
			case "ORDER_SUBMITTED_EVENT":
				event = modelMapper.map(map, OrderSubmittedEvent.class);
				break;
			case "FLIGHT_APPROVED_EVENT":
				event = modelMapper.map(map, FlightApprovalEvent.class);
				break;
			case "FLIGHT_REJECT_EVENT":
				logger.info("FLIGHT_REJECT_EVENT map");
				event = new FlightRejectEvent();
				break;
			case "HOTEL_APPROVED_EVENT":
				event = modelMapper.map(map, HotelApprovalEvent.class);
				break;
			case "HOTEL_REJECT_EVENT":
				event = new HotelRejectEvent();
				break;
			case "CAR_APPROVED_EVENT":
				event = modelMapper.map(map, CarApprovalEvent.class);
				break;
			case "CAR_REJECT_EVENT":
				event = new CarRejectEvent();
				break;
			case "ORDER_SUCCESS":
				event = modelMapper.map(map, OrderSuccessEvent.class);
				break;
			default:
				break;
			}
			if (saga != null) {
				logger.info("event = " + event);
				event.setSaga(saga);
			}
		}
		
		return event;
	}
}

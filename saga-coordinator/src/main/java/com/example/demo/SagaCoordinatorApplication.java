package com.example.demo;

import java.awt.Event;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.entity.EventStatus;
import com.example.demo.model.service.HotelDTO;
import com.example.demo.model.service.MyEvent;
import com.example.demo.repository.SagaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@EnableEurekaClient
@EnableJms
@RestController
public class SagaCoordinatorApplication {
	
	@Autowired
	private SagaRepository sagaRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(SagaCoordinatorApplication.class, args);
	}
	
//	@Bean // Serialize message content to json using TextMessage
//	public MessageConverter jacksonJmsMessageConverter() {
//	    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//	    converter.setTargetType(MessageType.TEXT);
//	    converter.setTypeIdPropertyName("_type");
//	    return converter;
//	}
	
//	@Bean // Serialize message content to json using TextMessage
//	public MessageConverter jacksonJmsMessageConverter() {
//		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//		converter.setTargetType(MessageType.TEXT);
//		converter.setTypeIdPropertyName("_type");
//		return converter;
//	}
	
	@Bean("CREATED")
	public EventStatus findCreatedEventStatus() {
		Optional<EventStatus> event = sagaRepository.findEventByName("CREATED");
		return event.isPresent() ? event.get() : null;
	}
	
	@Bean("PENDING")
	public EventStatus findPendingEventStatus() {
		Optional<EventStatus> event = sagaRepository.findEventByName("PENDING");
		return event.isPresent() ? event.get() : null;
	}
	
	@Bean("APPROVED")
	public EventStatus findApprovedEventStatus() {
		Optional<EventStatus> event = sagaRepository.findEventByName("APPROVED");
		return event.isPresent() ? event.get() : null;
	}
	
	@Bean("ABORT")
	public EventStatus findAbortEventStatus() {
		Optional<EventStatus> event = sagaRepository.findEventByName("ABORT");
		return event.isPresent() ? event.get() : null;
	}
	
	@GetMapping("/publish")
	public void publich() {
		Map hotelMap = new HashMap<String, String>();
		hotelMap.put("flightNum", "CZ325");
		hotelMap.put("date", "2020-03-15");
		hotelMap.put("hotelName", "Hilton");
		hotelMap.put("carName", "BMW 420i");
//		jmsTemplate.convertAndSend("TRAVEL_BOOKING", hotelMap, processor -> {
//			processor.setStringProperty("_type", "ORDER_SUBMITTED_EVENT");
//			return processor;
//		});
	}
	
//	@Bean 
//	public MessageConverter converter(){
//		return new SimpleMessageConverter();
//	}
}

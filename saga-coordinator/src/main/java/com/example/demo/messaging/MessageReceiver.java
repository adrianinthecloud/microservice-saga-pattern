package com.example.demo.messaging;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.model.entity.CarApprovalEvent;
import com.example.demo.model.entity.CarBookingEvent;
import com.example.demo.model.entity.CarRejectEvent;
import com.example.demo.model.entity.EventStatus;
import com.example.demo.model.entity.FlightApprovalEvent;
import com.example.demo.model.entity.FlightBookingEvent;
import com.example.demo.model.entity.FlightRejectEvent;
import com.example.demo.model.entity.HotelApprovalEvent;
import com.example.demo.model.entity.HotelBookingEvent;
import com.example.demo.model.entity.HotelRejectEvent;
import com.example.demo.model.entity.OrderBookingEvent;
import com.example.demo.model.entity.OrderSubmittedEvent;
import com.example.demo.model.entity.Saga;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.SagaRepository;
import com.example.demo.service.SagaServiceImpl;

import ch.qos.logback.classic.Logger;

@Component
public class MessageReceiver {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(SagaServiceImpl.class);

	private SagaServiceImpl sagaService;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Resource(name="CREATED")
	private EventStatus CREATED_EVENT;
	
	@Resource(name="PENDING")
	private EventStatus PENDING_EVENT;
	
	@Resource(name="APPROVED")
	private EventStatus APPROVED_EVENT;
	
	@Resource(name="ABORT")
	private EventStatus ABORT_EVENT;
	
	public MessageReceiver(SagaServiceImpl sagaService, EventRepository eventRepository, JmsTemplate jmsTemplate) {
		this.sagaService = sagaService;
		this.eventRepository = eventRepository;
		this.jmsTemplate = jmsTemplate;
	}
	
	@JmsListener(destination = "TRAVEL_BOOKING", selector = "_type = 'ORDER_SUBMITTED_EVENT'")
	public void onEvent(OrderSubmittedEvent event) {
		// TODO Auto-generated method stub
		logger.info("onEvent(OrderSubmittedEvent event): " + event);
		
		Saga saga = Saga.builder()
						.name("TRAVEL_BOOKING")
						.handler("travelBookingService")
						.build();
		
		OrderBookingEvent orderBookingEvent = OrderBookingEvent.builder()
													.name("ORDER_BOOKING")
													.checkInDate(event.getDate())
													.orderId(Long.parseLong(event.getOrderId()))
													.saga(saga)
													.status(CREATED_EVENT)
													.build();
		
		FlightBookingEvent flightBookingEvent = FlightBookingEvent.builder()
													.name("FLIGHT_BOOKING")
													.checkInDate(event.getDate())
													.flightNum(event.getFlightNum())
													.status(PENDING_EVENT)
													.saga(saga)
													.build();
		
		HotelBookingEvent hotelBookingEvent = HotelBookingEvent.builder()
													.name("HOTEL_BOOKING")
													.hotelName(event.getHotelName())
													.checkInDate(event.getDate())
													.status(CREATED_EVENT)
													.saga(saga)
													.build(); 
		
		CarBookingEvent carBookingEvent = CarBookingEvent.builder()
													.name("CAR_BOOKING")
													.carName(event.getCarName())
													.checkInDate(event.getDate())
													.status(CREATED_EVENT)
													.saga(saga)
													.build();
	
		saga.setRequiredEvents(Stream.of(orderBookingEvent, flightBookingEvent, hotelBookingEvent, carBookingEvent).collect(Collectors.toSet()));
		sagaService.save(saga);
		
		eventRepository.saveAll(Arrays.asList(orderBookingEvent, flightBookingEvent, hotelBookingEvent, carBookingEvent));

		try {
			event.setSaga(saga);
			sagaService.apply(saga.getId(), event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@JmsListener(destination = "TRAVEL_BOOKING", selector = "_type = 'FLIGHT_APPROVED_EVENT'")
	public void flightApprove(FlightApprovalEvent event) {
		// TODO Auto-generated method stub
		logger.info("flightApprove(FLIGHT_APPROVED_EVENT event) execution: " + event);
		
		try {
			sagaService.apply(event.getSaga().getId(), event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@JmsListener(destination = "TRAVEL_BOOKING", selector = "_type = 'FLIGHT_REJECT_EVENT'")
	public void flightReject(FlightRejectEvent event) {
		// TODO Auto-generated method stub
		logger.info("flightReject(FlightRejectEvent event) execution: " + event);
		
		try {
			sagaService.apply(event.getSaga().getId(), event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@JmsListener(destination = "TRAVEL_BOOKING", selector = "_type = 'HOTEL_APPROVED_EVENT'")
	public void onEvent(HotelApprovalEvent event) {
		// TODO Auto-generated method stub
		logger.info("onEvent(HotelBookingEvent event) execution: " + event);
		try {
			sagaService.apply(event.getSaga().getId(), event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@JmsListener(destination = "TRAVEL_BOOKING", selector = "_type = 'HOTEL_REJECT_EVENT'")
	public void hotelReject(HotelRejectEvent event) {
		// TODO Auto-generated method stub
		logger.info("hotelReject(HotelRejectEvent event) execution: " + event);
		
		try {
			sagaService.apply(event.getSaga().getId(), event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@JmsListener(destination = "TRAVEL_BOOKING", selector = "_type = 'CAR_APPROVED_EVENT'")
	public void onEvent(CarApprovalEvent event) {
		// TODO Auto-generated method stub
		logger.info("onEvent(CarApprovalEvent event) execution: " + event);
		try {
			sagaService.apply(event.getSaga().getId(), event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@JmsListener(destination = "TRAVEL_BOOKING", selector = "_type = 'CAR_REJECT_EVENT'")
	public void onEvent(CarRejectEvent event) {
		// TODO Auto-generated method stub
		logger.info("onEvent(CarRejectEvent event) execution: " + event);
		try {
			sagaService.apply(event.getSaga().getId(), event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

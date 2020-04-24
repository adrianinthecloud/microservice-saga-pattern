package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

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
import com.example.demo.model.entity.OrderSuccessEvent;
import com.example.demo.model.entity.Saga;
import com.example.demo.model.entity.SagaEvent;
import com.example.demo.repository.EventRepository;

import ch.qos.logback.classic.Logger;

@Service(value = "travelBookingService")
public class TravelBookingService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(TravelBookingService.class);

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Resource(name="CREATED")
	private EventStatus CREATED_EVENT;
	
	@Resource(name="PENDING")
	private EventStatus PENDING_EVENT;
	
	@Resource(name="APPROVED")
	private EventStatus APPROVED_EVENT;
	
	@Resource(name="ABORT")
	private EventStatus ABORT_EVENT;
	
	public void on(OrderSubmittedEvent event) {
		logger.info("on(OrderSubmittedEvent event): " + event);
		
		Map<String, String> payload = new HashMap();
		payload.put("flightNum", event.getFlightNum());
		payload.put("date", event.getDate());
		payload.put("sagaId", String.valueOf(event.getSaga().getId()));
		
		apply("FLIGHT_BOOKING", payload);
	}
	
	public void on(FlightApprovalEvent event) {
		logger.info("on(FlightApprovalEvent event): " + event);
		
		Saga saga = event.getSaga();
		
		saga.getRequiredEvents().stream().forEach(sagaEvent->{
			if (sagaEvent.getName().equalsIgnoreCase("FLIGHT_BOOKING")) {
				FlightBookingEvent flightBooking = (FlightBookingEvent) sagaEvent;
				flightBooking.setStatus(APPROVED_EVENT);
				flightBooking.setFlightId(event.getFlightId());
				flightBooking.setFlightNum(event.getFlightNum());
				eventRepository.save(flightBooking);
			} else if (sagaEvent.getName().equalsIgnoreCase("HOTEL_BOOKING")) {
				HotelBookingEvent hotelBooking = (HotelBookingEvent) sagaEvent;
				hotelBooking.setStatus(PENDING_EVENT);
				eventRepository.save(hotelBooking);
				Map<String, String> payload = new HashMap();
				payload.put("hotelName", hotelBooking.getHotelName());
				payload.put("date", hotelBooking.getCheckInDate());
				payload.put("sagaId", saga.getId().toString());
				apply("HOTEL_BOOKING", payload);
			}
		});
	}
	
	public void on(FlightRejectEvent event) {
		logger.info("on(FlightRejectEvent event): " + event);
		
		compensation(event.getSaga(), event.getName());
	}
	
	public void on(HotelApprovalEvent event) {
		logger.info("on(HotelApprovalEvent event)" + event);
		
		Saga saga = event.getSaga();
		saga.getRequiredEvents().stream().forEach(sagaEvent->{
			if (sagaEvent.getName().equalsIgnoreCase("HOTEL_BOOKING")) {
				HotelBookingEvent hotelBooking = (HotelBookingEvent) sagaEvent;
				hotelBooking.setStatus(APPROVED_EVENT);
				hotelBooking.setHotelId(event.getHotelId());
				eventRepository.save(hotelBooking);
			} else if (sagaEvent.getName().equalsIgnoreCase("CAR_BOOKING")) {
				Map<String, String> payload = new HashMap();
				CarBookingEvent carBooking = (CarBookingEvent) sagaEvent;
				carBooking.setStatus(PENDING_EVENT);
				eventRepository.save(carBooking);
				
				payload.put("carName", carBooking.getCarName());
				payload.put("date", carBooking.getCheckInDate());
				payload.put("sagaId", saga.getId().toString());
				apply("CAR_BOOKING", payload);
			}
		});
	}
	
	public void on(HotelRejectEvent event) {
		logger.info("on(HotelRejectEvent event)" + event);
		
		compensation(event.getSaga(), event.getName());
	}
	
	public void on(CarApprovalEvent event) {
		logger.info("on(CarApprovalEvent event)" + event);
		
		Saga saga = event.getSaga();
		String flightId = "";
		String hotelId = "";
		OrderBookingEvent orderEvent = null;
		for (SagaEvent sagaEvent : saga.getRequiredEvents()) {
			if (sagaEvent.getName().equalsIgnoreCase("FLIGHT_BOOKING")) {
				flightId = ((FlightBookingEvent) sagaEvent).getFlightId().toString();
			} else if (sagaEvent.getName().equalsIgnoreCase("HOTEL_BOOKING")) {
				hotelId = ((HotelBookingEvent) sagaEvent).getHotelId().toString();
			} else if (sagaEvent.getName().equalsIgnoreCase("CAR_BOOKING")) {
				CarBookingEvent carBooking = (CarBookingEvent) sagaEvent;
				carBooking.setStatus(APPROVED_EVENT);
				carBooking.setCarId(event.getCarId());
				eventRepository.save(carBooking);
			} else if (sagaEvent.getName().equalsIgnoreCase("ORDER_BOOKING")) {
				orderEvent = (OrderBookingEvent) sagaEvent;
				orderEvent.setStatus(APPROVED_EVENT);
				eventRepository.save(orderEvent);
			}
		}
		if (orderEvent != null) {
			Map<String, String> payload = new HashMap();
			
			payload.put("orderId", orderEvent.getOrderId().toString());
			payload.put("flightId", flightId);
			payload.put("hotelId", hotelId);
			payload.put("carId", event.getCarId().toString());
			payload.put("sagaId", saga.getId().toString());
			apply("ORDER_CONFIRMATION", payload);
		}
	}
	
	public void on(CarRejectEvent event) {
		logger.info("on(CarRejectEvent event)" + event);
		
		compensation(event.getSaga(), event.getName());
	}
	
	public void on(OrderSuccessEvent event) {
		logger.info("on(OrderSuccessEvent event)" + event);
		
		Saga saga = event.getSaga();
		
		saga.getRequiredEvents().stream().forEach(sagaEvent -> {
			sagaEvent.setStatus(APPROVED_EVENT);
			eventRepository.save(sagaEvent);
		});
	}
	
	
	public void compensation(Saga saga, String rejectEvent) {
			saga.getRequiredEvents().stream().forEach(sagaEvent->{
				if (sagaEvent.getName().equals(rejectEvent)) {
					sagaEvent.setStatus(ABORT_EVENT);
					eventRepository.save(sagaEvent);
				}
				
				logger.info("sagaName = " + sagaEvent.getName() + " rejectEvent = " + rejectEvent);
				
				if (!sagaEvent.getStatus().equals(ABORT_EVENT) && !sagaEvent.getStatus().equals(CREATED_EVENT)) {
					Map<String, String> payload = new HashMap();
					switch (sagaEvent.getName()) {
						case "ORDER_BOOKING":
							payload.put("orderId", String.valueOf(((OrderBookingEvent) sagaEvent).getOrderId()));
							apply("ORDER_ABORT", payload);
							break;
						case "FLIGHT_BOOKING":
							FlightBookingEvent flightBooking = (FlightBookingEvent) sagaEvent;
							payload.put("sagaId", saga.getId().toString());
							payload.put("flightId", flightBooking.getFlightId().toString());
							payload.put("date", flightBooking.getCheckInDate());
							apply("FLIGHT_CANCEL", payload);
							break;
						case "HOTEL_BOOKING":
							HotelBookingEvent hotelBooking = (HotelBookingEvent) sagaEvent;
							payload.put("sagaId", saga.getId().toString());
							payload.put("hotelId", hotelBooking.getHotelId().toString());
							payload.put("date", hotelBooking.getCheckInDate());
							apply("HOTEL_CANCEL", payload);
							break;
						case "CAR_BOOKING":
							CarBookingEvent carBooking = (CarBookingEvent) sagaEvent;
							payload.put("sagaId", saga.getId().toString());
							payload.put("carId", carBooking.getCarId().toString());
							payload.put("date", carBooking.getCheckInDate());
							apply("CAR_CANCEL", payload);
							break;
						default:
					}
				}
				
				sagaEvent.setStatus(ABORT_EVENT);
				eventRepository.save(sagaEvent);
			});
	}
	
	public void apply(String handler, Map<String, String> payload) {
		// TODO Auto-generated method stub
		switch (handler) {
		case "FLIGHT_BOOKING":
			jmsTemplate.convertAndSend("FLIGHT_BOOKING", payload, processor -> {
				processor.setStringProperty("_type", "APPLY_FLIGHT");
				return processor;
			});
			break;
		case "FLIGHT_CANCEL":
			jmsTemplate.convertAndSend("FLIGHT_BOOKING", payload, processor -> {
				processor.setStringProperty("_type", "CANCEL_FLIGHT");
				return processor;
			});
			break;
		case "HOTEL_BOOKING":
			jmsTemplate.convertAndSend("HOTEL_BOOKING", payload, processor -> {
				processor.setStringProperty("_type", "APPLY_HOTEL");
				return processor;
			});
			break;
		case "HOTEL_CANCEL":
			jmsTemplate.convertAndSend("HOTEL_BOOKING", payload, processor -> {
				processor.setStringProperty("_type", "CANCEL_HOTEL");
				return processor;
			});
			break;
		case "CAR_BOOKING":
			jmsTemplate.convertAndSend("CAR_BOOKING", payload, processor -> {
				processor.setStringProperty("_type", "APPLY_CAR");
				return processor;
			});
			break;
		case "CAR_CANCEL":
			jmsTemplate.convertAndSend("CAR_BOOKING", payload, processor -> {
				processor.setStringProperty("_type", "CANCEL_CAR");
				return processor;
			});
			break;
		case "ORDER_CONFIRMATION":
			jmsTemplate.convertAndSend("TRAVEL_BOOKING", payload, processor -> {
				processor.setStringProperty("_type", "ORDER_CONFIRMATION");
				return processor;
			});
			break;
		case "ORDER_ABORT":
			jmsTemplate.convertAndSend("TRAVEL_BOOKING", payload, processor -> {
				processor.setStringProperty("_type", "ORDER_ABORT");
				return processor;
			});
			break;
		default:
			break;
		}
	}
}

package com.example.demo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.model.Flight;
import com.example.demo.repository.FlightRepository;

import ch.qos.logback.classic.Logger;

@Service
public class FlightServiceImpl implements FlightService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(FlightServiceImpl.class);

	private FlightRepository flightRepository;
	private JmsTemplate jmsTemplate;

	public FlightServiceImpl(FlightRepository flightRepository, JmsTemplate jmsTemplate) {
		super();
		this.flightRepository = flightRepository;
		this.jmsTemplate = jmsTemplate;
	}

	@Override
	@JmsListener(destination="FLIGHT_BOOKING", selector = "_type = 'APPLY_FLIGHT'")
	public void flightBooking(Map<String, String> map) {
		// TODO Auto-generated method stub
		logger.info("flightBooking(String, String) execution: " + map);
		String flightNum = map.get("flightNum");
		String sagaId = map.get("sagaId");
		
		Optional<Flight> flight = flightRepository.checkAvailability(flightNum);
		if (flight.isPresent()) {
			// update number of availability
			flightRepository.reduceOneToFlightSeats(flightNum);
			Map<String, String> resultMap = new HashMap();
			resultMap.put("sagaId", sagaId);
			resultMap.put("flightId", String.valueOf(flight.get().getId()));
			resultMap.put("flightNum", flightNum);
			jmsTemplate.convertAndSend("TRAVEL_BOOKING", resultMap, processor -> {
				processor.setStringProperty("_type", "FLIGHT_APPROVED_EVENT");
				return processor;
			});
		} else {
			logger.info("not found");
			jmsTemplate.convertAndSend("TRAVEL_BOOKING", sagaId, processor -> {
				processor.setStringProperty("_type", "FLIGHT_REJECT_EVENT");
				return processor;
			});
		}
	}

	@Override
	@JmsListener(destination="FLIGHT_BOOKING", selector = "_type = 'CANCEL_FLIGHT'")
	public void compensatebooking(Map<String, String> map) {
		// TODO Auto-generated method stub
		logger.info("compensatebooking(String) execution");
		String flightNum = map.get("flightNum");
		flightRepository.addOneToFlightSeats(flightNum);
	}
	
	
}

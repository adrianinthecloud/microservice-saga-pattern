package com.example.demo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Hotel;
import com.example.demo.repository.HotelRepository;

import ch.qos.logback.classic.Logger;
import lombok.SneakyThrows;

@Service
public class HotelServiceImpl implements HotelService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(HotelServiceImpl.class);
	
	private HotelRepository hotelRepository;
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private SimpleDateFormat sdf;
	
	public HotelServiceImpl(HotelRepository hotelRepository, JmsTemplate jmsTemplate) {
		this.hotelRepository = hotelRepository;
		this.jmsTemplate = jmsTemplate;
	}

	@Override
	@JmsListener(destination="HOTEL_BOOKING", selector = "_type = 'APPLY_HOTEL'")
	public void hotelBooking(Map<String, String> map) {
		// TODO Auto-generated method stub
		logger.info("hotelBooking(String, String) execution: " + map);
		// check hotel room availability
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String name = map.get("hotelName");
			String date = map.get("date");
			String sagaId = map.get("sagaId");
			Date checkInDate = sdf.parse(date);
			Optional<Hotel> hotel = hotelRepository.checkAvailability(name, checkInDate);
			if (hotel.isPresent()) {
				// update number of availability
				hotelRepository.reduceOneToAvailability(name, checkInDate);
				Map<String, String> resultMap = new HashMap();
				resultMap.put("sagaId", sagaId);
				resultMap.put("hotelId", String.valueOf(hotel.get().getId()));
				jmsTemplate.convertAndSend("TRAVEL_BOOKING", resultMap, processor -> {
					processor.setStringProperty("_type", "HOTEL_APPROVED_EVENT");
					return processor;
				});
			} else {
				logger.info("not available");
				jmsTemplate.convertAndSend("TRAVEL_BOOKING", sagaId, processor -> {
					processor.setStringProperty("_type", "HOTEL_REJECT_EVENT");
					return processor;
				});
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	@SneakyThrows
	@JmsListener(destination="HOTEL_BOOKING", selector = "_type = 'CANCEL_HOTEL'")
	public void compensatebooking(Map<String, String> map) {
		// TODO Auto-generated method stub
		logger.info("compensatebooking(String) execution");
		String name = map.get("name");
		String checkInDate = map.get("date");
		
		hotelRepository.addOneToAvailability(name, sdf.parse(checkInDate));
	}
}

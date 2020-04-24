package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;

import ch.qos.logback.classic.Logger;

@Service
public class CarServiceImple implements CarService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(CarService.class);

	@Autowired
	private CarRepository carRepository;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Override
	@JmsListener(destination="CAR_BOOKING", selector = "_type = 'APPLY_CAR'")
	public void carBooking(Map<String, String> map) {
		// TODO Auto-generated method stub
		logger.info("carBooking(String, String) execution: " + map);
		String carName = map.get("carName");
		String sagaId = map.get("sagaId");
		
		Optional<Car> car = carRepository.checkAvailability(carName);
		if (car.isPresent()) {
			// update number of availability
			carRepository.reduceOneToCarNumber(carName);
			Map<String, String> resultMap = new HashMap();
			resultMap.put("sagaId", sagaId);
			resultMap.put("carId", String.valueOf(car.get().getId()));
			jmsTemplate.convertAndSend("TRAVEL_BOOKING", resultMap, processor -> {
				processor.setStringProperty("_type", "CAR_APPROVED_EVENT");
				return processor;
			});
		} else {
			logger.info("not found");
			jmsTemplate.convertAndSend("TRAVEL_BOOKING", sagaId, processor -> {
				processor.setStringProperty("_type", "CAR_REJECT_EVENT");
				return processor;
			});
		}
	}

	@Override
	@JmsListener(destination="CAR_BOOKING", selector = "_type = 'CANCEL_CAR'")
	public void compensatebooking(Map<String, String> map) {
		// TODO Auto-generated method stub
		logger.info("compensatebooking(String) execution");
		String carName = map.get("carName");
		carRepository.addOneToCarNumber(carName);
	}

}

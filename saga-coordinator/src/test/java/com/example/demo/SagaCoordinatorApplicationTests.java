package com.example.demo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.entity.CarBookingEvent;
import com.example.demo.model.entity.EventStatus;
import com.example.demo.model.entity.OrderSubmittedEvent;
import com.example.demo.model.entity.Saga;
import com.example.demo.model.entity.SagaEvent;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.SagaRepository;
import com.example.demo.service.SagaService;

@SpringBootTest
class SagaCoordinatorApplicationTests {
	
	@Test
	void contextLoads() {

	}
}

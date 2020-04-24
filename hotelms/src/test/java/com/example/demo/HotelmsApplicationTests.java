package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.repository.HotelRepository;
import com.example.demo.service.HotelServiceImpl;

import lombok.SneakyThrows;

@SpringBootTest
class HotelmsApplicationTests {
	@Autowired
	private HotelRepository hotelRepository;
	
	@Test
	void contextLoads() {
	}

}

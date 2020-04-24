package com.example.demo.service;

import java.util.Date;
import java.util.Map;

public interface HotelService {
	void hotelBooking(Map<String, String> map);

	void compensatebooking(Map<String, String> map);
}

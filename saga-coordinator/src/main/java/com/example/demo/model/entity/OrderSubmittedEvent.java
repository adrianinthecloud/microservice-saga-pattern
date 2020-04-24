package com.example.demo.model.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;



@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode

public class OrderSubmittedEvent extends SagaEvent {
	private String flightNum;
	private String hotelName;
	private String carName;
	private String date;
	private String orderId;
	
	@Builder
	public OrderSubmittedEvent(Long id, String name, Saga saga, EventStatus status,
			String flightNum, String hotelName, String carName, String date, String orderId) {
		super(id, name, saga, status);
		this.flightNum = flightNum;
		this.hotelName = hotelName;
		this.carName = carName;
		this.date = date;
		this.orderId = orderId;
	}
}

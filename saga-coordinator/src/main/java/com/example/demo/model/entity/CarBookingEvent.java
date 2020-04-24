package com.example.demo.model.entity;

import javax.persistence.Column;
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

@Entity
@Table(name="car_booking_event")
public class CarBookingEvent extends SagaEvent {
	@Column(name="check_in_date")
	private String checkInDate;
	
	@Column(name="car_id")
	private Long carId;
	
	@Column(name="car_name")
	private String carName;
	
	@Builder
	public CarBookingEvent(Long id, String name, Saga saga, EventStatus status,
							String checkInDate, Long carId, String carName) {
		super(id, name, saga, status);
		this.checkInDate = checkInDate;
		this.carId = carId;
		this.carName = carName;
	}
}

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
@Table(name="flight_booking_event")
public class FlightBookingEvent extends SagaEvent {
	@Column(name="check_in_date")
	private String checkInDate;

	@Column(name="flight_num")
	private String flightNum;
	
	@Column(name="flight_id")
	private Long flightId;
	
	@Builder
	public FlightBookingEvent(Long id, String name, Saga saga, EventStatus status,
							String checkInDate, String flightNum, Long flightId) {
		super(id, name, saga, status);
		this.checkInDate = checkInDate;
		this.checkInDate = checkInDate;
		this.flightId = flightId;
	}
}

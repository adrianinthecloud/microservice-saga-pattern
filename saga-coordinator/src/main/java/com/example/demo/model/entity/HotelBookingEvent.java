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
@Table(name="hotel_booking_event")
public class HotelBookingEvent extends SagaEvent {
	@Column(name="check_in_date")
	private String checkInDate;

	@Column(name="hotel_id")
	private Long hotelId;
	
	@Column(name="hotel_name")
	private String hotelName;
	
	@Builder
	public HotelBookingEvent(Long id, String name, Saga saga, EventStatus status,
							String checkInDate, Long hotelId, String hotelName) {
		super(id, name, saga, status);
		this.checkInDate = checkInDate;
		this.hotelId = hotelId;
		this.hotelName = hotelName;
	}
}

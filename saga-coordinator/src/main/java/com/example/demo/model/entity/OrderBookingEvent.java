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
@Table(name="order_booking_event")
public class OrderBookingEvent extends SagaEvent {
	@Column(name="check_in_date")
	private String checkInDate;
	
	@Column(name="order_id")
	private Long orderId;
	
	@Builder
	public OrderBookingEvent(Long id, String name, Saga saga,
							EventStatus status, String checkInDate, Long orderId) {
		super(id, name, saga, status);
		this.checkInDate = checkInDate;
		this.orderId = orderId;
	}
}

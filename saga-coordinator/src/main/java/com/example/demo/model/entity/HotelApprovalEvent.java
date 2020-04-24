package com.example.demo.model.entity;

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
public class HotelApprovalEvent extends SagaEvent {
	private Long hotelId;
	
	@Builder
	public HotelApprovalEvent(Long id, String name, Saga saga, 
			EventStatus status, Long hotelId) {
		super(id, name, saga, status);
		this.hotelId = hotelId;
	}
}

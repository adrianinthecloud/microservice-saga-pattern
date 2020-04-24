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
public class FlightApprovalEvent extends SagaEvent {
	private Long flightId;
	private String flightNum;
	
	@Builder
	public FlightApprovalEvent(Long id, String name, Saga saga, 
			EventStatus status, Long flightId, String flightNum) {
		super(id, name, saga, status);
		this.flightId = flightId;
		this.flightNum = flightNum;
	}
}

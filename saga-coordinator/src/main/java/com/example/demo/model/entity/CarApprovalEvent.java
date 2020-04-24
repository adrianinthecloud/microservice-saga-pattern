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
public class CarApprovalEvent extends SagaEvent {
	private Long carId;
	
	@Builder
	public CarApprovalEvent(Long id, String name, Saga saga, 
			EventStatus status, Long carId) {
		super(id, name, saga, status);
		this.carId = carId;
	}
}

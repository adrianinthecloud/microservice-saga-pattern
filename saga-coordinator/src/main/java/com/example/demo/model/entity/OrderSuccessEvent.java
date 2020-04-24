package com.example.demo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode
public class OrderSuccessEvent extends SagaEvent {
	@Builder
	public OrderSuccessEvent(Long id, String name, Saga saga, 
			EventStatus status) {
		super(id, name, saga, status);
	}
}

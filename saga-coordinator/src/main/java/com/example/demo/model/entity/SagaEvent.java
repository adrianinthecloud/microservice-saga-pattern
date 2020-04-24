package com.example.demo.model.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.InheritanceType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
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
@ToString(exclude = {"saga"})
@EqualsAndHashCode(exclude = "saga")

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class SagaEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	private String name;

	@ManyToOne
	@JoinColumn(name="saga_id")
	private Saga saga;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="status_id")
	private EventStatus status;
}

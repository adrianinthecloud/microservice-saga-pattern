package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.EventStatus;
import com.example.demo.model.entity.Saga;

@Repository
public interface SagaRepository extends JpaRepository<Saga, Long> {
	@Query("From EventStatus where name = :name")
	Optional<EventStatus> findEventByName(String name);
}

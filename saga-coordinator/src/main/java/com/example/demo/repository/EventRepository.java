package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.SagaEvent;

@Repository
public interface EventRepository extends JpaRepository<SagaEvent, Integer> {

}

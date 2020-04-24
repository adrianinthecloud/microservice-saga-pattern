package com.example.demo.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
	@Query("From Car c where name = :name and number > 0")
	Optional<Car> checkAvailability(String name);
	
	@Transactional
	@Modifying
	@Query("update Car set number = number-1 where name = :name")
	void reduceOneToCarNumber(String name);
	
	@Transactional
	@Modifying
	@Query("update Car set number = number+1 where name = :name")
	void addOneToCarNumber(String name);
}

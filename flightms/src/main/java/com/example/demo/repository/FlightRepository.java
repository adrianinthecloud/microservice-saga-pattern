package com.example.demo.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
	@Query("From Flight f where flightNum = :flightNum and availableSeats > 0")
	Optional<Flight> checkAvailability(String flightNum);
	
	@Transactional
	@Modifying
	@Query("update Flight set availableSeats = availableSeats-1 where flightNum = :flightNum")
	void reduceOneToFlightSeats(String flightNum);
	
	@Transactional
	@Modifying
	@Query("update Flight set availableSeats = availableSeats+1 where flightNum = :flightNum")
	void addOneToFlightSeats(String flightNum);
}

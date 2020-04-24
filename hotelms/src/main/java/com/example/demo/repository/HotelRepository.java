package com.example.demo.repository;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Hotel;


@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
	@Query("From Hotel where name = :name")
	Optional<Hotel> findByName(String name);
	
	@Query("select h From Hotel h join h.availableDate ha where h.name = :name and ha.checkInDate = :date and ha.availableRooms > 0")
	Optional<Hotel> checkAvailability(String name, Date date);
	
	@Transactional
	@Modifying
	@Query(value="update availability a join hotel h on (a.hotel_id = h.id) set "
			+ "a.available_rooms = a.available_rooms+1 where h.name = :name and a.check_in_date = :date", nativeQuery=true)
	void addOneToAvailability(String name, Date date);
	
	@Transactional
	@Modifying
	@Query(value="update availability a join hotel h on (a.hotel_id = h.id) set "
			+ "a.available_rooms = a.available_rooms-1 where h.name = :name and a.check_in_date = :date", nativeQuery=true)
	void reduceOneToAvailability(String name, Date date);
}

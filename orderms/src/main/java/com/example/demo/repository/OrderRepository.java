package com.example.demo.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.validator.constraints.Mod10Check;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.OrderStatus;
import com.example.demo.model.entity.TravelOrder;

@Repository
public interface OrderRepository extends JpaRepository<TravelOrder, Long> {
	@Query("From OrderStatus where name = :name")
	Optional<OrderStatus> findOrderStatusByName(String name);
}

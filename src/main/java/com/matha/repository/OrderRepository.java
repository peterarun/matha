package com.matha.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.Order;
import com.matha.domain.School;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

//	@Query("Select o.order from OrderItem o, School s where o.order.school=s and s.id=:school.id")
	public List<Order> findAllBySchool(School school);
	
}

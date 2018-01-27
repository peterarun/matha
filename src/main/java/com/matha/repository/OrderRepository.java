package com.matha.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.Order;
import com.matha.domain.Publisher;
import com.matha.domain.School;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

//	@Query("Select o.order from OrderItem o, School s where o.order.school=s and s.id=:school.id")
	public List<Order> findAllBySchool(School school);
	
	@Query("select distinct ord from Order ord, Publisher pub, Book book, OrderItem oDet where book.bookNum = oDet.book.bookNum and ord = oDet.order and pub = book.publisher and pub=?1")
	public Page<Order> fetchOrdersForPublisher(Publisher pub, Pageable pageable);
	
	@Query(value = "SELECT NEXT VALUE FOR SOrderSeq", nativeQuery = true)
	public Long fetchNextSeqVal();

	@Query("select distinct ord from Order ord, Publisher pub, Book book, OrderItem oDet where book = oDet.book and ord = oDet.order and pub = book.publisher and pub=?1")
	public List<Order> fetchOrdersForPublisher(Publisher pub);
}

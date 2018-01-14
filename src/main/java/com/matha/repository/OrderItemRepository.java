package com.matha.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.OrderItem;
import com.matha.domain.Publisher;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

	@Query("select oDet from Publisher pub, Book book, OrderItem oDet, Order ord where book.bookNum = oDet.book.bookNum and ord = oDet.order and pub = book.publisher and pub=?1")
	public List<OrderItem> fetchOrdersForPublisher(Publisher pub);
	
}

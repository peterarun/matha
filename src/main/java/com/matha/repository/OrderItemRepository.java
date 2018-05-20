package com.matha.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.matha.domain.Book;
import com.matha.domain.OrderItem;
import com.matha.domain.Publisher;
import com.matha.domain.School;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

	@Query("select oDet from Publisher pub, Book book, OrderItem oDet, Order ord where book.bookNum = oDet.book.bookNum and ord = oDet.order and pub = book.publisher and pub=?1")
	public List<OrderItem> fetchOrdersForPublisher(Publisher pub);
	
	@Query("select oDet from OrderItem oDet, Order ord where ord = oDet.order and ord.school = ?1")
	public List<OrderItem> fetchOrderItemsForSchool(School school);
	
	@Query("select distinct oDet.book from OrderItem oDet, Order ord where ord = oDet.order and ord.school = ?1")
	public List<Book> fetchBooksForSchool(School school);

}

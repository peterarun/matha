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

//	@Query("Select o from Order o, School s where o.school=:school order by o.orderDate desc")
	public List<Order> findAllBySchoolOrderByOrderDateDesc(School school);
	
	@Query("select distinct ord from Order ord, Publisher pub, Book book, OrderItem oDet where book.bookNum = oDet.book.bookNum and ord = oDet.order and pub = book.publisher and pub=?1")
	public Page<Order> fetchOrdersForPublisher(Publisher pub, Pageable pageable);

	@Query("select distinct ord from Order ord, Publisher pub, Book book, OrderItem oDet where book.bookNum = oDet.book.bookNum and ord = oDet.order and pub = book.publisher and pub=?1 and oDet.purchase is null")
	public Page<Order> fetchUnBilledOrdersForPub(Publisher pub, Pageable pageable);

	@Query(value = "SELECT NEXT VALUE FOR SOrderSeq", nativeQuery = true)
	public Long fetchNextSeqVal();

	@Query("select distinct ord from Order ord, Publisher pub, Book book, OrderItem oDet where book = oDet.book and ord = oDet.order and pub = book.publisher and pub=?1")
	public List<Order> fetchOrdersForPublisher(Publisher pub);
	
	@Query("select distinct ord from Order ord, Publisher pub, Book book, OrderItem oDet where book.bookNum = oDet.book.bookNum and ord = oDet.order and pub = book.publisher and pub=?1 and oDet.purchase is null")
	public List<Order> fetchUnBilledOrdersForPub(Publisher pub);

	@Query("select distinct ord from Order ord, Publisher pub, Book book, OrderItem oDet where book.bookNum = oDet.book.bookNum and ord = oDet.order and pub = book.publisher and pub=?1 and ord.serialNo like ?2%")
	public Page<Order> fetchOrdersForPublisherAndSearchStr(Publisher pub, String searchText, Pageable pageable);

	@Query("select distinct ord from Order ord, Publisher pub, Book book, OrderItem oDet where book.bookNum = oDet.book.bookNum and ord = oDet.order and pub = book.publisher and pub=?1 and ord.serialNo like ?2% and oDet.purchase is null")
	public Page<Order> fetchUnBilledOrdersForPubAndSearchStr(Publisher pub, String searchText, Pageable pageable);

	@Query("select distinct ord from Order ord where ord.serialNo like ?1%")
	public Page<Order> fetchOrdersForSearchStr(String searchText, Pageable pageable);

}

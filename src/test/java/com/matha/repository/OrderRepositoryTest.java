package com.matha.repository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import com.matha.domain.Purchase;
import com.matha.service.SchoolService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.matha.domain.Order;
import com.matha.domain.Publisher;
import com.matha.domain.School;
import com.matha.sales.SalesApplication;

import static org.springframework.data.domain.Sort.Direction.ASC;

@SpringBootTest(classes = SalesApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("vazhakulam")
public class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	PublisherRepository publisherRepository;

	@Autowired
	SchoolService schoolService;

	@Test
	public void testOne()
	{
		Order ord = orderRepository.getOne("SO-0");
		System.out.println(ord);
	}

	@Test
	public void testTwo()
	{
		Publisher pub = publisherRepository.getOne(48);
		int pageNum = 1;
		int ROWS_PER_PAGE = 10;
		Page<Purchase> purchasePages = schoolService.fetchPurchasesForPublisher(pub, pageNum, ROWS_PER_PAGE);
	}

	@Test
	public void testFetchNextSeqVal() {
		System.out.println(orderRepository.fetchNextSeqVal());
	}

	@Test
	public void testPaginatedResult() {
		PageRequest pageable = PageRequest.of(1, 5, Sort.Direction.DESC, "orderDate");
		Publisher pub = publisherRepository.getOne(48);
		Page<Order> orderList = orderRepository.fetchOrdersForPublisher(pub, pageable);
		for(Order o: orderList)
		{
			System.out.println(o);
		}
	}
	
	@Test
	public void testSearchResult() {
//		PageRequest pageable = new PageRequest(0, 5, Sort.Direction.DESC, "orderDate");
		PageRequest pageable = PageRequest.of(0, 5);
		Publisher pub = publisherRepository.getOne(48);
//		Order ord = orderService.findOne("2");
		Page<Order> orderList = orderRepository.fetchUnBilledOrdersForPub(pub, pageable);
		for(Order o: orderList.getContent())
		{
			System.out.println(o);
		}
	}

	@Test
	public void test_fetchUnBilledOrdersForPubAndSearchStr(){
		PageRequest pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "ord.orderDate");
		Publisher pub = publisherRepository.getOne(48);
//		Order ord = orderService.findOne("2");
		Page<Order> orderList = orderRepository.fetchUnBilledOrdersForPubAndSearchStr(pub, "2", pageable);
		for(Order o: orderList.getContent())
		{
			System.out.println(o);
		}
	}
	
	@Test
	public void testOrderData() {
		PageRequest pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "orderDate");
		Publisher pub = publisherRepository.getOne(48);
		School school = null;
		//		Order ord = orderService.findOne("2");
		List<Order> orderList = orderRepository.findAllBySchoolOrderByOrderDateDesc(school);
		for(Order o: orderList)
		{
			System.out.println(o);
		}
	}

	@Test
	public void testOrderFetch() {
		PageRequest pageable = PageRequest.of(0, 15, ASC, "calcSerialNum");

		List<Order> orderList = orderRepository.findOrdersBySerialNoStartingWith("", pageable).getContent();
		for(Order o: orderList)
		{
			System.out.println(o);
		}
	}

	@Test
	public void test_findAllByPublisherAndOrderDateAfter()
	{
		Publisher pub = publisherRepository.getOne(48);
		LocalDate ld = LocalDate.of(2017, Month.OCTOBER, 1);
		Sort sortIn = Sort.by(new Sort.Order(Sort.Direction.DESC, "orderDate"));
		orderRepository.findAllByPublisherAndOrderDateAfter(pub, ld, sortIn);
	}
}

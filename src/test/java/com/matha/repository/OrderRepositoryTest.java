package com.matha.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.matha.domain.Order;
import com.matha.domain.Publisher;
import com.matha.sales.SalesApplication;

@SpringBootTest(classes = SalesApplication.class)
@RunWith(SpringRunner.class)
public class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderService;
	
	@Autowired
	PublisherRepository publisherRepository;
	
	@Test
	public void testFetchNextSeqVal() {
		System.out.println(orderService.fetchNextSeqVal());
	}

	@Test
	public void testPaginatedResult() {
		PageRequest pageable = new PageRequest(1, 5, Sort.Direction.DESC, "orderDate");
		Publisher pub = publisherRepository.findOne("48");
		Page<Order> orderList = orderService.fetchOrdersForPublisher(pub, pageable);
		for(Order o: orderList)
		{
			System.out.println(o);
		}
	}

}

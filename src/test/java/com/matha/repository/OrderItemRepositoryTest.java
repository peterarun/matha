package com.matha.repository;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.matha.domain.Order;
import com.matha.domain.OrderItem;
import com.matha.domain.Publisher;
import com.matha.sales.SalesApplication;

@SpringBootTest(classes = SalesApplication.class)
@RunWith(SpringRunner.class)
public class OrderItemRepositoryTest {
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Autowired
	PublisherRepository publisherRepo;

	@Test
	public void testFetchOrdersForPublisher() {
		Publisher pub = publisherRepo.findOne("48");
		List<OrderItem> data = orderItemRepository.fetchOrdersForPublisher(pub);
		
		System.out.println(data);
		
	}

}

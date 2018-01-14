package com.matha.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.matha.sales.SalesApplication;

@SpringBootTest(classes = SalesApplication.class)
@RunWith(SpringRunner.class)
public class PurchaseRepositoryTest {

	@Autowired
	PurchaseRepository purchaseRepository;
	
	@Test
	public void test() {
		purchaseRepository.findAll();
	}

}

package com.matha.repository;

import com.matha.domain.Purchase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.matha.sales.SalesApplication;

import java.util.List;

@SpringBootTest(classes = SalesApplication.class)
@RunWith(SpringRunner.class)
public class PurchaseRepositoryTest {

	@Autowired
	PurchaseRepository purchaseRepository;
	
	@Test
	public void test() {
		purchaseRepository.findAll();
	}

	@Test
	public void test_findByInvoiceNoLike()
	{
		int page = 0;
		int size = 10;
		PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "purchaseDate");
		String searchStr = "2%";
		List<Purchase> purList = purchaseRepository.findByInvoiceNoLike(searchStr, pageable).getContent();
		for (int i = 0; i < purList.size(); i++)
		{
			System.out.println(purList.get(i));
		}
	}
}

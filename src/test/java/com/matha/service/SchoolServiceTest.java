package com.matha.service;

import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.matha.domain.Order;
import com.matha.domain.OrderItem;
import com.matha.domain.Publisher;
import com.matha.domain.Purchase;
import com.matha.domain.PurchasePayment;
import com.matha.domain.PurchaseTransaction;
import com.matha.domain.School;
import com.matha.repository.OrderItemRepository;
import com.matha.repository.PurchasePayRepository;
import com.matha.repository.PurchaseRepository;
import com.matha.sales.SalesApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SalesApplication.class)
public class SchoolServiceTest {

	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private PurchasePayRepository purchasePayRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Test
	public void testFetchSchoolsLike() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchAllSchools() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertSchool() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchAllStates() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchAllDistricts() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchAllBooks() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveOrder() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchOrderItems() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchOrders() {
		fail("Not yet implemented");
	}

	@Test
	public void testFetchOrderForSchool() {
		School school = schoolService.fetchSchoolById(419);
		List<Order> orderList = schoolService.fetchOrderForSchool(school);
		for (Order order : orderList)
		{
			System.out.println(order);
			System.out.println("OrderItems.................");
			System.out.println(order.getOrderItem());
		}		
	}
	
	@Test
	public void testSavePurchasePay()
	{
		try
		{

			PurchasePayment sPayment = new PurchasePayment();
			
			PurchaseTransaction sTxn = sPayment.getSalesTxn();
			if (sTxn == null)
			{
				sTxn = new PurchaseTransaction();
				Publisher publisher = schoolService.fetchAllPublishers().get(0);
				sTxn.setPublisher(publisher);
			}

			sPayment.setPaymentMode("Test");

			double amountVal = 100.00;
			sTxn.setAmount(amountVal);
			sTxn.setNote("TetsNotes ");
			sTxn.setTxnDate(LocalDate.of(2018, 1, 15));

			schoolService.savePurchasePay(sPayment, sTxn);
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();			
		}
			
	}

	@Test
	public void testUpdatePurchasePay()
	{
		try
		{
			PurchasePayment sPayment = purchasePayRepository.findOne(17);			
			PurchaseTransaction sTxn = sPayment.getSalesTxn();

			sPayment.setPaymentMode("TestUpd II");

			double amountVal = 100.00;
			sTxn.setAmount(amountVal);
			sTxn.setNote("TetsNotes Upd II");
			sTxn.setTxnDate(LocalDate.of(2018, 1, 21));

			schoolService.savePurchasePay(sPayment, sTxn);
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();			
		}
			
	}

	@Test
	public void testSavePurchaseBill()
	{
		try
		{

			Purchase pur = purchaseRepository.findOne("9765");
			
			PurchaseTransaction sTxn = pur.getSalesTxn();
			sTxn.setAmount(2200.00);
			sTxn.setTxnDate(LocalDate.of(2018, 1, 27));
			
			ArrayList<OrderItem> items = new ArrayList<>(pur.getOrderItems()); 
			
			schoolService.savePurchase(pur, items, sTxn);
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();			
		}
			
	}

}

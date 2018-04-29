package com.matha.service;

import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.matha.domain.*;
import com.matha.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import com.matha.sales.SalesApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SalesApplication.class)
public class SchoolServiceTest
{

	private static final Logger LOGGER = LogManager.getLogger(SchoolServiceTest.class);

	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private PurchasePayRepository purchasePayRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private PublisherRepository publisherRepository;

	@Autowired
	private SchoolRepository schoolRepository;

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
	public void testFetchOrders()
	{
		Publisher pub = publisherRepository.findOne(48);
		Page<Order> orders = schoolService.fetchOrders(pub, 1, 5, false);
		for (Order order : orders)
		{
			LOGGER.info(order);
		}
	}

	@Test
	public void test_fetchUnBilledOrders()
	{
		Publisher pub = publisherRepository.findOne(48);
		List<Order> orders = schoolService.fetchUnBilledOrders(pub);
		for (Order order : orders)
		{
			LOGGER.info(order);
		}
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
			
			ArrayList<PurchaseDet> items = new ArrayList<>(pur.getPurchaseItems());
			
			schoolService.savePurchase(pur, items, sTxn);
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();			
		}
			
	}

	@Test
	public void test_fetchBills()
	{
		School school = schoolRepository.findOne(369);
		List<Sales> bills = schoolService.fetchBills(school);
		for (Sales bill : bills) {
			LOGGER.info(bill);
		}
	}
}

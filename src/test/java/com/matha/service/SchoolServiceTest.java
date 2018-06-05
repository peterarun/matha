package com.matha.service;

import static com.matha.util.Utils.calcFinYear;
import static com.matha.util.Utils.getDoubleVal;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.matha.domain.*;
import com.matha.repository.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import com.matha.sales.SalesApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SalesApplication.class)
public class SchoolServiceTest
{

	private static final Logger LOGGER = LogManager.getLogger(SchoolServiceTest.class);

	@Autowired
	private ApplicationContext context;

	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private PurchasePayRepository purchasePayRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private PublisherRepository publisherRepository;

	@Autowired
	private SchoolRepository schoolRepository;

	@Value("#{'${datedPurPaymentModes}'.split(',')}")
	private List<String> datedSchoolPaymentModes;

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
		Page<Order> orders = schoolService.fetchOrders(pub, 1, 5, true);
		LOGGER.info(orders.getTotalPages());
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

	@Test
	public void test_saveSchoolReturn()
	{
		School school = schoolRepository.findOne(359);

		SchoolReturn returnIn = new SchoolReturn();
		returnIn.setId("SR-100");
		SalesTransaction salesTxn = null;
		salesTxn = new SalesTransaction();
		salesTxn.setSchool(school);

		returnIn.setCreditNoteNum("200");
		Set<SalesReturnDet> orderItems = returnIn.getSalesReturnDetSet();
		if(orderItems == null)
		{
			orderItems = new HashSet<>();
		}

		salesTxn.setTxnDate(LocalDate.now());

		String subTotalStr = "0";
		if (StringUtils.isNotBlank(subTotalStr))
		{
			salesTxn.setAmount(Double.parseDouble(subTotalStr));
		}

		schoolService.saveSchoolReturn(returnIn, salesTxn, orderItems);

	}

	@Test
	public void test_SavePurchase()
	{
		try
		{
			SalesApplication.ctx = (ConfigurableApplicationContext) context;
			Publisher publisher = publisherRepository.findOne(48);
			Order orderIn = orderRepository.findOne("SO-765");

			Purchase purchaseIn = null;
			if (purchaseIn == null)
			{
				purchaseIn = new Purchase();
				purchaseIn.setPurchaseDate(LocalDate.now());
				purchaseIn.setFinancialYear(calcFinYear(purchaseIn.getTxnDate()));
				purchaseIn.setSerialNo(this.schoolService.fetchNextPurchaseSerialNum(purchaseIn.getFinancialYear()));
				purchaseIn.setPublisher(publisher);
				purchaseIn.setInvoiceNo("540D");
			}
			PurchaseTransaction salesTxn = purchaseIn.getSalesTxn();
			if (salesTxn == null)
			{
				salesTxn = new PurchaseTransaction();
				salesTxn.setPublisher(publisher);
			}
			purchaseIn.setDiscAmt(0.0);
			purchaseIn.setDiscType(true);
			purchaseIn.setSubTotal(5400.0);
			salesTxn.setAmount(5400.0);
			salesTxn.setTxnDate(LocalDate.now());

			AtomicInteger index = new AtomicInteger();
			List<PurchaseDet> bookList = orderIn.getOrderItem().stream()
					.map(oi -> new PurchaseDet(null,
							null,
							index.incrementAndGet(),
							oi))
					.collect(toList());
			schoolService.savePurchase(purchaseIn, bookList, salesTxn);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}

	@Test
	public void test_Values()
	{
		System.out.println(datedSchoolPaymentModes);
		System.out.println(datedSchoolPaymentModes.contains("DD"));
	}

	@Test
	public void fetchAllPublishers() {
	}

	@Test
	public void savePublisher() {
	}

	@Test
	public void fetchAllBookCategories() {
	}

	@Test
	public void fetchSchoolsLike() {
	}

	@Test
	public void fetchByStateAndPart() {
	}

	@Test
	public void fetchAllSchools() {
	}

	@Test
	public void fetchSchoolById() {
	}

	@Test
	public void saveSchool() {
	}

	@Test
	public void fetchAllStates() {
	}

	@Test
	public void fetchAllDistricts() {
	}

	@Test
	public void fetchAllBooks() {
	}

	@Test
	public void fetchBooksByName() {
	}

	@Test
	public void saveBook() {
	}

	@Test
	public void deleteBook() {
	}

	@Test
	public void saveOrder() {
	}

	@Test
	public void fetchOrderItems() {
	}

	@Test
	public void fetchOrdersForSearchStr() {
	}

	@Test
	public void fetchOrderItemsForPublisher() {
	}

	@Test
	public void fetchOrderItemsForSchool() {
	}

	@Test
	public void fetchBooksForSchool() {
	}

	@Test
	public void fetchOrders() {
	}

	@Test
	public void fetchOrders1() {
	}

	@Test
	public void fetchOrderForSchool() {
	}

	@Test
	public void deleteOrder() {
	}

	@Test
	public void saveOrderItems() {
	}

	@Test
	public void deleteOrderItems() {
	}

	@Test
	public void fetchOrder() {
	}

	@Test
	public void updateOrderData() {
	}

	@Test
	public void test_deleteSchool()
	{
		School sc = schoolRepository.findOne(551);
		List<Sales> bills = schoolService.fetchBills(sc);
		List<SchoolReturn> returns = schoolService.fetchReturnsForSchool(sc);
		List<SchoolPayment> payments = schoolService.fetchPayments(sc);

		List<Order> ordersIn = schoolService.fetchOrderForSchool(sc);
		Set<PurchaseDet> purchasesIn = ordersIn.stream().map(o -> o.getOrderItem()).flatMap(List::stream).map(oi -> oi.getPurchaseDet()).flatMap(Set::stream).collect(toSet());

		schoolService.deleteSchool(sc, ordersIn, bills, returns, payments, purchasesIn);
	}

	@Test
	public void savePurchase() {
	}

	@Test
	public void fetchPurchasesForPublisher() {
	}

	@Test
	public void fetchPurchasesForSearchStr() {
	}

	@Test
	public void deletePurchase() {
	}

	@Test
	public void savePurchaseReturn() {
	}

	@Test
	public void fetchPurchaseReturns() {
	}

	@Test
	public void deletePurchaseReturn() {
	}

	@Test
	public void fetchPurchasePayments() {
	}

	@Test
	public void savePurchasePay() {
	}

	@Test
	public void deletePurchasePayment() {
	}

	@Test
	public void saveCashBook() {
	}

	@Test
	public void getAllTransactions() {
	}

	@Test
	public void deleteTransaction() {
	}

	@Test
	public void fetchPurTransactions() {
	}

	@Test
	public void saveCashHead() {
	}

	@Test
	public void searchTransactions() {
	}

	@Test
	public void fetchOrders2() {
	}

	@Test
	public void fetchOrderSearch() {
	}

	@Test
	public void fetchAllOrders() {
	}

	@Test
	public void fetchUnBilledOrders() {
	}

	@Test
	public void fetchNextSalesInvoiceNum() {
	}

	@Test
	public void fetchNextPurchaseSerialNum() {
	}

	@Test
	public void fetchSale() {
	}

	@Test
	public void saveSalesData() {
	}

	@Test
	public void fetchBills() {
	}

	@Test
	public void fetchBillsForSearchStr() {
	}

	@Test
	public void deleteBill() {
	}

	@Test
	public void savePayment() {
	}

	@Test
	public void fetchPayments() {
	}

	@Test
	public void deletePayment() {
	}

	@Test
	public void fetchTransactions() {
	}

	@Test
	public void saveSchoolReturn() {
	}

	@Test
	public void fetchBalance() {
	}

	@Test
	public void fetchReturnsForSchool() {
	}

	@Test
	public void deleteReturn() {
	}

	@Test
	public void fetchBooksForPublisher() {
	}

	@Test
	public void fetchAddress() {
	}

	@Test
	public void fetchAccount() {
	}

	@Test
	public void fetchAllPurchaseTxnsBetween() {
	}

	@Test
	public void fetchPurchaseTxnsBetween() {
	}

	@Test
	public void fetchSaleTxnsBetween() {
	}
}

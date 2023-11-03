package com.matha.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.matha.domain.*;
import com.matha.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import static com.matha.util.UtilConstants.*;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

@Service
public class SchoolService
{

	private static final Logger LOGGER = LogManager.getLogger(SchoolService.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Value("${matha.orderStartDate}")
	private String orderStartDateStr;

	@Autowired
	SchoolRepository schoolRepoitory;

	@Autowired
	StateRepository stateRepoitory;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	SalesDetRepository salesDetRepository;

	@Autowired
	PurchaseDetRepository purDetRepository;

	@Autowired
	SalesReturnDetRepository salesReturnDetRepository;

	@Autowired
	PurchaseReturnDetRepository purchaseReturnDetRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private PublisherRepository publisherRepository;

	@Autowired
	private PurchaseRepository purchaseRepoitory;

	@Autowired
	private PurchaseDetRepository purchaseDetRepository;

	@Autowired
	private CashBookRepository cashBookRepository;

	@Autowired
	private CashHeadRepository cashHeadRepository;

	@Autowired
	private SalesRepository salesRepository;

	@Autowired
	private SalesTxnRepository salesTxnRepository;

	@Autowired
	private DeletedSalesTxnRepository deletedSalesTxnRepository;

	@Autowired
	private SchoolPayRepository schoolPayRepository;

	@Autowired
	private SchoolAdjustmentRepository schoolAdjustmentRepository;

	@Autowired
	private SchoolReturnRepository schoolReturnRepository;

	@Autowired
	private PurchaseTxnRepository purchaseTxnRepository;

	@Autowired
	private DeletedPurchaseTxnRepository deletedPurchaseTxnRepository;

	@Autowired
	private PurchasePayRepository purchasePayRepository;

	@Autowired
	private PurchaseReturnRepository purchaseReturnRepository;

	@Autowired
	private AddressesRepository addressRepository;

	@Autowired
	private AccountsRepository accountRepository;

	public List<Publisher> fetchAllPublishers()
	{
		return publisherRepository.findAll();
	}

	public void savePublisher(Publisher publisher)
	{
		publisherRepository.save(publisher);
	}

	public List<School> fetchSchoolsLike(String schoolPart)
	{
		return schoolRepoitory.findByNameLike(schoolPart);
	}

	public List<School> fetchByStateAndPart(State state, District district, String schoolPart, String city, String pin)
	{
		return schoolRepoitory.fetchSchools(state, district, schoolPart, city, pin);
	}

	public List<School> fetchAllSchools()
	{
		return schoolRepoitory.findAll();
	}

	public School fetchSchoolById(Integer id)
	{
		return schoolRepoitory.findById(id).orElse(null);
	}

	public School saveSchool(School school)
	{
		return schoolRepoitory.save(school);
	}

	public List<State> fetchAllStates()
	{
		List<State> states = stateRepoitory.findAll();
		for (State state : states)
		{
			state.getDistricts();
		}
		return states;
	}

	public List<District> fetchAllDistricts()
	{
		return districtRepository.findAll();
	}

	public List<Book> fetchAllBooks()
	{
		return bookRepository.findAll();
	}

	public List<Book> fetchBooksByName(String bookName)
	{
		return bookRepository.findAllByNameStartingWith(bookName);
	}

	public List<Book> fetchBooksByNum(String bookNum)
	{
		return bookRepository.findAllByBookNumStartingWith(bookNum);
	}

	public void saveBook(Book bookObj)
	{
		bookRepository.save(bookObj);
	}

	public void deleteBook(Book selectedOrder)
	{
		List<OrderItem> orderItems = orderItemRepository.findAllByBook(selectedOrder);
		List<SalesDet> billItems = salesDetRepository.findAllByBook(selectedOrder);
		List<SalesReturnDet> billRetItems = salesReturnDetRepository.findAllByBook(selectedOrder);
		List<PurchaseDet> purItems = purDetRepository.findAllByBook(selectedOrder);
		List<PurchaseReturnDet> purRetItems = purchaseReturnDetRepository.findAllByBook(selectedOrder);

		for (OrderItem orderItem : orderItems)
		{
			orderItem.setBook(null);
		}
		for (SalesDet orderItem : billItems)
		{
			orderItem.setBook(null);
		}
		for (SalesReturnDet orderItem : billRetItems)
		{
			orderItem.setBook(null);
		}
		for (PurchaseDet orderItem : purItems)
		{
			orderItem.setBook(null);
		}
		for (PurchaseReturnDet orderItem : purRetItems)
		{
			orderItem.setBook(null);
		}

		orderItemRepository.saveAll(orderItems);
		salesDetRepository.saveAll(billItems);
		salesReturnDetRepository.saveAll(billRetItems);
		purDetRepository.saveAll(purItems);
		purchaseReturnDetRepository.saveAll(purRetItems);

		bookRepository.delete(selectedOrder);
	}

	public void saveOrder(Order item)
	{
		orderRepository.save(item);
	}

	public List<OrderItem> fetchOrderItems()
	{
		return orderItemRepository.findAll();
	}

	public Page<Order> fetchOrdersForSearchStr(String searchStr, int page, int size)
	{
//		int page = 0;
//		int size = 10;
//		PageRequest pageable = new PageRequest(page, size, Direction.ASC, "calcSerialNum");
		PageRequest pageable = PageRequest.of(page, size, Direction.DESC, "orderDate");

		return orderRepository.findOrdersBySerialNoStartingWith(searchStr, pageable);
	}

	public List<OrderItem> fetchOrderItemsForPublisher(Publisher pub)
	{
		return orderItemRepository.fetchOrdersForPublisher(pub);
	}

	public List<OrderItem> fetchOrderItemsForSchool(School school)
	{
		return orderItemRepository.fetchOrderItemsForSchool(school);
	}

	public List<Book> fetchBooksForSchool(School school)
	{
		return orderItemRepository.fetchBooksForSchool(school);
	}

	public List<Order> fetchOrders()
	{
		return orderRepository.findAll();
	}

	public List<Order> fetchOrders(List<String> orderIds)
	{
		return orderRepository.findAllById(orderIds);
	}

	public List<Order> fetchOrderForSchool(School school)
	{
		return orderRepository.findAllBySchoolOrderByOrderDateDesc(school);
	}

	public void deleteOrder(Order order)
	{
		List<OrderItem> removedItems = order.getOrderItem();

		// Removing the link to Order Item from Sales Det
		for (OrderItem removedItem : removedItems)
		{
//			Set<SalesDet> saleDets = removedItem.getSalesDet();
			List<SalesDet> saleDets = salesDetRepository.findAllByOrderItem(removedItem);
			for (SalesDet saleDet : saleDets)
			{
				saleDet.setOrderItem(null);
			}
			salesDetRepository.saveAll(saleDets);
		}

		// Removing the link to Order Item from Purchase Det
		for (OrderItem removedItem : removedItems)
		{
//			Set<PurchaseDet> purchaseDets = removedItem.getPurchaseDet();
			List<PurchaseDet> purchaseDets = purDetRepository.findAllByOrderItem(removedItem);
			for (PurchaseDet purchaseDet : purchaseDets)
			{
				purchaseDet.setOrderItem(null);
			}
			purDetRepository.saveAll(purchaseDets);
		}

		orderRepository.delete(order);
	}

	public void saveOrderItems(List<OrderItem> orderItem)
	{
		orderItemRepository.saveAll(orderItem);
	}

	public void deleteOrderItems(List<OrderItem> orderItem)
	{
		orderItemRepository.deleteAll(orderItem);
	}

	public Order fetchOrder(String id)
	{
		return orderRepository.findById(id).orElse(null);
	}

	@Transactional
	public void updateOrderDataOld(Order order,
								List<OrderItem> orderItem,
								List<OrderItem> removedItems,
								Set<SalesTransaction> saleTxns,
								Set<PurchaseTransaction> purTxns)
	{
		if(order.getId() == null)
		{
			orderRepository.save(order);
		}

		for (OrderItem orderItemIn : orderItem)
		{
			orderItemIn.setOrder(order);
		}
		orderItemRepository.saveAll(orderItem);
		orderItemRepository.deleteAll(removedItems);

		for (SalesTransaction saleTxn : saleTxns)
		{
			SalesTransaction prevTxn = fetchPrevTxn(saleTxn);
			updateBalance(prevTxn);
		}

		for (PurchaseTransaction purTxn : purTxns)
		{
			PurchaseTransaction prevTxn = fetchPrevTxn(purTxn);
			updateBalance(prevTxn);
		}
	}

	@Transactional
	public void updateOrderData(Order order,
								List<OrderItem> orderItem,
								List<OrderItem> removedItems)
	{
//		if(order.getId() == null)
		{
			order = orderRepository.save(order);
		}

		for (OrderItem orderItemIn : orderItem)
		{
			orderItemIn.setOrder(order);
		}
		orderItemRepository.saveAll(orderItem);

		// Removing the link to Order Item from Sales Det
		for (OrderItem removedItem : removedItems)
		{
//			Set<SalesDet> saleDets = removedItem.getSalesDet();
			List<SalesDet> saleDets = salesDetRepository.findAllByOrderItem(removedItem);
			for (SalesDet saleDet : saleDets)
			{
				saleDet.setOrderItem(null);
			}
			salesDetRepository.saveAll(saleDets);
		}

		// Removing the link to Order Item from Purchase Det
		for (OrderItem removedItem : removedItems)
		{
//			Set<PurchaseDet> purchaseDets = removedItem.getPurchaseDet();
			List<PurchaseDet> purchaseDets = purDetRepository.findAllByOrderItem(removedItem);
			for (PurchaseDet purchaseDet : purchaseDets)
			{
				purchaseDet.setOrderItem(null);
			}
			purDetRepository.saveAll(purchaseDets);
		}

		orderItemRepository.deleteAll(removedItems);
	}

	@Transactional
	public void deleteSchool(School school, List<Sales> bills, List<SchoolReturn> returns, List<SchoolPayment> payments, List<Order> ordersIn, List<PurchaseDet> purchasesIn)
	{
		Set<SalesTransaction> billTxns = bills.stream().map(Sales::getSalesTxn).filter(st -> st != null).collect(toSet());
		Set<SalesTransaction> returnTxns = returns.stream().map(SchoolReturn::getSalesTxn).filter(st -> st != null).collect(toSet());
		Set<SalesTransaction> paymentTxns = payments.stream().map(SchoolPayment::getSalesTxn).filter(st -> st != null).collect(toSet());
		Set<SalesTransaction> allTxns = new HashSet<>(billTxns);
		allTxns.addAll(returnTxns);
		allTxns.addAll(paymentTxns);

		salesRepository.deleteAll(bills);
		schoolReturnRepository.deleteAll(returns);
		schoolPayRepository.deleteAll(payments);
		salesTxnRepository.deleteAll(allTxns);

		for (PurchaseDet purDet : purchasesIn)
		{
			purDet.setOrderItem(null);
		}
		purDetRepository.saveAll(purchasesIn);

		orderRepository.deleteAll(ordersIn);
		schoolRepoitory.delete(school);
	}

	private PurchaseTransaction updateBalance(PurchaseTransaction txn)
	{
		LOGGER.warn("Updating Balance for {}", txn);
		if (txn == null)
		{
			LOGGER.info("Null PTxn passed in for updating balance");
			return txn;
		}
		PurchaseTransaction prevTxn = this.fetchPrevTxn(txn);
		if (prevTxn != null)
		{
			txn.setBalance(prevTxn.getBalance() + txn.getNetForBalance());
		}
		else
		{
			txn.setBalance(txn.getNetForBalance());
		}
		txn = purchaseTxnRepository.save(txn);

		PurchaseTransaction nextTxn = txn.getNextTxn();
		PurchaseTransaction currTxn = txn;
		while (nextTxn != null)
		{
			nextTxn.setBalance(currTxn.getBalance() + nextTxn.getNetForBalance());
			purchaseTxnRepository.save(nextTxn);
			currTxn = nextTxn;
			nextTxn = currTxn.getNextTxn();
		}
		LOGGER.warn("Balance update completed");
		return txn;
	}

	// private PurchaseTransaction saveNewPurchaseTxnOld(PurchaseTransaction txn)
	// {
	// PurchaseTransaction firstTxn = purchaseTxnRepository.findByPrevTxnIsNull();
	// PurchaseTransaction lastTxn = purchaseTxnRepository.findByNextTxnIsNull();
	// LocalDate txnDate = txn.getTxnDate();
	// if (lastTxn == null || txnDate.isAfter(lastTxn.getTxnDate()) ||
	// txnDate.isEqual(lastTxn.getTxnDate()))
	// {
	// txn = saveFreshTransaction(txn, firstTxn, lastTxn);
	// return txn;
	// }
	//
	// PurchaseTransaction currTxn = lastTxn;
	// while (currTxn != null)
	// {
	// LocalDate currTxnDate = currTxn.getTxnDate();
	// if (txnDate.isAfter(currTxnDate) || txnDate.isEqual(currTxnDate))
	// {
	// txn = saveNewTransactionInBetween(txn, currTxn, currTxn.getNextTxn(),
	// firstTxn, lastTxn);
	// updateBalance(txn);
	// break;
	// }
	// else if (currTxn.getId().equals(firstTxn.getId()))
	// {
	// txn = saveAsFirstTxn(txn, currTxn, firstTxn, lastTxn);
	// updateBalance(txn);
	// break;
	// }
	// currTxn = currTxn.getPrevTxn();
	// }
	//
	// return txn;
	// }

	private PurchaseTransaction saveNewPurchaseTxn(PurchaseTransaction txn)
	{
		PurchaseTransaction firstTxn = purchaseTxnRepository.findByPublisherAndPrevTxnIsNull(txn.getPublisher());
		PurchaseTransaction lastTxn = purchaseTxnRepository.findByPublisherAndNextTxnIsNull(txn.getPublisher());

		if (lastTxn != null)
		{
			lastTxn.setNextTxn(firstTxn);
			lastTxn = purchaseTxnRepository.save(lastTxn);
			purchaseTxnRepository.flush();

			txn.setBalance(lastTxn.getBalance() + txn.getNetForBalance());
			txn.setPrevTxn(lastTxn);
			txn = purchaseTxnRepository.save(txn);

			lastTxn.setNextTxn(txn);
			lastTxn = purchaseTxnRepository.save(lastTxn);
		}
		else
		{
			txn.setBalance(txn.getNetForBalance());
			txn = purchaseTxnRepository.save(txn);
		}
		return txn;
	}

	private PurchaseTransaction saveAsFirstTxn(PurchaseTransaction txn, PurchaseTransaction currTxn, PurchaseTransaction firstTxn, PurchaseTransaction lastTxn)
	{
		firstTxn.setPrevTxn(lastTxn);
		firstTxn = purchaseTxnRepository.save(firstTxn);
		purchaseTxnRepository.flush();

		txn.setNextTxn(firstTxn);
		txn.setBalance(txn.getNetForBalance());
		txn = purchaseTxnRepository.save(txn);

		firstTxn.setPrevTxn(txn);
		firstTxn = purchaseTxnRepository.save(firstTxn);

		return txn;
	}

	private PurchaseTransaction saveNewTransactionInBetween(PurchaseTransaction txn, PurchaseTransaction currTxn, PurchaseTransaction nextTxn, PurchaseTransaction firstTxn,
			PurchaseTransaction lastTxn)
	{
		txn.setPrevTxn(lastTxn);
		txn.setNextTxn(firstTxn);
		txn.setBalance(txn.getNetForBalance());
		txn = purchaseTxnRepository.save(txn);

		currTxn.setNextTxn(txn);
		currTxn = purchaseTxnRepository.save(currTxn);

		nextTxn.setPrevTxn(txn);
		nextTxn = purchaseTxnRepository.save(nextTxn);

		txn.setPrevTxn(currTxn);
		txn.setNextTxn(nextTxn);
		txn = purchaseTxnRepository.save(txn);

		return txn;
	}

	private PurchaseTransaction saveFreshTransaction(PurchaseTransaction txn, PurchaseTransaction firstTxn, PurchaseTransaction lastTxn)
	{
		if (lastTxn == null)
		{
			txn.setBalance(txn.getNetForBalance());
			txn = purchaseTxnRepository.save(txn);
		}
		else
		{
			lastTxn.setNextTxn(firstTxn);
			lastTxn = purchaseTxnRepository.save(lastTxn);
			purchaseTxnRepository.flush();

			txn.setBalance(lastTxn.getBalance() + txn.getNetForBalance());
			txn.setPrevTxn(lastTxn);
			txn = purchaseTxnRepository.save(txn);

			lastTxn.setNextTxn(txn);
			lastTxn = purchaseTxnRepository.save(lastTxn);
		}
		return txn;
	}

	// private PurchaseTransaction updatePurchaseTxnOld(PurchaseTransaction txn)
	// {
	// PurchaseTransaction origTxn = purchaseTxnRepository.findOne(txn.getId());
	// if (origTxn.getTxnDate().equals(txn.getTxnDate()))
	// {
	// LOGGER.info("No Date Update: " + txn);
	// txn = purchaseTxnRepository.save(txn);
	// updateBalance(txn);
	// return txn;
	// }
	// else if (origTxn.getPrevTxn() == null && origTxn.getNextTxn() == null)
	// {
	// LOGGER.info("Update to the one and only txn: " + txn);
	// txn = purchaseTxnRepository.save(txn);
	// return txn;
	// }
	//
	// PurchaseTransaction updateFromTxn = null;
	// PurchaseTransaction firstTxn = purchaseTxnRepository.findByPrevTxnIsNull();
	// PurchaseTransaction lastTxn = purchaseTxnRepository.findByNextTxnIsNull();
	//
	// LocalDate txnDate = txn.getTxnDate();
	// PurchaseTransaction currTxn = lastTxn;
	// while (currTxn != null)
	// {
	// LocalDate currTxnDate = currTxn.getTxnDate();
	// if (txnDate.isAfter(currTxnDate) || txnDate.isEqual(currTxnDate))
	// {
	// updateFromTxn = origTxn.getNextTxn();
	// if (currTxn.getId().equals(lastTxn.getId()))
	// {
	// txn = moveAsLastTxn(txn, origTxn.getPrevTxn(), origTxn.getNextTxn(),
	// firstTxn, lastTxn);
	// updateBalance(updateFromTxn);
	// break;
	// }
	//
	// if (origTxn.getNextTxn().getTxnDate().isAfter(currTxnDate))
	// {
	// updateFromTxn = currTxn;
	// }
	// txn = moveInBetween(txn, origTxn.getPrevTxn(), origTxn.getNextTxn(),
	// firstTxn, lastTxn, currTxn, currTxn.getNextTxn());
	// updateBalance(updateFromTxn);
	// break;
	// }
	// else if (currTxn.getId().equals(firstTxn.getId()))
	// {
	// txn = moveAsFirstTxn(txn, origTxn.getPrevTxn(), origTxn.getNextTxn(),
	// firstTxn, lastTxn);
	// updateBalance(txn);
	// break;
	// }
	// currTxn = currTxn.getPrevTxn();
	// }
	// return txn;
	// }

	private PurchaseTransaction updatePurchaseTxn(PurchaseTransaction txn)
	{
		updateBalance(txn);
		return txn;
	}

	private PurchaseTransaction moveAsLastTxn(PurchaseTransaction txn, PurchaseTransaction fromPrevTxn, PurchaseTransaction fromNextTxn, PurchaseTransaction firstTxn, PurchaseTransaction lastTxn)
	{
		LOGGER.info("Moving as last Txn: " + txn);

		txn.setPrevTxn(lastTxn);
		txn.setNextTxn(firstTxn);
		txn = purchaseTxnRepository.save(txn);

		fromPrevTxn.setNextTxn(fromNextTxn);
		fromPrevTxn = purchaseTxnRepository.save(fromPrevTxn);

		fromNextTxn.setPrevTxn(fromPrevTxn);
		fromNextTxn = purchaseTxnRepository.save(fromNextTxn);

		lastTxn.setNextTxn(txn);
		lastTxn = purchaseTxnRepository.save(lastTxn);

		purchaseTxnRepository.flush();

		txn.setNextTxn(null);
		txn = purchaseTxnRepository.save(txn);

		return txn;
	}

	private PurchaseTransaction moveInBetween(PurchaseTransaction txn, PurchaseTransaction fromPrevTxn, PurchaseTransaction fromNextTxn, PurchaseTransaction firstTxn, PurchaseTransaction lastTxn,
			PurchaseTransaction toPrevTxn, PurchaseTransaction toNextTxn)
	{
		LOGGER.info("Moving In Between: " + txn);

		logId("firstTxn ", firstTxn);
		logId("lastTxn ", lastTxn);
		txn.setPrevTxn(lastTxn);
		txn.setNextTxn(firstTxn);
		txn = purchaseTxnRepository.save(txn);

		logId("fromPrevTxn ", fromPrevTxn);
		fromPrevTxn.setNextTxn(fromNextTxn);
		fromPrevTxn = purchaseTxnRepository.save(fromPrevTxn);

		logId("fromNextTxn ", fromNextTxn);
		fromNextTxn.setPrevTxn(fromPrevTxn);
		fromNextTxn = purchaseTxnRepository.save(fromNextTxn);

		// purchaseTxnRepository.flush();

		logId("toPrevTxn ", toPrevTxn);
		toPrevTxn.setNextTxn(txn);
		toPrevTxn = purchaseTxnRepository.save(toPrevTxn);

		logId("toNextTxn ", toNextTxn);
		toNextTxn.setPrevTxn(txn);
		toNextTxn = purchaseTxnRepository.save(toNextTxn);

		purchaseTxnRepository.flush();

		txn.setPrevTxn(toPrevTxn);
		txn.setNextTxn(toNextTxn);
		txn = purchaseTxnRepository.save(txn);

		return txn;
	}

	private PurchaseTransaction moveAsFirstTxn(PurchaseTransaction txn, PurchaseTransaction fromPrevTxn, PurchaseTransaction fromNextTxn, PurchaseTransaction firstTxn, PurchaseTransaction lastTxn)
	{
		LOGGER.info("Moving as First Txn: " + txn);

		txn.setPrevTxn(lastTxn);
		txn.setNextTxn(firstTxn);
		txn = purchaseTxnRepository.save(txn);

		fromPrevTxn.setNextTxn(fromNextTxn);
		fromPrevTxn = purchaseTxnRepository.save(fromPrevTxn);

		fromNextTxn.setPrevTxn(fromPrevTxn);
		fromNextTxn = purchaseTxnRepository.save(fromNextTxn);

		firstTxn.setPrevTxn(txn);
		firstTxn = purchaseTxnRepository.save(firstTxn);

		purchaseTxnRepository.flush();

		txn.setPrevTxn(null);
		txn = purchaseTxnRepository.save(txn);

		return txn;
	}

	private void deletePurchaseTxn(PurchaseTransaction txn)
	{
		LOGGER.warn("Deleting Purchase Transaction: {}", txn);
		if(txn == null)
		{
			return;
		}

		PurchaseTransaction prevTxn = this.fetchPrevTxn(txn);
		PurchaseTransaction nextTxn = this.fetchNextTxn(txn);
		PurchaseTransaction updateFromTxn = null;

		DeletedPurchaseTransaction deletedTxn = new DeletedPurchaseTransaction();
		BeanUtils.copyProperties(txn, deletedTxn);
		deletedTxn.setPrevTxn(prevTxn == null ? null : prevTxn.getId());
		deletedTxn.setNextTxn(nextTxn == null ? null : nextTxn.getId());
		deletedPurchaseTxnRepository.save(deletedTxn);

		purchaseTxnRepository.delete(txn);
		purchaseTxnRepository.flush();
		LOGGER.warn("Purchase Transaction delete completed; Updating previous/next references");

		if (prevTxn != null)
		{
			updateFromTxn = prevTxn;
			prevTxn.setNextTxn(nextTxn);
			prevTxn = purchaseTxnRepository.save(prevTxn);
		}
		else
		{
			updateFromTxn = nextTxn;
		}

		if (nextTxn != null)
		{
			nextTxn.setPrevTxn(prevTxn);
			nextTxn = purchaseTxnRepository.save(nextTxn);
		}
		LOGGER.warn("previous/next reference update completed; Updating balance");
		updateBalance(updateFromTxn);
	}

	private void logId(String msg, PurchaseTransaction firstTxn)
	{
		if (firstTxn != null)
		{
			LOGGER.info(msg + ": " + firstTxn.getId());
		}
	}

	@Transactional
	public void savePurchase(Purchase pur, List<PurchaseDet> orderItems, PurchaseTransaction txn)
	{
		txn.setPurchase(pur);
		List<PurchaseDet> orderItemsOrig = new ArrayList<>();
		if (txn.getId() == null)
		{
			txn = saveNewPurchaseTxn(txn);
			txn.setPurchase(null);

			pur.setSalesTxn(txn);
			pur = purchaseRepoitory.save(pur);

			txn.setPurchase(pur);
			purchaseTxnRepository.save(txn);

			for (PurchaseDet orderIn : orderItems)
			{
				orderIn.setPurchase(pur);
			}
			purDetRepository.saveAll(orderItems);
			Set<Integer> bookSet = orderItems.stream().map(PurchaseDet::getBook).map(Book::getId).collect(toSet());
			updateBookInventory(orderItemsOrig, new ArrayList<>(orderItems), new ArrayList<>(), new ArrayList<>(), bookSet, Integer.valueOf(1));

		}
		else
		{
			txn = updatePurchaseTxn(txn);

			pur.setSalesTxn(txn);
			pur = purchaseRepoitory.save(pur);

			txn.setPurchase(pur);
			purchaseTxnRepository.save(txn);

			List<Integer> orderIds = pur.getPurchaseItems().stream().map(PurchaseDet::getPurDetId).collect(Collectors.toList());
			orderItemsOrig.addAll(purDetRepository.findAllById(orderIds));

			saveOrderItemUpdates(orderItemsOrig, orderItems, pur);
		}
	}

	private void saveOrderItemUpdates(List<PurchaseDet> ordersOrig,
									  List<PurchaseDet> ordersIn,
									  Purchase pur)
	{
		List<PurchaseDet> removedOrders = new ArrayList<>();
		List<PurchaseDet> addedOrders = new ArrayList<>();
		List<PurchaseDet> affectedOrders = new ArrayList<>();
		Set<Integer> bookNums = new HashSet<>();

		if (ordersOrig != null && !ordersOrig.isEmpty())
		{
			removedOrders.addAll(ordersOrig);
			if (ordersIn != null && !ordersIn.isEmpty())
			{
				affectedOrders.addAll(ordersIn);
				removedOrders.removeAll(ordersIn);
				affectedOrders.removeAll(removedOrders);
			}
			Set<Integer> bookSet = ordersOrig.stream().map(InventoryData::getBook).map(Book::getId).collect(toSet());
			bookNums.addAll(bookSet);
		}

		if (ordersIn != null && !ordersIn.isEmpty())
		{
			addedOrders.addAll(ordersIn);
			if (ordersOrig != null && !ordersOrig.isEmpty())
			{
				addedOrders.removeAll(ordersOrig);
				affectedOrders.removeAll(addedOrders);
			}
			Set<Integer> bookSet = ordersIn.stream().map(InventoryData::getBook).map(Book::getId).collect(toSet());
			bookNums.addAll(bookSet);
		}

		for (PurchaseDet orderIn : addedOrders)
		{
			orderIn.setPurDetId(null);
			orderIn.setPurchase(pur);
		}
		purDetRepository.saveAll(addedOrders);

		for (PurchaseDet orderIn : removedOrders)
		{
			orderIn.setPurchase(null);
		}
		purDetRepository.saveAll(removedOrders);
		purDetRepository.saveAll(affectedOrders);
		updateBookInventory(ordersOrig, addedOrders, removedOrders, affectedOrders, bookNums, Integer.valueOf(1));
	}

	public List<PurchaseDet> fetchPurDetForOrders(List<Order> ordersIn)
	{
		return purDetRepository.findAllByOrderList(ordersIn);
	}

	public List<PurchaseDet> fetchPurDetForOrderItems(List<OrderItem> orderItems)
	{
		return purDetRepository.findAllByOrderItems(orderItems);
	}

	public List<SalesDet> fetchSalesDetForOrderItems(List<OrderItem> orderItems)
	{
		return salesDetRepository.findAllByOrderItems(orderItems);
	}

	public Page<Purchase> fetchActivePurchasesForPublisher(Publisher pub, int page, int size)
	{
		PageRequest pageable = PageRequest.of(page, size, Direction.DESC, "purchaseDate");
		return purchaseRepoitory.findAllActiveByPublisher(pub, pageable);
	}

	public List<PurchaseDet> fetchPurchaseDet(Purchase pur)
	{
		return purchaseDetRepository.findAllByPurchase(pur);
	}

	public Page<Purchase> fetchPurchasesForSearchStr(String searchStr, int page, int size)
	{
//		int page = 0;
//		int size = 10;
		PageRequest pageable = PageRequest.of(page, size, Direction.DESC, "purchaseDate");
		return purchaseRepoitory.findByInvoiceNoLike(searchStr + "%", pageable);
	}

	@Transactional
	public void deletePurchase(Purchase pur)
	{
		LOGGER.warn("Deleting Purchase: {}" + pur);
		double netAmt = pur.getNetAmount();
		PurchaseTransaction txn = pur.getSalesTxn();

		pur.setSalesTxn(null);
//		pur.setPurchaseItems(Collections.EMPTY_SET);
		pur.setDeletedAmt(netAmt);
		pur.setStatusInd(DELETED_IND);
		purchaseRepoitory.save(pur);
		LOGGER.warn("Deleted purchase record; Deleting the associated transaction");

		// Deleting the transaction
		deletePurchaseTxn(txn);
		LOGGER.warn("Purchase delete completed.");
	}

	@Transactional
	public void savePurchaseReturn(PurchaseReturn returnIn, PurchaseTransaction txn, Set<PurchaseReturnDet> orderItems)
	{
		txn.setPurchaseReturn(returnIn);
		if (txn.getId() == null)
		{
			txn = saveNewPurchaseTxn(txn);
		}
		else
		{
			txn = updatePurchaseTxn(txn);
		}

		txn.setPurchaseReturn(null);
		purchaseTxnRepository.save(txn);


		Set<Integer> bookNums = new HashSet<>();
		List<PurchaseReturnDet> origBooks = new ArrayList<>();
		if(returnIn.getId() != null)
		{
			Optional<PurchaseReturn> returnOrig = purchaseReturnRepository.findById(returnIn.getId());
			if(returnOrig.isPresent())
			{
				origBooks = new ArrayList<>(returnOrig.get().getPurchaseReturnDetSet());
			}
		}
		List<PurchaseReturnDet> addedBooks = new ArrayList<>(orderItems);
		addedBooks.removeAll(origBooks);

		List<PurchaseReturnDet> removedBooks = new ArrayList<>(origBooks);
		removedBooks.removeAll(orderItems);

		List<PurchaseReturnDet> affectedBooks = new ArrayList<>(origBooks);
		affectedBooks.retainAll(orderItems);

		Set<Integer> bookSet = origBooks.stream().map(PurchaseReturnDet::getBook).map(Book::getId).collect(toSet());
		bookSet.addAll(addedBooks.stream().map(PurchaseReturnDet::getBook).map(Book::getId).collect(toSet()));

		updateBookInventory(origBooks, addedBooks, removedBooks, affectedBooks, bookSet, Integer.valueOf(-1));

		purchaseReturnDetRepository.deleteAll(removedBooks);

		returnIn.setSalesTxn(txn);
		returnIn = purchaseReturnRepository.save(returnIn);

		txn.setPurchaseReturn(returnIn);
		txn = purchaseTxnRepository.save(txn);

		PurchaseReturn returnFinal = returnIn;
		orderItems.forEach(it -> it.setPurchaseReturn(returnFinal));
		purchaseReturnDetRepository.saveAll(orderItems);

	}

	public List<PurchaseReturn> fetchActivePurchaseReturns(Publisher pub)
	{
		Sort dateSort = Sort.by(new Sort.Order(Direction.DESC, "salesTxn.txnDate"));
		return purchaseReturnRepository.findAllActiveByPublisher(pub, dateSort);
	}

	public PurchaseReturn fetchPurchaseReturn(String creditNoteNum, Integer fy)
	{
		return purchaseReturnRepository.findByCreditNoteNumAndFy(creditNoteNum, fy);
	}

	@Transactional
	public void deletePurchaseReturn(PurchaseReturn pur)
	{
		LOGGER.warn("Deleting Purchase Return: {}" + pur);

		double netAmt = pur.getAmount();
		PurchaseTransaction txn = pur.getSalesTxn();

		pur.setSalesTxn(null);
		pur.setDeletedAmt(netAmt);
		pur.setStatusInd(DELETED_IND);
		purchaseReturnRepository.save(pur);

		LOGGER.warn("Deleted purchase return record; Deleting the associated transaction");

		deletePurchaseTxn(txn);

		LOGGER.warn("Purchase return delete completed.");
	}

	public List<PurchasePayment> fetchActivePurchasePayments(Publisher pub)
	{
		Sort dateSort = Sort.by(new Sort.Order(Direction.DESC, "salesTxn.txnDate"));
		return purchasePayRepository.findAllActiveByPublisher(pub, dateSort);
	}

	@Transactional
	public void savePurchasePay(PurchasePayment sPayment, PurchaseTransaction sTxn)
	{
		sTxn.setPayment(sPayment);
		if (sTxn.getId() == null)
		{
			sTxn = saveNewPurchaseTxn(sTxn);
		}
		else
		{
			sTxn = updatePurchaseTxn(sTxn);
		}

		sPayment.setSalesTxn(sTxn);
		sPayment = purchasePayRepository.save(sPayment);

		sTxn.setPayment(sPayment);
		// updateBalance(sTxn);
		purchaseTxnRepository.save(sTxn);

	}

	@Transactional
	public void deletePurchasePayment(PurchasePayment pur)
	{
		LOGGER.warn("Deleting Purchase payment: {}" + pur);

		double netAmt = pur.getAmount();
		PurchaseTransaction txn = pur.getSalesTxn();

		pur.setSalesTxn(null);
		pur.setDeletedAmt(netAmt);
		pur.setStatusInd(DELETED_IND);
		purchasePayRepository.save(pur);

		LOGGER.warn("Deleted purchase payment record; Deleting the associated transaction");

		deletePurchaseTxn(txn);

		LOGGER.warn("Purchase payment delete completed.");
	}

	public void saveCashBook(CashBook item)
	{
		cashBookRepository.save(item);
	}

	public Integer fetchNextTxnVal()
	{
		return cashBookRepository.fetchNextSerialSeqVal();
	}

	public List<CashBook> getAllTransactions()
	{
		return cashBookRepository.findAllByOrderByTxnDateDesc();
	}

	public void deleteTransaction(CashBook cashHead)
	{
		cashBookRepository.delete(cashHead);
	}

	public List<PurchaseTransaction> fetchPurTransactions(Publisher pub, LocalDate fromDateVal, LocalDate toDateVal, Sort sort)
	{
		return purchaseTxnRepository.findByFromToDate(pub, fromDateVal, toDateVal, sort);
	}

	@Transactional
	public SalesTransaction fetchPrevTxn(SalesTransaction salesTransaction)
	{
		SalesTransaction prevTxn = null;
		Optional<SalesTransaction> currTxn = salesTxnRepository.findById(salesTransaction.getId());
		if(currTxn.isPresent())
		{
			prevTxn = currTxn.get().getPrevTxn();
			Hibernate.initialize(prevTxn);
		}
		return prevTxn;
	}

	@Transactional
	public SalesTransaction fetchNextTxn(SalesTransaction salesTransaction)
	{
		SalesTransaction nextTxn = null;
		Optional<SalesTransaction> currTxn = salesTxnRepository.findById(salesTransaction.getId());
		if(currTxn.isPresent())
		{
			nextTxn = currTxn.get().getNextTxn();
			Hibernate.initialize(nextTxn);
		}
		return nextTxn;
	}

	@Transactional
	public PurchaseTransaction fetchPrevTxn(PurchaseTransaction purchaseTransaction)
	{
		PurchaseTransaction prevTxn = null;
		Optional<PurchaseTransaction> currTxn = purchaseTxnRepository.findById(purchaseTransaction.getId());
		if(currTxn.isPresent())
		{
			prevTxn = currTxn.get().getPrevTxn();
			Hibernate.initialize(prevTxn);
		}
		return prevTxn;
	}

	@Transactional
	public PurchaseTransaction fetchNextTxn(PurchaseTransaction purchaseTransaction)
	{
		PurchaseTransaction nextTxn = null;
		Optional<PurchaseTransaction> currTxn = purchaseTxnRepository.findById(purchaseTransaction.getId());
		if(currTxn.isPresent())
		{
			nextTxn = currTxn.get().getNextTxn();
			Hibernate.initialize(nextTxn);
		}
		return nextTxn;
	}

	public List<CashHead> fetchCashHeads()
	{
		return cashHeadRepository.findAll();
	}

	public void saveCashHead(String cashHeadStr)
	{
		CashHead cashHead = new CashHead();
		cashHead.setCashHeadName(cashHeadStr);
		cashHeadRepository.save(cashHead);
	}

	public List<CashBook> searchTransactions(LocalDate fromDate, LocalDate toDate, String entryDesc, CashHead cashHead)
	{
		List<CashBook> recs = new ArrayList<>();
		if(fromDate != null && toDate != null && entryDesc != null)
		{
			recs = cashBookRepository.fetchCashBookRecords(fromDate, toDate, entryDesc);
		}
		else if(fromDate != null && entryDesc != null)
		{
			recs = cashBookRepository.fetchCashBookRecordsFr(fromDate, entryDesc);
		}
		else if(toDate != null && entryDesc != null)
		{
			recs = cashBookRepository.fetchCashBookRecordsTo(toDate, entryDesc);
		}
		else if(entryDesc != null)
		{
			recs = cashBookRepository.fetchCashBookRecords(entryDesc);
		}
		else if(fromDate != null && toDate != null)
		{
			recs = cashBookRepository.findAllByTxnDateBetweenOrderByIdDesc(fromDate, toDate);
		}
		else
		{
			recs = cashBookRepository.findAllByOrderByTxnDateDesc();
		}

		if(cashHead != null)
		{
			recs = recs.stream().filter(cb -> cb.getTypeVal().equals(cashHead.getCashHeadName())).collect(toList());
		}

		return recs;
	}

	public Page<Order> fetchOrders(Publisher pub, int page, int size, boolean billed)
	{
		Page<Order> orderList = null;
		LocalDate orderStartDate = LocalDate.parse(orderStartDateStr);
		if (billed)
		{
			PageRequest pageable = PageRequest.of(page, size, Direction.DESC, "orderDate");
			orderList = orderRepository.findAllByPublisherAndOrderDateAfter(pub, orderStartDate, pageable);
		}
		else
		{
			PageRequest pageable = PageRequest.of(page, size, Direction.DESC, "oDet.order.orderDate");
			orderList = orderRepository.fetchUnBilledOrdersForPub(pub, pageable);
		}

		return orderList;
	}

	public Page<Order> fetchOrderSearch(Publisher pub, int page, int size, boolean billed, String searchText)
	{
		Page<Order> orderList = null;
		if (billed)
		{
			PageRequest pageable = PageRequest.of(page, size, Direction.DESC, "orderDate");
			orderList = orderRepository.fetchOrdersForPublisherAndSearchStr(pub, searchText, pageable);
		}
		else
		{
			PageRequest pageable = PageRequest.of(page, size, Direction.DESC, "ord.orderDate");
			orderList = orderRepository.fetchUnBilledOrdersForPubAndSearchStr(pub, searchText, pageable);
		}

		for(Order o: orderList.getContent())
		{
			LOGGER.info(o);
		}

		return orderList;
	}


	public List<Order> fetchAllOrders(Publisher pub)
	{
		List<Order> orderList = orderRepository.fetchOrdersForPublisher(pub);
		return orderList;
	}

	public List<Order> fetchUnBilledOrders(Publisher pub)
	{
		List<Order> orderList = orderRepository.fetchUnBilledOrdersForPub(pub);
//		List<Order> orderList = new ArrayList<>();
		return orderList;
	}

	private SalesTransaction updateBalance(SalesTransaction txn)
	{
		LOGGER.warn("Updating Balance for txn: {}", txn);
		if (txn == null)
		{
			LOGGER.info("Null STxn passed in for updating balance");
			return txn;
		}
		SalesTransaction prevTxn = this.fetchPrevTxn(txn);
		if (prevTxn != null)
		{
			txn.setBalance(prevTxn.getBalance() + txn.getNetForBalance());
		}
		else
		{
			txn.setBalance(txn.getNetForBalance());
		}
		txn = salesTxnRepository.save(txn);

		SalesTransaction nextTxn = this.fetchNextTxn(txn);
		SalesTransaction currTxn = txn;
		while (nextTxn != null)
		{
			nextTxn.setBalance(currTxn.getBalance() + nextTxn.getNetForBalance());
			salesTxnRepository.save(nextTxn);
			currTxn = nextTxn;
			nextTxn = this.fetchNextTxn(currTxn);
		}
		LOGGER.warn("Balance updated");

		return txn;
	}

	private SalesTransaction saveNewSalesTxn(SalesTransaction txn)
	{
		SalesTransaction firstTxn = salesTxnRepository.findBySchoolAndPrevTxnIsNull(txn.getSchool());
		SalesTransaction lastTxn = salesTxnRepository.findBySchoolAndNextTxnIsNull(txn.getSchool());

		if (lastTxn != null)
		{
			lastTxn.setNextTxn(firstTxn);
			lastTxn = salesTxnRepository.save(lastTxn);
			salesTxnRepository.flush();

			txn.setBalance(lastTxn.getBalance() + txn.getNetForBalance());
			txn.setPrevTxn(lastTxn);
			txn.setSale(null);
			txn.setSalesReturn(null);
			txn.setPayment(null);
			txn = salesTxnRepository.save(txn);

			lastTxn.setNextTxn(txn);
			lastTxn = salesTxnRepository.save(lastTxn);
		}
		else
		{
			txn.setBalance(txn.getNetForBalance());
			txn.setSale(null);
			txn.setSalesReturn(null);
			txn.setPayment(null);
			txn = salesTxnRepository.save(txn);
		}
		return txn;
	}

	private SalesTransaction updateSalesTxn(SalesTransaction txn)
	{
		updateBalance(txn);
		return txn;
	}

	private void deleteSalesTxn(SalesTransaction txn)
	{
		LOGGER.warn("Deleting Transaction: {}", txn);
		if(txn  == null)
		{
			return;
		}

		LOGGER.info("Type: " + txn.getType() + " InvoiceNum: " + txn.getInvoiceNum());

		SalesTransaction prevTxn = this.fetchPrevTxn(txn);
		SalesTransaction nextTxn = this.fetchNextTxn(txn);
		SalesTransaction updateFromTxn = null;

		DeletedSalesTransaction deletedTxn = new DeletedSalesTransaction();
		BeanUtils.copyProperties(txn, deletedTxn);
		deletedTxn.setPrevTxn(prevTxn == null ? null : prevTxn.getId());
		deletedTxn.setNextTxn(nextTxn == null ? null : nextTxn.getId());
		deletedSalesTxnRepository.save(deletedTxn);

		salesTxnRepository.delete(txn);
		salesTxnRepository.flush();
		LOGGER.warn("Deleted Transaction; Updating Previous/Next references");

		if (prevTxn != null)
		{
			updateFromTxn = prevTxn;
			prevTxn.setNextTxn(nextTxn);
			prevTxn = salesTxnRepository.save(prevTxn);
		}
		else
		{
			updateFromTxn = nextTxn;
		}

		if (nextTxn != null)
		{
			nextTxn.setPrevTxn(prevTxn);
			nextTxn = salesTxnRepository.save(nextTxn);
		}

		LOGGER.warn("Updated references; Updating balance");
		updateBalance(updateFromTxn);
	}
//
//	private void deleteSalesTxnSoft(SalesTransaction txn)
//	{
//		if(txn == null)
//		{
//			LOGGER.info("Null STxn passed in for updating balance");
//			return;
//		}
//		txn.setAmount(0.0);
//		txn.setTxnFlag(DELETED_STR);
//		salesTxnRepository.save(txn);
//
//		updateBalance(txn);
//	}
//
	public Integer fetchNextSalesInvoiceNum(Integer fy)
	{
		return salesRepository.fetchNextSerialSeqVal(fy);
	}

	public Integer fetchNextPurchaseSerialNum(Integer fy)
	{
		return purchaseRepoitory.fetchNextSerialSeqVal(fy);
	}

	public Sales fetchSale(String saleId)
	{
		return salesRepository.findById(saleId).orElse(null);
	}

	@Transactional
	public Sales saveSalesData(Sales pur, List<SalesDet> ordersIn, SalesTransaction txn)
	{
		// correct
		txn.setSale(pur);
		List<SalesDet> ordersOrig = new ArrayList<>();
		if (txn.getId() == null)
		{
			txn = saveNewSalesTxn(txn);

			pur.setSalesTxn(txn);
			pur = salesRepository.save(pur);

			txn.setSale(pur);
			salesTxnRepository.save(txn);

			for (SalesDet orderIn : ordersIn)
			{
				orderIn.setSale(pur);
			}
			salesDetRepository.saveAll(ordersIn);
			Set<Integer> bookSet = ordersIn.stream().map(SalesDet::getBook).map(Book::getId).collect(toSet());
			updateBookInventory(ordersOrig, new ArrayList<>(ordersIn), new ArrayList<>(), new ArrayList<>(), bookSet, Integer.valueOf(-1));
		}
		else
		{
			txn = updateSalesTxn(txn);

			pur.setSalesTxn(txn);
			pur = salesRepository.save(pur);

			txn.setSale(pur);
			salesTxnRepository.save(txn);

			List<Integer> orderItemIds = pur.getSaleItems().stream().map(SalesDet::getSalesDetId).collect(Collectors.toList());
			ordersOrig = salesDetRepository.findAllById(orderItemIds);
			saveSalesDetUpdates(ordersOrig, ordersIn, pur);
		}
		return pur;
	}

	private void saveSalesDetUpdates(List<SalesDet> ordersOrig, List<SalesDet> ordersIn, Sales pur)
	{
		List<SalesDet> removedOrders = new ArrayList<>();
		List<SalesDet> addedOrders = new ArrayList<>();
		List<SalesDet> affectedOrders = new ArrayList<>();
		Set<Integer> bookNums = new HashSet<>();

		if (ordersOrig != null && !ordersOrig.isEmpty())
		{
			removedOrders.addAll(ordersOrig);
			if (ordersIn != null && !ordersIn.isEmpty())
			{
				affectedOrders.addAll(ordersIn);
				removedOrders.removeAll(ordersIn);
				affectedOrders.removeAll(removedOrders);
			}
			Set<Integer> bookSet = ordersOrig.stream().map(SalesDet::getBook).map(Book::getId).collect(toSet());
			bookNums.addAll(bookSet);
		}

		if (ordersIn != null && !ordersIn.isEmpty())
		{
			addedOrders.addAll(ordersIn);
			if (ordersOrig != null && !ordersOrig.isEmpty())
			{
				addedOrders.removeAll(ordersOrig);
				affectedOrders.removeAll(addedOrders);
			}
			Set<Integer> bookSet = ordersIn.stream().map(SalesDet::getBook).map(Book::getId).collect(toSet());
			bookNums.addAll(bookSet);
		}

		Collections.sort(addedOrders, comparing(SalesDet::getSlNum));
		for (SalesDet orderIn : addedOrders)
		{
			orderIn.setSalesDetId(null);
			orderIn.setSale(pur);
		}
		salesDetRepository.saveAll(addedOrders);

		for (SalesDet orderIn : removedOrders)
		{
			orderIn.setSale(null);
		}
		salesDetRepository.saveAll(removedOrders);
		salesDetRepository.saveAll(affectedOrders);
		updateBookInventory(ordersOrig, addedOrders, removedOrders, affectedOrders, bookNums, Integer.valueOf(-1));
	}

	private void updateBookInventory(List<? extends InventoryData> ordersOrig,
									 List<? extends InventoryData> addedOrders,
									 List<? extends InventoryData> removedOrders,
									 List<? extends InventoryData> affectedOrders,
									 Set<Integer> bookNums,
									 Integer multiplier)
	{
		Map<String, Integer> addedCount = addedOrders.stream().collect(groupingBy(id -> id.getBook().getBookNum(), summingInt(InventoryData :: getQuantity)));
		Map<String, Integer> removedCount = removedOrders.stream().collect(groupingBy(id -> id.getBook().getBookNum(), summingInt(InventoryData :: getQuantity)));
		Map<String, Integer> bookCounts = Stream.of(addedCount.entrySet(), removedCount.entrySet()).flatMap(Set::stream).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (ac, rc) -> ac - rc));

		for (InventoryData orderItem : affectedOrders)
		{
			int origItemIdx = ordersOrig.indexOf(orderItem);
			if (origItemIdx > -1)
			{
				InventoryData origItem = ordersOrig.get(origItemIdx);
				int diff = origItem.getQuantity() - orderItem.getQuantity();
				if (bookCounts.containsKey(orderItem.getBook().getBookNum()))
				{
					int bookCnt = bookCounts.get(orderItem.getBook().getBookNum());
					bookCounts.put(orderItem.getBook().getBookNum(), bookCnt + diff);
				}
				else
				{
					bookCounts.put(orderItem.getBook().getBookNum(), diff);
				}
			}
		}

		List<Book> origBooks = bookRepository.findAllById(bookNums);
		for (Book book : origBooks)
		{
			if (bookCounts.containsKey(book.getBookNum()))
			{
				int diff = bookCounts.get(book.getBookNum());
				book.addInventory(diff * multiplier);
			}
		}
		bookRepository.saveAll(origBooks);
	}

	public List<Sales> fetchBills(School school)
	{
//		return salesRepository.findAllByTxnSchool(school);
		return salesRepository.findAllBySchool(school);
	}

	public Page<Sales> fetchBillsForSearchStr(String searchStr, int page, int size)
	{
//		int page = 0;
//		int size = 10;
		PageRequest pageable = PageRequest.of(page, size, Direction.DESC, "txnDate");
		return salesRepository.fetchBySerialNoStartingWith(searchStr, pageable);
	}

	@Transactional
	public void deleteBill(Sales selectedSale)
	{

		Set<SalesDet> ordersOrig = selectedSale.getSaleItems();
		List<SalesDet> ordersIn = new ArrayList<>();
		Set<Integer> bookSet = ordersIn.stream().map(SalesDet::getBook).map(Book::getId).collect(toSet());
		updateBookInventory(new ArrayList<>(ordersOrig), ordersIn, new ArrayList<>(), new ArrayList<>(), bookSet, Integer.valueOf(1));

		SalesTransaction salesTransaction = selectedSale.getSalesTxn();
		double netAmt = selectedSale.getNetAmount();

		selectedSale.setSalesTxn(null);
		selectedSale.setDeletedAmt(netAmt);
		selectedSale.setStatusInd(DELETED_IND);
		salesRepository.save(selectedSale);

		deleteSalesTxn(salesTransaction);
	}

	@Transactional
	public void deleteBillPerm(Sales selectedSale)
	{
		Set<SalesDet> ordersOrig = selectedSale.getSaleItems();
		List<SalesDet> ordersIn = new ArrayList<>();
		Set<Integer> bookSet = ordersIn.stream().map(SalesDet::getBook).map(Book::getId).collect(toSet());
		updateBookInventory(new ArrayList<>(ordersOrig), ordersIn, new ArrayList<>(), new ArrayList<>(), bookSet, Integer.valueOf(1));

		SalesTransaction salesTransaction = selectedSale.getSalesTxn();
		selectedSale.setSalesTxn(null);
		salesRepository.delete(selectedSale);

		deleteSalesTxn(salesTransaction);
	}

	@Transactional
	public void savePayment(SchoolPayment sPayment)
	{
		SalesTransaction txn = sPayment.getSalesTxn();
		txn.setPayment(sPayment);

		if (txn.getId() == null)
		{
			txn = saveNewSalesTxn(txn);
		}
		else
		{
			txn = updateSalesTxn(txn);
		}

		sPayment.setSalesTxn(txn);
		schoolPayRepository.save(sPayment);

		txn.setPayment(sPayment);
		salesTxnRepository.save(txn);

	}


	public List<SchoolPayment> fetchPayments(School school)
	{
		return schoolPayRepository.findAllBySchool(school);
	}

	@Transactional
	public void deletePayment(SchoolPayment selectedPayment)
	{
//		deleteSalesTxn(selectedPayment.getSalesTxn());
//		selectedPayment.setSalesTxn(null);
//		schoolPayRepository.delete(selectedPayment);

		SalesTransaction salesTransaction = selectedPayment.getSalesTxn();
		double netAmt = selectedPayment.getAmount();

		selectedPayment.setSalesTxn(null);
		selectedPayment.setDeletedAmt(netAmt);
		selectedPayment.setStatusInd(DELETED_IND);
		schoolPayRepository.save(selectedPayment);

		deleteSalesTxn(salesTransaction);

	}

	@Transactional
	public void saveAdjustment(SchoolAdjustment sPayment)
	{
		SalesTransaction txn = sPayment.getSalesTxn();
		txn.setAdjustment(sPayment);

		if (txn.getId() == null)
		{
			txn = saveNewSalesTxn(txn);
		}
		else
		{
			txn = updateSalesTxn(txn);
		}

		sPayment.setSalesTxn(txn);
		schoolAdjustmentRepository.save(sPayment);

		txn.setAdjustment(sPayment);
		salesTxnRepository.save(txn);

	}

	public List<SchoolAdjustment> fetchAdjustments(School school)
	{
		return schoolAdjustmentRepository.findAllBySchool(school);
	}

	public List<SalesTransaction> fetchTransactions(School school, LocalDate fromDateVal, LocalDate toDateVal)
	{
		return salesTxnRepository.findByFromToDate(school, fromDateVal, toDateVal);
	}

	@Transactional
	public void saveSchoolReturn(SchoolReturn returnIn, SalesTransaction txn, List<SalesReturnDet> orderItemsIn)
	{
		txn.setSalesReturn(returnIn);
		if (txn.getId() == null)
		{
			txn = saveNewSalesTxn(txn);
		}
		else
		{
			txn = updateSalesTxn(txn);
		}

		List<SalesReturnDet> origBooks = new ArrayList<>();
		List<SalesReturnDet> addedBooks = new ArrayList<>();
		List<SalesReturnDet> affectedBooks = new ArrayList<>();
		List<SalesReturnDet> removedBooks = new ArrayList<>();
		Set<Integer> bookSet = new HashSet<>();
		if(returnIn.getId() == null)
		{
			addedBooks = new ArrayList<>(orderItemsIn);
			Set<Integer> bookNums = addedBooks.stream().map(SalesReturnDet::getBook).map(Book::getId).collect(toSet());
			bookSet.addAll(bookNums);
		}
		else
		{
			Optional<SchoolReturn> salesRet = schoolReturnRepository.findById(returnIn.getId());
			if(salesRet.isPresent())
			{
				origBooks = new ArrayList<>(salesRet.get().getSalesReturnDetSet());
			}
			addedBooks = new ArrayList<>(orderItemsIn);
			addedBooks.removeAll(origBooks);

			removedBooks = new ArrayList<>(origBooks);
			removedBooks.removeAll(orderItemsIn);

			affectedBooks = new ArrayList<>(origBooks);
			affectedBooks.retainAll(orderItemsIn);

			Set<Integer> bookNums = origBooks.stream().map(SalesReturnDet::getBook).map(Book::getId).collect(toSet());
			bookSet.addAll(bookNums);
			bookNums = addedBooks.stream().map(SalesReturnDet::getBook).map(Book::getId).collect(toSet());
			bookSet.addAll(bookNums);
		}

		updateBookInventory(origBooks, addedBooks, removedBooks, affectedBooks, bookSet, Integer.valueOf(1));

		returnIn.setSalesTxn(txn);
		returnIn = schoolReturnRepository.save(returnIn);

		txn.setSalesReturn(returnIn);
		txn = salesTxnRepository.save(txn);

		salesReturnDetRepository.deleteAll(removedBooks);

		final SchoolReturn returnInFinal = returnIn;
		orderItemsIn.forEach(it -> it.setSchoolReturn(returnInFinal));
		salesReturnDetRepository.saveAll(orderItemsIn);

	}

	public Double fetchBalance(School school)
	{
		SalesTransaction sTrans = salesTxnRepository.findBySchoolAndNextTxnIsNull(school);
		if (sTrans == null)
		{
			return 0.0;
		}
		else
		{
			return sTrans.getBalance();
		}
	}

	public List<SchoolReturn> fetchReturnsForSchool(School school)
	{
		return schoolReturnRepository.findAllBySchool(school);
	}

	@Transactional
	public void deleteReturn(SchoolReturn selectedReturn)
	{

//		deleteSalesTxn(selectedReturn.getSalesTxn());
//
//		selectedReturn.setStatusInd(Integer.valueOf(-2));
//		schoolReturnRepository.delete(selectedReturn);

		List<SalesReturnDet> ordersIn = new ArrayList<>(selectedReturn.getSalesReturnDetSet());
		Set<Integer> bookSet = ordersIn.stream().map(SalesReturnDet::getBook).map(Book::getId).collect(toSet());
		updateBookInventory(new ArrayList<>(), new ArrayList<>(), ordersIn, new ArrayList<>(), bookSet, Integer.valueOf(-1));

		SalesTransaction salesTransaction = selectedReturn.getSalesTxn();
		double netAmt = selectedReturn.getNetAmount();

		selectedReturn.setSalesTxn(null);
		selectedReturn.setDeletedAmt(netAmt);
		selectedReturn.setStatusInd(DELETED_IND);
		schoolReturnRepository.save(selectedReturn);

		deleteSalesTxn(salesTransaction);
	}

	public List<Book> fetchBooksForPublisher(Publisher publisher)
	{
		return bookRepository.findAllByPublisher(publisher);
	}

	@Transactional
	public Address fetchAddress(String name)
	{
		Address address = addressRepository.findById(name).orElse(null);
		Hibernate.initialize(address);
		return address;
	}

	public List<PurchaseTransaction> fetchAllPurchaseTxnsBetween(LocalDate fromDateVal, LocalDate toDateVal, Sort sort)
	{
		return purchaseTxnRepository.findByTxnDateBetween(fromDateVal, toDateVal, sort);
	}

	public List<PurchaseTransaction> fetchPurchaseTxnsBetween(LocalDate fromDateVal, LocalDate toDateVal, Optional<String> purTypeStr, Sort sort)
	{
		List<PurchaseTransaction> tableData = new ArrayList<>();
		if(!purTypeStr.isPresent())
		{
			return purchaseTxnRepository.findByTxnDateBetween(fromDateVal, toDateVal, sort);
		}

		switch (purTypeStr.get())
		{
			case PURCHASE_STR:
				tableData = purchaseTxnRepository.findByPurchaseIsNotNullAndTxnDateBetween(fromDateVal, toDateVal, sort);
				break;

			case PAYMENT_STR:
				tableData = purchaseTxnRepository.findByPaymentIsNotNullAndTxnDateBetween(fromDateVal, toDateVal, sort);
				break;

			case RETURN_STR:
				tableData = purchaseTxnRepository.findByPurchaseReturnIsNotNullAndTxnDateBetween(fromDateVal, toDateVal, sort);
				break;

			default:
				tableData = purchaseTxnRepository.findByTxnDateBetween(fromDateVal, toDateVal, sort);
		}
		return tableData;
	}

	public List<SalesTransaction> fetchSaleTxnsBetween(LocalDate fromDateVal, LocalDate toDateVal, Optional<String> purTypeStr, Sort sort)
	{
		List<SalesTransaction> tableData = new ArrayList<>();

		if(!purTypeStr.isPresent())
		{
			return salesTxnRepository.findByTxnDateBetween(fromDateVal, toDateVal, sort);
		}
		switch (purTypeStr.get())
		{
			case SALE_STR:
				tableData = salesTxnRepository.findBySaleIsNotNullAndTxnDateBetween(fromDateVal, toDateVal, sort);
				break;

			case PAYMENT_STR:
				tableData = salesTxnRepository.findByPaymentIsNotNullAndTxnDateBetween(fromDateVal, toDateVal, sort);
				break;

			case CREDIT_NOTE_STR:
				tableData = salesTxnRepository.findBySalesReturnIsNotNullAndTxnDateBetween(fromDateVal, toDateVal, sort);
				break;

			default:
				tableData = salesTxnRepository.findByTxnDateBetween(fromDateVal, toDateVal, sort);
		}
		return tableData;
	}

}

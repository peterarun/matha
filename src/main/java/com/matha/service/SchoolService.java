package com.matha.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.matha.domain.Account;
import com.matha.domain.Address;
import com.matha.domain.Book;
import com.matha.domain.BookCategory;
import com.matha.domain.CashBook;
import com.matha.domain.CashHead;
import com.matha.domain.District;
import com.matha.domain.Order;
import com.matha.domain.OrderItem;
import com.matha.domain.Publisher;
import com.matha.domain.Purchase;
import com.matha.domain.PurchasePayment;
import com.matha.domain.PurchaseReturn;
import com.matha.domain.PurchaseTransaction;
import com.matha.domain.Sales;
import com.matha.domain.SalesTransaction;
import com.matha.domain.School;
import com.matha.domain.SchoolPayment;
import com.matha.domain.SchoolReturn;
import com.matha.domain.State;
import com.matha.repository.AccountsRepository;
import com.matha.repository.AddressesRepository;
import com.matha.repository.BookRepository;
import com.matha.repository.CashBookRepository;
import com.matha.repository.CashHeadRepository;
import com.matha.repository.CategoryRepository;
import com.matha.repository.DistrictRepository;
import com.matha.repository.OrderItemRepository;
import com.matha.repository.OrderRepository;
import com.matha.repository.PublisherRepository;
import com.matha.repository.PurchasePayRepository;
import com.matha.repository.PurchaseRepository;
import com.matha.repository.PurchaseReturnRepository;
import com.matha.repository.PurchaseTxnRepository;
import com.matha.repository.SalesRepository;
import com.matha.repository.SalesTxnRepository;
import com.matha.repository.SchoolPayRepository;
import com.matha.repository.SchoolRepository;
import com.matha.repository.SchoolReturnRepository;
import com.matha.repository.StateRepository;

@Service
public class SchoolService
{

	private static final Logger LOGGER = LogManager.getLogger(SchoolService.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	SchoolRepository schoolRepoitory;

	@Autowired
	StateRepository stateRepoitory;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private PublisherRepository publisherRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private PurchaseRepository purchaseRepoitory;

	@Autowired
	private CashBookRepository cashBookRepository;

	@Autowired
	private CashHeadRepository cashHeadRepository;

	@Autowired
	private SalesRepository salesRepository;

	@Autowired
	private SalesTxnRepository salesTxnRepository;

	@Autowired
	private SchoolPayRepository schoolPayRepository;

	@Autowired
	private SchoolReturnRepository schoolReturnRepository;

	@Autowired
	private PurchaseTxnRepository purchaseTxnRepository;

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

	public List<BookCategory> fetchAllBookCategories()
	{
		return categoryRepository.findAll();
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
		return schoolRepoitory.findOne(id);
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

	public void saveBook(Book bookObj)
	{
		bookRepository.save(bookObj);
	}

	public void deleteBook(Book selectedOrder)
	{
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
		return orderRepository.findAll(orderIds);
	}

	public List<Order> fetchOrderForSchool(School school)
	{
		return orderRepository.findAllBySchoolOrderByOrderDateDesc(school);
	}

	public void deleteOrder(Order order)
	{
		orderRepository.delete(order);
	}

	public void saveOrderItems(List<OrderItem> orderItem)
	{
		orderItemRepository.save(orderItem);
	}

	public void deleteOrderItems(List<OrderItem> orderItem)
	{
		orderItemRepository.delete(orderItem);
	}

	@Transactional
	public void updateOrderData(Order order, List<OrderItem> orderItem, List<OrderItem> removedItems)
	{
		for (OrderItem orderItemIn : orderItem)
		{
			orderItemIn.setOrder(order);
		}
		orderItemRepository.save(orderItem);
		orderItemRepository.delete(removedItems);
		orderRepository.save(order);
	}

	public void deleteSchool(School school)
	{
		schoolRepoitory.delete(school);
	}

	private PurchaseTransaction updateBalance(PurchaseTransaction txn)
	{
		if (txn == null)
		{
			LOGGER.info("Null PTxn passed in for updating balance");
			return txn;
		}
		PurchaseTransaction prevTxn = txn.getPrevTxn();
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
		PurchaseTransaction prevTxn = txn.getPrevTxn();
		PurchaseTransaction nextTxn = txn.getNextTxn();
		PurchaseTransaction updateFromTxn = null;

		purchaseTxnRepository.delete(txn);
		purchaseTxnRepository.flush();
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
	public void savePurchase(Purchase pur, List<OrderItem> orderItems, PurchaseTransaction txn)
	{
		txn.setPurchase(pur);
		List<OrderItem> orderItemsOrig = new ArrayList<>();
		if (txn.getId() == null)
		{
			txn = saveNewPurchaseTxn(txn);

			pur.setSalesTxn(txn);
			pur = purchaseRepoitory.save(pur);

			txn.setPurchase(pur);
			purchaseTxnRepository.save(txn);

			for (OrderItem orderIn : orderItems)
			{
				orderIn.setPurchase(pur);
			}
			orderItemRepository.save(orderItems);
			Set<String> bookSet = orderItems.stream().map(OrderItem::getBook).map(Book::getBookNum).collect(Collectors.toSet());
			updateBookInventory(true, orderItemsOrig, bookSet, new ArrayList<>(orderItems), new ArrayList<>(), new ArrayList<>());

		}
		else
		{
			txn = updatePurchaseTxn(txn);

			pur.setSalesTxn(txn);
			pur = purchaseRepoitory.save(pur);

			txn.setPurchase(pur);
			purchaseTxnRepository.save(txn);

			List<Integer> orderIds = pur.getOrderItems().stream().map(OrderItem::getId).collect(Collectors.toList());
			orderItemsOrig.addAll(orderItemRepository.findAll(orderIds));

			saveOrderItemUpdates(orderItemsOrig, orderItems, pur);
		}
	}

	private void saveOrderItemUpdates(List<OrderItem> ordersOrig, List<OrderItem> ordersIn, Purchase pur)
	{
		List<OrderItem> removedOrders = new ArrayList<>();
		List<OrderItem> addedOrders = new ArrayList<>();
		List<OrderItem> affectedOrders = new ArrayList<>();
		Set<String> bookNums = new HashSet<>();

		if (ordersOrig != null && !ordersOrig.isEmpty())
		{
			removedOrders.addAll(ordersOrig);
			if (ordersIn != null && !ordersIn.isEmpty())
			{
				affectedOrders.addAll(ordersIn);
				removedOrders.removeAll(ordersIn);
				affectedOrders.removeAll(removedOrders);
			}
			Set<String> bookSet = ordersOrig.stream().map(OrderItem::getBook).map(Book::getBookNum).collect(Collectors.toSet());
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
			Set<String> bookSet = ordersIn.stream().map(OrderItem::getBook).map(Book::getBookNum).collect(Collectors.toSet());
			bookNums.addAll(bookSet);
		}

		for (OrderItem orderIn : addedOrders)
		{
			orderIn.setId(null);
			orderIn.setPurchase(pur);
		}
		orderItemRepository.save(addedOrders);

		for (OrderItem orderIn : removedOrders)
		{
			orderIn.setPurchase(null);
		}
		orderItemRepository.save(removedOrders);
		orderItemRepository.save(affectedOrders);
		updateBookInventory(true, ordersOrig, bookNums, addedOrders, removedOrders, affectedOrders);
	}

	public List<Purchase> fetchPurchasesForPublisher(Publisher pub)
	{
		return purchaseRepoitory.findAllByPublisher(pub);
	}

	@Transactional
	public void deletePurchase(Purchase pur)
	{
		PurchaseTransaction txn = pur.getSalesTxn();
		Set<OrderItem> orderItems = pur.getOrderItems();
		for (OrderItem orderItem : orderItems)
		{
			orderItem.setPurchase(null);
		}
		orderItemRepository.save(orderItems);
		purchaseRepoitory.delete(pur);
		deletePurchaseTxn(txn);

	}

	@Transactional
	public void savePurchaseReturn(PurchaseReturn returnIn, PurchaseTransaction txn, Set<OrderItem> orderItems)
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

		returnIn.setSalesTxn(txn);
		returnIn = purchaseReturnRepository.save(returnIn);

		txn.setPurchaseReturn(returnIn);
		txn = purchaseTxnRepository.save(txn);

		PurchaseReturn returnFinal = returnIn;
		orderItems.forEach(it -> it.setPurchReturn(returnFinal));
		orderItemRepository.save(orderItems);

	}

	public List<PurchaseReturn> fetchPurchaseReturns(Publisher pub)
	{
		Sort dateSort = new Sort(new Sort.Order(Direction.DESC, "salesTxn.txnDate"));
		return purchaseReturnRepository.findAllByPublisher(pub, dateSort);
	}

	@Transactional
	public void deletePurchaseReturn(PurchaseReturn purchase)
	{
		PurchaseTransaction txn = purchase.getSalesTxn();
		purchaseReturnRepository.delete(purchase);

		deletePurchaseTxn(txn);

	}

	public List<PurchasePayment> fetchPurchasePayments(Publisher pub)
	{
		Sort dateSort = new Sort(new Sort.Order(Direction.DESC, "salesTxn.txnDate"));
		return purchasePayRepository.findAllByPublisher(pub, dateSort);
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

	public void deletePurchasePayment(PurchasePayment pur)
	{

		PurchaseTransaction txn = pur.getSalesTxn();
		purchasePayRepository.delete(pur);
		deletePurchaseTxn(txn);

	}

	public void saveCashBook(CashBook item)
	{
		cashBookRepository.save(item);

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

	public void saveCashHead(String cashHeadStr)
	{
		CashHead cashHead = new CashHead();
		cashHead.setCashHeadName(cashHeadStr);
		cashHeadRepository.save(cashHead);
	}

	public List<CashBook> searchTransactions(LocalDate fromDate, LocalDate toDate, String entryId, String entryDesc, CashHead cashHead)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Page<Order> fetchOrders(Publisher pub, int page, int size, boolean billed)
	{
		PageRequest pageable = new PageRequest(page, size, Direction.DESC, "orderDate");
		Page<Order> orderList = null;
		if (billed)
		{
			orderList = orderRepository.fetchOrdersForPublisher(pub, pageable);
		}
		else
		{
			orderList = orderRepository.fetchUnBilledOrdersForPub(pub, pageable);
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
		return orderList;
	}

	private SalesTransaction updateBalance(SalesTransaction txn)
	{
		if (txn == null)
		{
			LOGGER.info("Null STxn passed in for updating balance");
			return txn;
		}
		SalesTransaction prevTxn = txn.getPrevTxn();
		if (prevTxn != null)
		{
			txn.setBalance(prevTxn.getBalance() + txn.getNetForBalance());
		}
		else
		{
			txn.setBalance(txn.getNetForBalance());
		}
		txn = salesTxnRepository.save(txn);

		SalesTransaction nextTxn = txn.getNextTxn();
		SalesTransaction currTxn = txn;
		while (nextTxn != null)
		{
			nextTxn.setBalance(currTxn.getBalance() + nextTxn.getNetForBalance());
			salesTxnRepository.save(nextTxn);
			currTxn = nextTxn;
			nextTxn = currTxn.getNextTxn();
		}

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
		SalesTransaction prevTxn = txn.getPrevTxn();
		SalesTransaction nextTxn = txn.getNextTxn();
		SalesTransaction updateFromTxn = null;

		salesTxnRepository.delete(txn);
		salesTxnRepository.flush();
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
		updateBalance(updateFromTxn);
	}

	public Integer fetchNextSalesInvoiceNum()
	{
		return salesRepository.fetchNextSerialSeqVal();
	}
	
	@Transactional
	public void saveSales(Sales pur, Set<OrderItem> ordersIn, SalesTransaction txn)
	{
		// correct
		txn.setSale(pur);
		List<OrderItem> ordersOrig = new ArrayList<>();
		if (txn.getId() == null)
		{
			txn = saveNewSalesTxn(txn);

			pur.setSalesTxn(txn);
			pur = salesRepository.save(pur);

			txn.setSale(pur);
			salesTxnRepository.save(txn);

			for (OrderItem orderIn : ordersIn)
			{
				orderIn.setSale(pur);
			}
			orderItemRepository.save(ordersIn);
			Set<String> bookSet = ordersIn.stream().map(OrderItem::getBook).map(Book::getBookNum).collect(Collectors.toSet());
			updateBookInventory(false, ordersOrig, bookSet, new ArrayList<>(ordersIn), new ArrayList<>(), new ArrayList<>());
		}
		else
		{
			txn = updateSalesTxn(txn);

			pur.setSalesTxn(txn);
			pur = salesRepository.save(pur);

			txn.setSale(pur);
			salesTxnRepository.save(txn);

			List<Integer> orderItemIds = pur.getOrderItems().stream().map(OrderItem::getId).collect(Collectors.toList());
			ordersOrig = orderItemRepository.findAll(orderItemIds);
			saveOrderItemUpdates(ordersOrig, ordersIn, pur);
		}

	}

	private void saveOrderItemUpdates(List<OrderItem> ordersOrig, Set<OrderItem> ordersIn, Sales pur)
	{
		List<OrderItem> removedOrders = new ArrayList<>();
		List<OrderItem> addedOrders = new ArrayList<>();
		List<OrderItem> affectedOrders = new ArrayList<>();
		Set<String> bookNums = new HashSet<>();

		if (ordersOrig != null && !ordersOrig.isEmpty())
		{
			removedOrders.addAll(ordersOrig);
			if (ordersIn != null && !ordersIn.isEmpty())
			{
				affectedOrders.addAll(ordersIn);
				removedOrders.removeAll(ordersIn);
				affectedOrders.removeAll(removedOrders);
			}
			Set<String> bookSet = ordersOrig.stream().map(OrderItem::getBook).map(Book::getBookNum).collect(Collectors.toSet());
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
			Set<String> bookSet = ordersIn.stream().map(OrderItem::getBook).map(Book::getBookNum).collect(Collectors.toSet());
			bookNums.addAll(bookSet);
		}

		for (OrderItem orderIn : addedOrders)
		{
			orderIn.setId(null);
			orderIn.setSale(pur);
		}
		orderItemRepository.save(addedOrders);

		for (OrderItem orderIn : removedOrders)
		{
			orderIn.setSale(null);
		}
		orderItemRepository.save(removedOrders);
		orderItemRepository.save(affectedOrders);
		updateBookInventory(false, ordersOrig, bookNums, addedOrders, removedOrders, affectedOrders);
	}

	private void updateBookInventory(boolean add, List<OrderItem> ordersOrig, Set<String> bookNums, List<OrderItem> addedOrders, List<OrderItem> removedOrders, List<OrderItem> affectedOrders)
	{
		int addFactor = add ? 1 : -1;

		Map<String, Integer> bookCounts = new HashMap<>();
		for (OrderItem orderItem : addedOrders)
		{
			if (bookCounts.containsKey(orderItem.getBook().getBookNum()))
			{
				int bookCnt = bookCounts.get(orderItem.getBook().getBookNum());
				int chgCnt = add ? orderItem.getFullFilledCnt() : orderItem.getSoldCnt();
				bookCounts.put(orderItem.getBook().getBookNum(), bookCnt + chgCnt);
			}
			else
			{
				int chgCnt = add ? orderItem.getFullFilledCnt() : orderItem.getSoldCnt();
				bookCounts.put(orderItem.getBook().getBookNum(), chgCnt);
			}
		}

		for (OrderItem orderItem : removedOrders)
		{
			if (bookCounts.containsKey(orderItem.getBook().getBookNum()))
			{
				int bookCnt = bookCounts.get(orderItem.getBook().getBookNum());
				int chgCnt = add ? orderItem.getFullFilledCnt() : orderItem.getSoldCnt();
				bookCounts.put(orderItem.getBook().getBookNum(), bookCnt - chgCnt);
			}
			else
			{
				int chgCnt = add ? orderItem.getFullFilledCnt() : orderItem.getSoldCnt();
				bookCounts.put(orderItem.getBook().getBookNum(), -chgCnt);
			}
		}

		for (OrderItem orderItem : affectedOrders)
		{
			int origItemIdx = ordersOrig.indexOf(orderItem);
			if (origItemIdx > -1)
			{
				OrderItem origItem = ordersOrig.get(origItemIdx);

				int diff = 0;
				if (add)
				{
					diff = origItem.getFullFilledCnt() - orderItem.getFullFilledCnt();
				}
				else
				{
					diff = origItem.getSoldCnt() - orderItem.getSoldCnt();
				}
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

		List<Book> origBooks = bookRepository.findAll(bookNums);
		for (Book book : origBooks)
		{
			if (bookCounts.containsKey(book.getBookNum()))
			{
				int diff = bookCounts.get(book.getBookNum());
				book.addInventory(addFactor * diff);
			}
		}
		bookRepository.save(origBooks);
	}

	public List<Sales> fetchBills(School school)
	{
		return salesRepository.findAllBySchool(school);
	}

	@Transactional
	public void deleteBill(Sales selectedSale)
	{
		Set<OrderItem> orders = selectedSale.getOrderItems();
		for (OrderItem order : orders)
		{
			order.setSale(null);
		}
		orderItemRepository.save(orders);
		deleteSalesTxn(selectedSale.getSalesTxn());
		selectedSale.setSalesTxn(null);
		salesRepository.delete(selectedSale);
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
		salesTxnRepository.delete(selectedPayment.getSalesTxn());
		schoolPayRepository.delete(selectedPayment);
	}

	public List<SalesTransaction> fetchTransactions(School school, LocalDate fromDateVal, LocalDate toDateVal)
	{
		return salesTxnRepository.findByFromToDate(school, fromDateVal, toDateVal);
	}

	@Transactional
	public void saveSchoolReturn(SchoolReturn returnIn, SalesTransaction txn, Set<OrderItem> orderItemsIn)
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

		returnIn.setSalesTxn(txn);
		returnIn = schoolReturnRepository.save(returnIn);

		txn.setSalesReturn(returnIn);
		txn = salesTxnRepository.save(txn);

		final SchoolReturn returnInFinal = returnIn;
		orderItemsIn.forEach(it -> it.setBookReturn(returnInFinal));
		orderItemRepository.save(returnIn.getOrderItem());

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

	public void deleteReturn(SchoolReturn selectedReturn)
	{
		schoolReturnRepository.delete(selectedReturn);
	}

	public List<Book> fetchBooksForPublisher(Publisher publisher)
	{
		return bookRepository.findAllByPublisher(publisher);
	}

	public Address fetchAddress(String name)
	{
		return addressRepository.findOne(name);
	}

	public Account fetchAccount(String name)
	{
		return accountRepository.findOne(name);
	}

}

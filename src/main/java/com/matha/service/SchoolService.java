package com.matha.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.matha.domain.Book;
import com.matha.domain.BookCategory;
import com.matha.domain.CashBook;
import com.matha.domain.CashHead;
import com.matha.domain.District;
import com.matha.domain.Order;
import com.matha.domain.OrderItem;
import com.matha.domain.Publisher;
import com.matha.domain.Purchase;
import com.matha.domain.Sales;
import com.matha.domain.SalesTransaction;
import com.matha.domain.School;
import com.matha.domain.State;
import com.matha.repository.BookRepository;
import com.matha.repository.CashBookRepository;
import com.matha.repository.CashHeadRepository;
import com.matha.repository.CategoryRepository;
import com.matha.repository.DistrictRepository;
import com.matha.repository.OrderItemRepository;
import com.matha.repository.OrderRepository;
import com.matha.repository.PublisherRepository;
import com.matha.repository.PurchaseRepository;
import com.matha.repository.SalesRepository;
import com.matha.repository.SalesTxnRepository;
import com.matha.repository.SchoolRepository;
import com.matha.repository.StateRepository;

@Service
public class SchoolService {

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
	
	public List<Publisher> fetchAllPublishers() {
		return publisherRepository.findAll();
	}

	public List<BookCategory> fetchAllBookCategories() {
		return categoryRepository.findAll();
	}

	public List<School> fetchSchoolsLike(String schoolPart) {
		return schoolRepoitory.findByNameLike(schoolPart);
	}

	public List<School> fetchByStateAndPart(State state, District district, String schoolPart, String city,
			String pin) {
		return schoolRepoitory.fetchSchools(state, district, schoolPart, city, pin);
	}

	public List<School> fetchAllSchools() {
		return schoolRepoitory.findAll();
	}

	public School fetchSchoolById(String id) {
		return schoolRepoitory.getOne(id);
	}

	public School saveSchool(School school) {
		return schoolRepoitory.save(school);
	}

	public List<State> fetchAllStates() {
		List<State> states = stateRepoitory.findAll();
		for (State state : states) {
			state.getDistricts();
		}
		return states;
	}

	public List<District> fetchAllDistricts() {
		return districtRepository.findAll();
	}

	public List<Book> fetchAllBooks() {
		return bookRepository.findAll();
	}

	public List<Book> fetchBooksByName(String bookName) {
		return bookRepository.findAllByNameStartingWith(bookName);
	}

	public void saveBook(Book bookObj) {
		bookRepository.save(bookObj);
	}

	public void deleteBook(Book selectedOrder) {
		bookRepository.delete(selectedOrder);
	}

	public void saveOrder(Order item) {
		orderRepository.save(item);
	}

	public List<OrderItem> fetchOrderItems() {
		return orderItemRepository.findAll();
	}

	public List<OrderItem> fetchOrderItemsForPublisher(Publisher pub) {
		return orderItemRepository.fetchOrdersForPublisher(pub);
	}

	public List<Order> fetchOrders() {
		return orderRepository.findAll();
	}
	
	public List<Order> fetchOrders(List<String> orderIds) {
		return orderRepository.findAll(orderIds);
	}

	public List<Order> fetchOrderForSchool(School school) {
		return orderRepository.findAllBySchool(school);
	}

	public void deleteOrder(Order order) {
		orderRepository.delete(order);
	}

	public void saveOrderItems(List<OrderItem> orderItem) {
		orderItemRepository.save(orderItem);

	}

	public void deleteSchool(School school) {
		schoolRepoitory.delete(school);
	}

	@Transactional
	public void savePurchase(Purchase pur, List<OrderItem> orderItems) {

		purchaseRepoitory.save(pur);

		for (OrderItem orderItem : orderItems) {
			orderItem.setPurchase(pur);
		}
		saveOrderItems(orderItems);
		pur.setOrderItem(new HashSet<>(orderItems));
		purchaseRepoitory.save(pur);

	}

	public List<Purchase> fetchPurchasesForPublisher(Publisher pub) {
		return purchaseRepoitory.findAllByPublisher(pub);
	}

	public void saveCashBook(CashBook item) {
		cashBookRepository.save(item);

	}

	public List<CashBook> getAllTransactions() {
		return cashBookRepository.findAllByOrderByTxnDateDesc();
	}

	public void deleteTransaction(CashBook cashHead) {
		cashBookRepository.delete(cashHead);
	}

	public void saveCashHead(String cashHeadStr) {
		CashHead cashHead = new CashHead();
		cashHead.setCashHeadName(cashHeadStr);
		cashHeadRepository.save(cashHead);
	}

	public List<CashBook> searchTransactions(LocalDate fromDate, LocalDate toDate, String entryId, String entryDesc,
			CashHead cashHead) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<Order> fetchOrders(Publisher pub, int page, int size) {
		PageRequest pageable = new PageRequest(page, size, Direction.DESC, "orderDate");		
		Page<Order> orderList = orderRepository.fetchOrdersForPublisher(pub, pageable);
		return orderList;
	}
	
	@Transactional
	public void saveSales(Sales sales)
	{
		Set<Order> orders = sales.getOrder();
		for (Order order : orders)
		{
			List<OrderItem> orderItems = order.getOrderItem();
			orderItemRepository.save(orderItems);
		}
		
		SalesTransaction txn = sales.getSalesTxn();
		salesTxnRepository.save(txn);
		
		salesRepository.save(sales);
		
		txn.setSale(sales);
		salesTxnRepository.save(txn);
	}
}

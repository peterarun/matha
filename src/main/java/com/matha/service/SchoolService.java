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
import org.springframework.data.domain.Sort;
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
import com.matha.domain.PurchasePayment;
import com.matha.domain.PurchaseReturn;
import com.matha.domain.PurchaseTransaction;
import com.matha.domain.Sales;
import com.matha.domain.SalesTransaction;
import com.matha.domain.School;
import com.matha.domain.SchoolPayment;
import com.matha.domain.SchoolReturn;
import com.matha.domain.State;
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

	public List<Publisher> fetchAllPublishers()
	{
		return publisherRepository.findAll();
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

	public School fetchSchoolById(String id)
	{
		return schoolRepoitory.getOne(id);
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
		return orderRepository.findAllBySchool(school);
	}

	public void deleteOrder(Order order)
	{
		orderRepository.delete(order);
	}

	public void saveOrderItems(List<OrderItem> orderItem)
	{
		orderItemRepository.save(orderItem);

	}

	public void deleteSchool(School school)
	{
		schoolRepoitory.delete(school);
	}

	private PurchaseTransaction savePurchaseTransaction(PurchaseTransaction txn)
	{
		if (txn.getPrevTxn() == null)
		{
			PurchaseTransaction prevTxn = purchaseTxnRepository.findByNextTxnIsNull();
			
			if(prevTxn != null)
			{
				txn.setPrevTxn(prevTxn);
				txn.setBalance(prevTxn.getBalance() + txn.getNetForBalance());
				// The following false update is done to avoid the Unique Key Violation
				PurchaseTransaction nextTxn = prevTxn.getPrevTxn() == null ? prevTxn : prevTxn.getPrevTxn();   	
				txn.setNextTxn(nextTxn);
				txn = purchaseTxnRepository.save(txn);
				// Following is to remove the false update above
				txn.setNextTxn(null);
				prevTxn.setNextTxn(txn);
				prevTxn = purchaseTxnRepository.save(prevTxn);				
			}
			else
			{
				txn.setBalance(txn.getAmount());
			}			
			txn = purchaseTxnRepository.save(txn);
		}
		else
		{
			PurchaseTransaction prevTxn = txn.getPrevTxn();
			txn.setBalance(prevTxn.getBalance() + txn.getNetForBalance());
			PurchaseTransaction nextTxn = txn.getNextTxn();
			PurchaseTransaction currTxn = txn; 
			while(nextTxn != null)
			{
				nextTxn.setBalance(currTxn.getBalance() + nextTxn.getNetForBalance());
				purchaseTxnRepository.save(nextTxn);
				currTxn = nextTxn; 
				nextTxn = currTxn.getNextTxn();
			}
		}
		txn = purchaseTxnRepository.save(txn);
		return txn;
	}

	@Transactional
	public void savePurchase(Purchase pur, List<OrderItem> orderItems, PurchaseTransaction txn)
	{
		txn = savePurchaseTransaction(txn);

		pur.setSalesTxn(txn);
		pur = purchaseRepoitory.save(pur);

		txn.setPurchase(pur);
		purchaseTxnRepository.save(txn);

		for (OrderItem orderItem : orderItems)
		{
			orderItem.setPurchase(pur);
		}
		saveOrderItems(orderItems);
		pur.setOrderItems(new HashSet<>(orderItems));
		purchaseRepoitory.save(pur);

	}

	public List<Purchase> fetchPurchasesForPublisher(Publisher pub)
	{
		return purchaseRepoitory.findAllByPublisher(pub);
	}

	@Transactional
	public void deletePurchase(Purchase pur)
	{
		PurchaseTransaction txn = pur.getSalesTxn();
		purchaseRepoitory.delete(pur);
		purchaseTxnRepository.delete(txn);
	}

	@Transactional
	public void savePurchaseReturn(PurchaseReturn returnIn, PurchaseTransaction txn, Set<OrderItem> orderItems)
	{
		txn = savePurchaseTransaction(txn);		

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
	
	public void deletePurchaseReturn(PurchaseReturn purchase)
	{
		// TODO Auto-generated method stub
		
	}
	
	public List<PurchasePayment> fetchPurchasePayments(Publisher pub)
	{
		Sort dateSort = new Sort(new Sort.Order(Direction.DESC, "salesTxn.txnDate"));
		return purchasePayRepository.findAllByPublisher(pub, dateSort);
	}	
	
	@Transactional
	public void savePurchasePay(PurchasePayment sPayment, PurchaseTransaction sTxn)
	{
		sTxn = savePurchaseTransaction(sTxn);

		sPayment.setSalesTxn(sTxn);
		sPayment = purchasePayRepository.save(sPayment);

		sTxn.setPayment(sPayment);
		purchaseTxnRepository.save(sTxn);

	}
	
	public void deletePurchasePayment(PurchasePayment purchase)
	{
		// TODO Auto-generated method stub
		
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

	public Page<Order> fetchOrders(Publisher pub, int page, int size)
	{
		PageRequest pageable = new PageRequest(page, size, Direction.DESC, "orderDate");
		Page<Order> orderList = orderRepository.fetchOrdersForPublisher(pub, pageable);
		return orderList;
	}

	public List<Order> fetchAllOrders(Publisher pub)
	{
		List<Order> orderList = orderRepository.fetchOrdersForPublisher(pub);
		return orderList;
	}

	@Transactional
	public void saveSales(Sales sales)
	{
		Set<Order> orders = sales.getOrder();

		SalesTransaction txn = sales.getSalesTxn();
		salesTxnRepository.save(txn);

		sales.setInvoiceNo(salesRepository.fetchNextSerialSeqVal());
		salesRepository.save(sales);

		txn.setSale(sales);
		salesTxnRepository.save(txn);

		for (Order order : orders)
		{
			order.setSale(sales);
			orderRepository.save(order);

			List<OrderItem> orderItems = order.getOrderItem();
			orderItemRepository.save(orderItems);
		}
	}

	public List<Sales> fetchBills(School school)
	{
		return salesRepository.findAllBySchool(school);
	}

	@Transactional
	public void deleteOrder(Sales selectedSale)
	{
		Set<Order> orders = selectedSale.getOrder();
		for (Order order : orders)
		{
			order.setSale(null);
		}
		orderRepository.save(orders);
		salesTxnRepository.delete(selectedSale.getSalesTxn());
		salesRepository.delete(selectedSale);
	}

	@Transactional
	public void savePayment(SchoolPayment sPayment)
	{
		SalesTransaction txn = sPayment.getSalesTxn();
		salesTxnRepository.save(txn);

		schoolPayRepository.save(sPayment);

		txn.setPaymentId(sPayment);
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
	public void saveSchoolReturn(SchoolReturn returnIn)
	{
		SalesTransaction txn = returnIn.getSalesTxn();
		salesTxnRepository.save(txn);

		orderItemRepository.save(returnIn.getOrderItem());
		schoolReturnRepository.save(returnIn);
		returnIn.getOrderItem().forEach(it -> it.setBookReturn(returnIn));
		orderItemRepository.save(returnIn.getOrderItem());

		txn.setReturnId(returnIn);
		salesTxnRepository.save(txn);

	}

	public Double fetchBalance(School school)
	{
		return salesTxnRepository.findBalance(school);
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

}

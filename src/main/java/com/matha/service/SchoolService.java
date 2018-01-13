package com.matha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matha.domain.Book;
import com.matha.domain.BookCategory;
import com.matha.domain.District;
import com.matha.domain.Order;
import com.matha.domain.OrderItem;
import com.matha.domain.Publisher;
import com.matha.domain.School;
import com.matha.domain.State;
import com.matha.repository.BookRepository;
import com.matha.repository.CategoryRepository;
import com.matha.repository.DistrictRepository;
import com.matha.repository.OrderItemRepository;
import com.matha.repository.OrderRepository;
import com.matha.repository.PublisherRepository;
import com.matha.repository.SchoolRepository;
import com.matha.repository.StateRepository;

@Service
public class SchoolService {

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

	public List<Order> fetchOrders() {
		return orderRepository.findAll();
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

}

package com.matha.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.Book;
import com.matha.domain.Order;
import com.matha.domain.OrderItem;
import com.matha.domain.School;
import com.matha.service.SchoolService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class AddOrderController {

	private static final Logger LOGGER = Logger.getLogger("AddOrderController");

	private Map<String, Book> bookMap;
	private School school;
	
	@FXML
	private TextField orderNum;

	@FXML
	private TextField bookText;

	@FXML
	private TextField bookCount;

	@FXML
	private Label schoolName;

	@FXML
	private DatePicker orderDate;
	
	@FXML
	private DatePicker despatchDate;

	@FXML
	private TextField desLocation;
	
	@FXML
	private TableView<OrderItem> addedBooks;

	@FXML
	private Button cancelBtn;

	@Autowired
	private SchoolService srvc;
	
	private Order order;

	public void initData(School school, Map<String, Book> bookMap, Order selectedOrder) {
		System.out.println("initData");
		this.school = school;
		this.bookMap = bookMap;
		this.order = selectedOrder;
//		this.orderDate.setValue(LocalDate.now());

		updateFormData(selectedOrder);
		
		List<String> items = new ArrayList<>(bookMap.keySet());
		TextFields.bindAutoCompletion(bookText, items);

	}

	@FXML
	void addBookData(ActionEvent e) {
		System.out.println(bookText.getText());
		LOGGER.info(addedBooks.getItems().toString());
		OrderItem item = new OrderItem();
		item.setBook(bookMap.get(bookText.getText()));
		item.setCount(Integer.parseInt(bookCount.getText()));
		addedBooks.getItems().add(item);
		bookText.clear();
		bookCount.clear();
	}

	@FXML
	void removeEntry(ActionEvent e) {
		int sels = addedBooks.getSelectionModel().getSelectedIndex();
		addedBooks.getItems().remove(sels);
	}

	@FXML
	void saveData(ActionEvent e) {

		Order item;
		if(this.order == null)
		{
			item = new Order();
		}
		else
		{
			item = this.order;
		}
		item.setOrderItem(addedBooks.getItems());
		srvc.saveOrderItems(item.getOrderItem());
		item.setSerialNo(orderNum.getText());
		item.setSchool(school);
		LocalDate dt = orderDate.getValue();
		item.setOrderDate(dt);
		item.setDeliveryDate(despatchDate.getValue());
		item.setDesLocation(desLocation.getText());
		
		srvc.saveOrder(item);

		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void cancelOperation(ActionEvent e) {
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	public void updateFormData(Order selectedOrder) {
		if(selectedOrder == null)
		{
			return;
		}
		this.orderNum.setText(selectedOrder.getSerialNo());
		this.orderDate.setValue(selectedOrder.getOrderDate());
		this.addedBooks.setItems(FXCollections.observableList(selectedOrder.getOrderItem()));
	}

}

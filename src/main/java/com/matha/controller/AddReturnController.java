package com.matha.controller;

import static com.matha.util.Utils.showErrorAlert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.Book;
import com.matha.domain.OrderItem;
import com.matha.domain.SalesTransaction;
import com.matha.domain.School;
import com.matha.domain.SchoolReturn;
import com.matha.service.SchoolService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class AddReturnController
{

	private static final Logger LOGGER = LogManager.getLogger("AddReturnController");

	@Autowired
	private SchoolService schoolService;

	@FXML
	private DatePicker returnDate;

	@FXML
	private TextField notes;

	@FXML
	private Button cancelBtn;

	@FXML
	private TextField subTotal;

	@FXML
	private Label schoolName;

	@FXML
	private TextField bookName;
	
	@FXML
	private TextField quantity;
	
	@FXML
	private TextField price;

	@FXML
	private TableView<OrderItem> addedBooks;

	private School school;
	private SchoolReturn schoolReturn;
	private Map<String, Book> bookMap;
	private Collector<OrderItem, ?, Double> summingDblCollector = Collectors.summingDouble(OrderItem::getTotal);
	private Collector<Book, ?, Map<String, Book>> bookMapCollector = Collectors.toMap(o -> o.getName() + " - " + o.getPublisherName(), o -> o);	
	
	void initData(School schoolIn, SchoolReturn returnIn)
	{
		this.school = schoolIn;
		this.schoolReturn = returnIn;
		this.loadReturnData(returnIn);
		this.schoolName.setText(schoolIn.getName());

		List<Book> allOrders = schoolService.fetchBooksForSchool(schoolIn);
		bookMap = allOrders.stream().collect(bookMapCollector);

		List<String> items = new ArrayList<>(bookMap.keySet());
		TextFields.bindAutoCompletion(bookName, items);

	}

	private void loadReturnData(SchoolReturn returnIn)
	{
		if (returnIn != null)
		{
			if (returnIn.getOrderItem() != null)
			{
				ObservableList<OrderItem> orderItemsIn = FXCollections
						.observableList(new ArrayList<OrderItem>(returnIn.getOrderItem()));
				this.addedBooks.setItems(orderItemsIn);
			}
			this.returnDate.setValue(returnIn.getSalesTxn().getTxnDate());
			this.notes.setText(returnIn.getSalesTxn().getNote());
			
			Double subTotalDbl = returnIn.getSalesTxn().getAmount();
			if (subTotalDbl != null)
			{
				this.subTotal.setText(subTotalDbl.toString());
			}
		}
	}

	private void loadSubTotal()
	{
		ObservableList<OrderItem> orderItemsIn = this.addedBooks.getItems();
		Double subTotalDbl = orderItemsIn.stream().collect(summingDblCollector);
		this.subTotal.setText(subTotalDbl.toString());		
	}
	
	@FXML
	void addBookData(ActionEvent event)
	{
		OrderItem itemIn = new OrderItem();
		String bookStr = this.bookName.getText(); 
		itemIn.setBook(this.bookMap.get(bookStr));
		itemIn.setCount(Integer.parseInt(this.quantity.getText()));
		itemIn.setBookPrice(Double.parseDouble(this.price.getText()));
		this.addedBooks.getItems().add(itemIn);
		LOGGER.debug("Added" + itemIn);
		loadSubTotal();
		clearBookFields();
	}
	
	private void clearBookFields()
	{
		this.bookName.clear();
		this.quantity.clear();
		this.price.clear();
	}

	private boolean validateData()
	{
		boolean valid = true;
		StringBuilder errorMsg = new StringBuilder();
		if (this.returnDate.getValue() == null)
		{
			errorMsg.append("Please provide a date");
			valid = false;
		}
		showErrorAlert("Error in Saving Order", "Please correct the following errors", errorMsg.toString());
		return valid;
	}
	
	@FXML
	void saveData(ActionEvent event)
	{
		if(!validateData())
		{
			return;
		}
		SchoolReturn returnIn = this.schoolReturn;
		SalesTransaction salesTxn = null;
		if(returnIn == null)
		{
			returnIn = new SchoolReturn();
			salesTxn = new SalesTransaction();
			salesTxn.setSchool(school);			
		}
		else
		{
			salesTxn = returnIn.getSalesTxn();
		}
		Set<OrderItem> orderItems = returnIn.getOrderItem();
		if(orderItems == null)
		{
			orderItems = new HashSet<>();
		}
			
		orderItems.addAll(this.addedBooks.getItems());
		salesTxn.setTxnDate(this.returnDate.getValue());
		salesTxn.setNote(this.notes.getText());
	
		String subTotalStr = this.subTotal.getText();				
		if (StringUtils.isNotBlank(subTotalStr))
		{
			salesTxn.setAmount(Double.parseDouble(subTotalStr));
		}		
		
		schoolService.saveSchoolReturn(returnIn, salesTxn, orderItems);
		
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void cancelOperation(ActionEvent event)
	{
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void removeOperation(ActionEvent event)
	{
		OrderItem itemIn = this.addedBooks.getSelectionModel().getSelectedItem();
		this.addedBooks.getItems().remove(itemIn);
		loadSubTotal();
	}

}

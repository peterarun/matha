package com.matha.controller;

import static com.matha.util.Utils.getDoubleVal;
import static com.matha.util.Utils.getStringVal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.Book;
import com.matha.domain.OrderItem;
import com.matha.domain.Publisher;
import com.matha.domain.PurchaseReturn;
import com.matha.domain.PurchaseTransaction;
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
public class AddPurchaseRetController
{

	private static final Logger LOGGER = Logger.getLogger("AddPurchaseRetController");

	@Autowired
	private SchoolService schoolService;

	@FXML
	private TextField publisherDetails;
	
	@FXML
	private Label message;
	
	@FXML
	private TextField creditNoteNum;
	
	@FXML
	private DatePicker returnDate;

	@FXML
	private TextField bookName;
	
	@FXML
	private TextField quantity;
	
	@FXML
	private TextField price;

	@FXML
	private TableView<OrderItem> addedBooks;	

	@FXML
	private TextField subTotal;
	
	@FXML
	private TextField notes;

	@FXML
	private Button cancelBtn;

	private Publisher publisher;
	private PurchaseReturn purchaseReturn;
	private Map<String, Book> bookMap;
	private Collector<OrderItem, ?, Double> summingDblCollector = Collectors.summingDouble(OrderItem::getTotal);
	private Collector<Book, ?, Map<String, Book>> bookMapCollector = Collectors.toMap(o -> o.getShortName() + ": " + o.getName() + " - " + o.getPublisherName(), o -> o);	
	
	void initData(Publisher publisherIn, PurchaseReturn purchaseReturnIn)
	{
		this.publisher = publisherIn;
		this.purchaseReturn = purchaseReturnIn;
		this.publisherDetails.setText(this.publisher.getAddress());
		
		this.loadReturnData(purchaseReturnIn);

		List<Book> allOrders = schoolService.fetchBooksForPublisher(publisher);
		bookMap = allOrders.stream().collect(bookMapCollector);

		List<String> items = new ArrayList<>(bookMap.keySet());
		TextFields.bindAutoCompletion(bookName, items);

	}

	private void loadReturnData(PurchaseReturn purchaseReturnIn)
	{
		if (purchaseReturnIn != null)
		{
			if (purchaseReturnIn.getOrderItem() != null)
			{
				ObservableList<OrderItem> orderItemsIn = FXCollections
						.observableList(new ArrayList<OrderItem>(purchaseReturnIn.getOrderItem()));
				this.addedBooks.setItems(orderItemsIn);
			}
			this.returnDate.setValue(purchaseReturnIn.getSalesTxn().getTxnDate());
			this.notes.setText(purchaseReturnIn.getSalesTxn().getNote());
			
			if (purchaseReturnIn.getSalesTxn() != null)
			{
				this.subTotal.setText(getStringVal(purchaseReturnIn.getSalesTxn().getAmount()));
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
		loadSubTotal();
		clearBookFields();
	}
	
	private void clearBookFields()
	{
		this.bookName.clear();
		this.quantity.clear();
		this.price.clear();
	}

	@FXML
	void saveData(ActionEvent event)
	{
		PurchaseReturn returnIn = this.purchaseReturn;		
		if(returnIn == null)
		{
			returnIn = new PurchaseReturn();						
		}
		
		PurchaseTransaction salesTxn = returnIn.getSalesTxn();
		if(salesTxn == null)
		{
			salesTxn = new PurchaseTransaction();
			salesTxn.setPublisher(publisher);
		}
		
		TreeSet<OrderItem> orderItems = new TreeSet<>();	
		orderItems.addAll(this.addedBooks.getItems());
		
		salesTxn.setTxnDate(this.returnDate.getValue());
		salesTxn.setNote(this.notes.getText());
		salesTxn.setAmount(getDoubleVal(this.subTotal));	
		
		schoolService.savePurchaseReturn(returnIn, salesTxn, orderItems);
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

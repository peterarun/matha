package com.matha.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.controlsfx.control.textfield.TextFields;

import com.matha.Main;
import com.matha.domain.Book;
import com.matha.service.SchoolService;
import com.matha.vo.BookItem;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AddOrderController {

	private static final Logger LOGGER = Logger.getLogger("AddOrderController");

	private Map<String, Book> bookMap;

	@FXML
	private TextField orderNum;

	@FXML
	private TextField bookText;
	// private AutoCompletionTextField bookText;
	
	@FXML
	private TextField bookCount;	

	@FXML
	private Label schoolName;

	@FXML
	private DatePicker orderDate;

	@FXML
	private TableView<BookItem> addedBooks;

	@FXML
	protected void initialize() {
		System.out.println("initialize");
		SchoolService srvc = Main.getContext().getBean(SchoolService.class);
		List<Book> schools = srvc.fetchAllBooks();
//		LOGGER.info(schools.toString());
		bookMap = new HashMap<>();
		for (Book bookIn : schools) {
			LOGGER.info(bookIn.toString());
			bookMap.put(bookIn.getName(), bookIn);	
		}		
//		 List<String> items = Arrays.asList(new String[]{"Hey", "Hello", "Hello World", "Apple", "Cool", "Costa", "Cola", "Coca Cola"});
//		addedBooks.setItems(FXCollections.observableArrayList());
		List<String> items = new ArrayList<>(bookMap.keySet());
		LOGGER.info(items.toString());
		TextFields.bindAutoCompletion(bookText, items);

	}

	public void initData() {
		System.out.println("initData");
	}

	@FXML
	void addBookData(ActionEvent e) {
		System.out.println(bookText.getText());
		LOGGER.info(addedBooks.getItems().toString());
		BookItem item = new BookItem();
		item.setBook(bookMap.get(bookText.getText()));
		item.setCount(Integer.parseInt(bookCount.getText()));
		addedBooks.getItems().add(item);
	}

	@FXML
	void removeEntry(ActionEvent e) {
		int sels = addedBooks.getSelectionModel().getSelectedIndex();
		addedBooks.getItems().remove(sels);
	}

	@FXML
	void saveData(ActionEvent e) {

	}

	@FXML
	void cancelOperation(ActionEvent e) {

	}

}

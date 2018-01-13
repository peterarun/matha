package com.matha.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.Book;
import com.matha.domain.BookCategory;
import com.matha.domain.Publisher;
import com.matha.service.SchoolService;
import com.matha.util.Converters;
import com.matha.util.SchoolConverter;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class AddBookController {

	@Autowired
	private SchoolService srvc;

	@FXML
	private Button cancelBtn;

	@FXML
	private TextField name;

	@FXML
	private TextField bookNo;

	@FXML
	private TextField shortName;

	@FXML
	private ChoiceBox<Publisher> publishers;

	@FXML
	private ChoiceBox<BookCategory> categories;

	@FXML
	protected void initialize() {
		List<BookCategory> bookCategories = srvc.fetchAllBookCategories();
		categories.setConverter(Converters.getCategoryConverter());
		categories.setItems(FXCollections.observableList(bookCategories));

		List<Publisher> bookPublishers = srvc.fetchAllPublishers();
		publishers.setConverter(Converters.getPublisherConverter());
		publishers.setItems(FXCollections.observableList(bookPublishers));
	}

	public void initEdit(Book selectedItem) {
		this.name.setText(selectedItem.getName());
		this.bookNo.setText(selectedItem.getBookNum());
		this.shortName.setText(selectedItem.getShortName());
		this.categories.getSelectionModel().select(selectedItem.getCategory());
		this.publishers.getSelectionModel().select(selectedItem.getPublisher());
		this.name.setText(selectedItem.getName());
	}

	@FXML
	void handleCancel(ActionEvent event) {
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void handleSave(ActionEvent event) {

		Book bookObj = SchoolConverter.createBookObj(name, bookNo, shortName, categories, publishers);
		srvc.saveBook(bookObj);
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

}

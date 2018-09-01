package com.matha.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.Book;
import com.matha.domain.BookCategory;
import com.matha.domain.Publisher;
import com.matha.service.SchoolService;
import com.matha.util.Converters;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class AddBookController
{

	@Autowired
	private SchoolService srvc;

	@FXML
	private Button cancelBtn;

	@FXML
	private TextField name;

	@FXML
	private TextField shortName;

	@FXML
	private TextField inventory;

	@FXML
	private ChoiceBox<Publisher> publishers;

	private Book book;

	@FXML
	protected void initialize()
	{
		List<Publisher> bookPublishers = srvc.fetchAllPublishers();
		publishers.setConverter(Converters.getPublisherConverter());
		publishers.setItems(FXCollections.observableList(bookPublishers));
	}

	public void initEdit(Book selectedItem)
	{
		this.book = selectedItem;
		this.name.setText(selectedItem.getName());
		this.shortName.setText(selectedItem.getBookNum());
		if (selectedItem.getInventory() != null)
		{
			this.inventory.setText(StringUtils.defaultString(selectedItem.getInventory().toString()));
		}
		this.publishers.getSelectionModel().select(selectedItem.getPublisher());
	}

	@FXML
	void handleCancel(ActionEvent event)
	{
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void handleSave(ActionEvent event)
	{
		Book bookObj = book;
		if (bookObj == null)
		{
			bookObj = new Book();
		}
		bookObj.setName(name.getText());
		bookObj.setBookNum(shortName.getText());
		bookObj.setPublisher(publishers.getSelectionModel().getSelectedItem());
		srvc.saveBook(bookObj);

		((Stage) cancelBtn.getScene().getWindow()).close();
	}

}

package com.matha.controller;

import com.matha.domain.Book;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.matha.util.UtilConstants.NEW_LINE;
import static com.matha.util.Utils.getIntegerVal;
import static com.matha.util.Utils.showErrorAlert;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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
			this.inventory.setText(defaultString(selectedItem.getInventory().toString()));
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
		if(!this.validateData())
		{
			return;
		}
		Book bookObj = book;
		if (bookObj == null)
		{
			bookObj = new Book();
		}
		bookObj.setName(name.getText());
		bookObj.setBookNum(shortName.getText());
		bookObj.setInventory(getIntegerVal(this.inventory));
		bookObj.setPublisher(publishers.getSelectionModel().getSelectedItem());
		srvc.saveBook(bookObj);

		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	private boolean validateData()
	{
		boolean valid = true;
		StringBuilder errorMsg = new StringBuilder();
		if(isBlank(this.name.getText()))
		{
			errorMsg.append("Please provide a Name");
			errorMsg.append(NEW_LINE);
			valid = false;
		}
		if (isBlank(this.shortName.getText()))
		{
			errorMsg.append("Please provide a Short Name");
			errorMsg.append(NEW_LINE);
			valid = false;
		}
		if (this.publishers.getSelectionModel().getSelectedItem() == null)
		{
			errorMsg.append("Please select a Publisher");
			valid = false;
		}
		if(!valid)
		{
			showErrorAlert("Error in Saving Bill", "Please correct the following errors", errorMsg.toString());
		}
		return valid;
	}

}

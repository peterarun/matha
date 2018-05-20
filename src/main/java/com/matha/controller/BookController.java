package com.matha.controller;

import static com.matha.util.UtilConstants.NEW_LINE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.Book;
import com.matha.service.SchoolService;
import com.matha.util.LoadUtils;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

@Component
public class BookController
{

	@Autowired
	SchoolService srvc;

	@FXML
	private TableView<Book> tableView;

	@FXML
	private TextField bookName;

	private String addBookFxmlFile = "/fxml/addBook.fxml";;

	@FXML
	protected void initialize()
	{
		List<Book> bookList = srvc.fetchAllBooks();
		tableView.setItems(FXCollections.observableList(bookList));
	}

	private void loadData(String text)
	{

		List<Book> bookList = new ArrayList<>();
		if (StringUtils.isEmpty(text))
		{
			bookList = srvc.fetchAllBooks();
		}
		else
		{
			bookList = srvc.fetchBooksByName(text);
		}

		tableView.setItems(FXCollections.observableList(bookList));
	}

	@FXML
	void nameSearch(KeyEvent event)
	{

		String bookNameAll = this.bookName.getText() + event.getCharacter();
		loadData(bookNameAll);
	}

	@FXML
	void addBook(ActionEvent event)
	{

		try
		{
			String bookNameAll = this.bookName.getText();
			FXMLLoader fxmlLoader = LoadUtils.loadFxml(this, addBookFxmlFile);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);

			Stage stage = LoadUtils.loadChildStage(event, scene);
			stage.setOnHiding(new EventHandler<WindowEvent>() {
				@Override
				public void handle(final WindowEvent event)
				{
					loadData(bookNameAll);
				}
			});
			stage.show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	@FXML
	void addInventory(ActionEvent event)
	{

		Book selBook = this.tableView.getSelectionModel().getSelectedItem();
		TextInputDialog dialog = new TextInputDialog("0");
		dialog.setTitle("Add Inventory");
		dialog.setHeaderText("Book: " + selBook.getName());
		dialog.setContentText("Inventory quantity to be added:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent())
		{
			String qtyStr = result.get();
			if (StringUtils.isNotBlank(qtyStr))
			{
				Integer addedVal = Integer.parseInt(qtyStr);
				selBook.addInventory(addedVal);
				srvc.saveBook(selBook);
			}
		}
		loadData(this.bookName.getText());

	}

	@FXML
	void deleteBook(ActionEvent event)
	{

		Book selectedOrder = tableView.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Order Confirmation");
		alert.setHeaderText("Are you sure you want to delete the book: " + selectedOrder.getName() + NEW_LINE + selectedOrder.getPublisherName());
		alert.setContentText("Click Ok to Delete");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			srvc.deleteBook(selectedOrder);
			String bookNameAll = this.bookName.getText();
			loadData(bookNameAll);
		}
	}

	@FXML
	void editBook(ActionEvent event)
	{

		try
		{
			String bookNameAll = this.bookName.getText();
			FXMLLoader fxmlLoader = LoadUtils.loadFxml(this, addBookFxmlFile);
			Parent root = fxmlLoader.load();

			AddBookController ctrl = fxmlLoader.getController();
			ctrl.initEdit(this.tableView.getSelectionModel().getSelectedItem());

			Scene scene = new Scene(root);

			Stage stage = LoadUtils.loadChildStage(event, scene);
			stage.setOnHiding(new EventHandler<WindowEvent>() {
				@Override
				public void handle(final WindowEvent event)
				{
					loadData(bookNameAll);
				}
			});
			stage.show();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}

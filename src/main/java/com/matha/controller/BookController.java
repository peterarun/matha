package com.matha.controller;

import static com.matha.util.UtilConstants.EMPTY_STR;
import static com.matha.util.UtilConstants.NEW_LINE;
import static com.matha.util.UtilConstants.addBookFxmlFile;
import static com.matha.util.Utils.showErrorAlert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.matha.domain.School;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

@Component
public class BookController
{

	private static final Logger LOGGER = LogManager.getLogger(BookController.class);

	@Autowired
	SchoolService srvc;

	@FXML
	private TableView<Book> tableView;

	@FXML
	private TableColumn<Book, String> shortNameCol;

	@FXML
	private TableColumn<Book, String> bkNameCol;

	@FXML
	private TableColumn<Book, Double> prcCol;

	@FXML
	private TableColumn<Book, String> pubCol;

	@FXML
	private TableColumn<Book, Integer> invCol;

	@FXML
	private TextField bookName;

	@FXML
	private TextField bookNum;

	@FXML
	protected void initialize()
	{
		List<Book> bookList = srvc.fetchAllBooks();
		tableView.setItems(FXCollections.observableList(bookList));

		this.shortNameCol.prefWidthProperty().bind(this.tableView.widthProperty().multiply(0.09));
		this.bkNameCol.prefWidthProperty().bind(this.tableView.widthProperty().multiply(0.45));
		this.pubCol.prefWidthProperty().bind(this.tableView.widthProperty().multiply(0.22));
		this.invCol.prefWidthProperty().bind(this.tableView.widthProperty().multiply(0.11));
		this.prcCol.prefWidthProperty().bind(this.tableView.widthProperty().multiply(0.12));

		tableView.setOnMouseClicked(mouseEvent -> {
			if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2)
			{
				editBook(mouseEvent);
			}
		});
	}

	private void loadDataName(String text)
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

	private void loadDataFromNum(String text)
	{
		List<Book> bookList = new ArrayList<>();
		if (StringUtils.isEmpty(text))
		{
			bookList = srvc.fetchAllBooks();
		}
		else
		{
			bookList = srvc.fetchBooksByNum(text);
		}
		tableView.setItems(FXCollections.observableList(bookList));
	}

	private void loadBooks()
	{
		if(StringUtils.isNotBlank(this.bookNum.getText()))
		{
			this.loadDataFromNum(this.bookNum.getText());
		}
		else if(StringUtils.isNotBlank(this.bookName.getText()))
		{
			this.loadDataName(this.bookName.getText());
		}
		else
		{
			this.initialize();
		}
	}

	@FXML
	void nameSearch(KeyEvent event)
	{
		this.bookNum.setText(EMPTY_STR);
		String bookNameAll = this.bookName.getText() + event.getCharacter();
		loadDataName(bookNameAll);
	}


	@FXML
	void numSearch(KeyEvent event)
	{
		this.bookName.setText(EMPTY_STR);
		String bookNameAll = this.bookNum.getText() + event.getCharacter();
		loadDataFromNum(bookNameAll);
	}

	@FXML
	void addBook(ActionEvent event)
	{
		try
		{
			FXMLLoader fxmlLoader = LoadUtils.loadFxml(this, addBookFxmlFile);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);

			Stage stage = LoadUtils.loadChildStage(event, scene);
			stage.setOnHiding(new EventHandler<WindowEvent>() {
				@Override
				public void handle(final WindowEvent event)
				{
					loadBooks();
				}
			});
			stage.show();
		}
		catch (IOException e)
		{
			LOGGER.error("Error...", e);
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
		loadBooks();
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
			try
			{
				srvc.deleteBook(selectedOrder);
			}
			catch (DataIntegrityViolationException dive)
			{
				LOGGER.error("A Constraint Violated", dive);
				showErrorAlert("Error in Deleting Book", "Please correct the following errors", "Book is used in Orders/Bills/Credit Notes");
			}
			String bookNameAll = this.bookName.getText();
			loadBooks();
		}
	}

	@FXML
	void editBook(Event event)
	{
		try
		{
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
					loadBooks();
				}
			});
			stage.show();
		}
		catch (IOException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}
}

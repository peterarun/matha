package com.matha.controller;

import java.util.List;
import java.util.Optional;

import com.matha.domain.Book;
import javafx.event.Event;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.CashBook;
import com.matha.domain.CashHead;
import com.matha.service.SchoolService;
import com.matha.util.LoadUtils;
import com.matha.util.UtilConstants;

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

import static com.matha.util.Converters.getCashHeadConverter;
import static com.matha.util.UtilConstants.DATE_CONV;
import static com.matha.util.Utils.verifyDblClick;
import static javafx.collections.FXCollections.observableList;

@Component
public class CashBookController {

	private static final Logger LOGGER = LogManager.getLogger(CashBookController.class);

	@Autowired
	private SchoolService srvc;

	@FXML
	private DatePicker fromDate;

	@FXML
	private TableView<CashBook> txnData;

	@FXML
	private DatePicker toDate;

	@FXML
	private TextField transactionDesc;
	
	@FXML 
	private ChoiceBox<CashHead> cashHead;

	@FXML
	private TableColumn<CashBook, Integer> id;

	@FXML
	private TableColumn<CashBook, String> description;

	@FXML
	private TableColumn<CashBook, String> txnDateStr;

	@FXML
	private TableColumn<CashBook, Double> amount;

	@FXML
	private TableColumn<CashBook, String> typeVal;

	@FXML
	private TableColumn<CashBook, String> mode;

	@FXML
	protected void initialize()
	{
		List<CashBook> transactions = srvc.getAllTransactions();
		this.txnData.setItems(FXCollections.observableList(transactions));
		this.txnData.setOnMouseClicked(ev -> {
			if(verifyDblClick(ev)) editTransaction(ev);
		});

		List<CashHead> cashHeads = srvc.fetchCashHeads();
		this.cashHead.setConverter(getCashHeadConverter());
		this.cashHead.setItems(observableList(cashHeads));

		this.id.prefWidthProperty().bind(this.txnData.widthProperty().multiply(0.05));
		this.description.prefWidthProperty().bind(this.txnData.widthProperty().multiply(0.45));
		this.txnDateStr.prefWidthProperty().bind(this.txnData.widthProperty().multiply(0.2));
		this.amount.prefWidthProperty().bind(this.txnData.widthProperty().multiply(0.1));
		this.typeVal.prefWidthProperty().bind(this.txnData.widthProperty().multiply(0.1));
		this.mode.prefWidthProperty().bind(this.txnData.widthProperty().multiply(0.1));
	}

	@FXML
	void addTransaction(ActionEvent event) {

		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, UtilConstants.addTransactionFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			Scene addOrderScene = new Scene(addOrderRoot);
			AddTxnController ctrl = createOrderLoader.getController();
			ctrl.initTxnId();
			prepareAndShowStage(event, addOrderScene);
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void deleteTransaction(ActionEvent event) {

		CashBook selectedOrder = this.txnData.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Order Confirmation");
		alert.setHeaderText("Are you sure you want to delete the transaction?");
		alert.setContentText("Click Ok to Delete");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			srvc.deleteTransaction(selectedOrder);
			this.search(null);
		}
	}

	@FXML
	void editTransaction(Event event)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, UtilConstants.addTransactionFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddTxnController ctrl = createOrderLoader.getController();
			ctrl.updateFormData(this.txnData.getSelectionModel().getSelectedItem());			
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene);
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void addCashHead(ActionEvent event) {

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add Cash Head");
		dialog.setHeaderText("Please provide Cash Head Details");
		dialog.setContentText("Cash Head: ");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    String cashHead = result.get();
		    srvc.saveCashHead(cashHead);
		}
	}

	@FXML
	void search(ActionEvent event)
	{
		List<CashBook> transactions = srvc.searchTransactions(fromDate.getValue(), toDate.getValue(), this.transactionDesc.getText(), this.cashHead.getSelectionModel().getSelectedItem());
		this.txnData.setItems(FXCollections.observableList(transactions));
	}

	private void prepareAndShowStage(Event e, Scene childScene) {
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.setOnHiding(ev -> initialize());
//		stage.setOnHiding(new EventHandler<WindowEvent>() {
//			@Override
//			public void handle(final WindowEvent event) {
//				initialize();
//			}
//		});
		stage.show();
	}

}

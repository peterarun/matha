package com.matha.controller;

import static com.matha.util.UtilConstants.*;
import static com.matha.util.UtilConstants.addPaymentFxmlFile;
import static com.matha.util.UtilConstants.createOrderFxmlFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.Book;
import com.matha.domain.Order;
import com.matha.domain.Sales;
import com.matha.domain.SalesTransaction;
import com.matha.domain.School;
import com.matha.domain.SchoolPayment;
import com.matha.domain.SchoolReturn;
import com.matha.service.SchoolService;
import com.matha.util.LoadUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

@Component
public class SchoolDetailsController
{

	private Parent addOrderRoot;
	private Scene addOrderScene;
	private School school;

	@Autowired
	SchoolService schoolService;

	@FXML
	private TableView<Order> txnData;

	@FXML
	private TableView<Sales> billData;

	@FXML
	private Tab paymentTab;

	@FXML
	private TableView<SchoolPayment> paymentData;

	@FXML
	private Tab statementTab;

	@FXML
	private TableView<SalesTransaction> transactionData;

	@FXML
	private Tab creditNoteTab;

	@FXML
	private TableView<SchoolReturn> creditNoteData;

	@FXML
	private TextArea address;

	@FXML
	private TextField phone;
	
	@FXML
	private TextField outBalance;

	@FXML
	private Label schoolName;

	@FXML
	private TextField email;

	@FXML
	private DatePicker fromDate;

	@FXML
	private DatePicker toDate;

	private HashMap<String, Book> bookMap;

	@FXML
	protected void initialize() throws IOException
	{

		// if (addOrderRoot == null) {
		// addOrderRoot = createOrderLoader.load();
		// }
		// AddOrderController ctrl = createOrderLoader.getController();
		// ctrl.initData();
		// addOrderScene = new Scene(addOrderRoot);
		// SchoolService srvc = Main.getContext().getBean(SchoolService.class);
		System.out.println(schoolService);

		List<Book> schools = schoolService.fetchAllBooks();
		// LOGGER.info(schools.toString());
		bookMap = new HashMap<>();
		for (Book bookIn : schools)
		{
			bookMap.put(bookIn.getName() + " - " + bookIn.getPublisherName(), bookIn);
		}

		txnData.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	public void initData(School school)
	{
		this.school = school; 
		Double balance = schoolService.fetchBalance(school);
		if(balance != null)
		{
		outBalance.setText(balance.toString());
		}
		schoolName.setText(school.getName());
		address.setText(school.addressText());
		txnData.setItems(FXCollections.observableList(schoolService.fetchOrderForSchool(school)));
		
		List<Sales> billDataList = schoolService.fetchBills(school);
		billData.setItems(FXCollections.observableList(billDataList));
	}

	@FXML
	void addOrder(ActionEvent e) throws IOException
	{

		FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createOrderFxmlFile);
		addOrderRoot = createOrderLoader.load();
		AddOrderController ctrl = createOrderLoader.getController();
		ctrl.initData(this.school, this.bookMap, null);
		addOrderScene = new Scene(addOrderRoot);
		prepareAndShowStage(e, addOrderScene);
	}

	@FXML
	void editOrder(ActionEvent e) throws IOException
	{
		FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createOrderFxmlFile);
		addOrderRoot = createOrderLoader.load();
		AddOrderController ctrl = createOrderLoader.getController();
		Order selectedOrder = txnData.getSelectionModel().getSelectedItem();
		ctrl.initData(this.school, this.bookMap, selectedOrder);
		// ctrl.updateFormData(selectedOrder);
		addOrderScene = new Scene(addOrderRoot);
		prepareAndShowStage(e, addOrderScene);
	}

	@FXML
	void deleteOrder(ActionEvent event)
	{

		Order selectedOrder = txnData.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Order Confirmation");
		alert.setHeaderText("Are you sure you want to delete the order: " + selectedOrder.getId());
		alert.setContentText("Click Ok to Delete");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			schoolService.deleteOrder(selectedOrder);
			initData(this.school);
		}
	}

	@FXML
	void createBill(ActionEvent event)
	{

		try
		{
			FXMLLoader createBillLoader = LoadUtils.loadFxml(this, addBillFxmlFile);
			Parent addBillRoot = createBillLoader.load();
			AddBillController ctrl = createBillLoader.getController();
			ObservableList<Order> selectedOrder = txnData.getSelectionModel().getSelectedItems();
			ctrl.initData(selectedOrder, this.school, null);
			Scene addBillScene = new Scene(addBillRoot);
			prepareAndShowStage(event, addBillScene);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@FXML
	void addBill(ActionEvent event)
	{
		try
		{
			FXMLLoader createBillLoader = LoadUtils.loadFxml(this, addBillFxmlFile);
			Parent addBillRoot = createBillLoader.load();
			AddBillController ctrl = createBillLoader.getController();
			ObservableList<Order> selectedOrder = txnData.getSelectionModel().getSelectedItems();
			ctrl.initData(selectedOrder, this.school, null);
			Scene addBillScene = new Scene(addBillRoot);
			prepareAndShowStage(event, addBillScene);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@FXML
	void editBill(ActionEvent event)
	{

		try
		{
			Sales selectedSale = billData.getSelectionModel().getSelectedItem();
			FXMLLoader createBillLoader = LoadUtils.loadFxml(this, addBillFxmlFile);
			Parent addBillRoot = createBillLoader.load();
			AddBillController ctrl = createBillLoader.getController();
			ObservableList<Order> selectedOrder = txnData.getSelectionModel().getSelectedItems();
			ctrl.initData(selectedOrder, this.school, selectedSale);
			Scene addBillScene = new Scene(addBillRoot);
			prepareAndShowStage(event, addBillScene);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@FXML
	void deleteBill(ActionEvent event)
	{
		Sales selectedSale = billData.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Bill Confirmation");
		alert.setHeaderText("Are you sure you want to delete the bill: " + selectedSale.getInvoiceNo());
		alert.setContentText("Click Ok to Delete");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			schoolService.deleteOrder(selectedSale);
			initData(this.school);
		}
	}

	@FXML
	void loadCreditNotes(Event e)
	{
		if (creditNoteTab.isSelected())
		{
			List<SchoolReturn> creditNotes = schoolService.fetchReturnsForSchool(this.school);
			creditNoteData.setItems(FXCollections.observableList(creditNotes));
		}
	}

	@FXML
	void addCredit(ActionEvent event)
	{
		try
		{
			FXMLLoader createReturnLoader = LoadUtils.loadFxml(this, addReturnFxmlFile);
			Parent addReturnRoot = createReturnLoader.load();
			AddReturnController ctrl = createReturnLoader.getController();
			ctrl.initData(this.school, null);
			Scene addCreditNoteScene = new Scene(addReturnRoot);
			prepareAndShowStage(event, addCreditNoteScene, returnEventHandler);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@FXML
	void editCredit(ActionEvent event)
	{
		try
		{
			FXMLLoader createReturnLoader = LoadUtils.loadFxml(this, addReturnFxmlFile);
			Parent addReturnRoot = createReturnLoader.load();
			AddReturnController ctrl = createReturnLoader.getController();
			ctrl.initData(this.school, this.creditNoteData.getSelectionModel().getSelectedItem());
			Scene addCreditNoteScene = new Scene(addReturnRoot);
			prepareAndShowStage(event, addCreditNoteScene, returnEventHandler);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@FXML
	void deleteCredit(ActionEvent event)
	{
		SchoolReturn selectedPayment = creditNoteData.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Return Confirmation");
		alert.setHeaderText("Are you sure you want to delete the Credit Note: " + selectedPayment.getAmount());
		alert.setContentText("Click Ok to Delete");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			schoolService.deleteReturn(selectedPayment);
			loadCreditNotes(event);
		}
	}

	@FXML
	void loadPayments(Event event)
	{
		if (paymentTab.isSelected())
		{
			List<SchoolPayment> paymentList = schoolService.fetchPayments(school);
			ObservableList<SchoolPayment> paymentItems = FXCollections.observableList(paymentList);
			paymentData.setItems(paymentItems);
		}
	}

	@FXML
	void addPayment(ActionEvent event)
	{
		try
		{
			FXMLLoader createBillLoader = LoadUtils.loadFxml(this, addPaymentFxmlFile);
			Parent addBillRoot = createBillLoader.load();
			AddPaymentController ctrl = createBillLoader.getController();
			ctrl.initData(this.school, null);
			Scene addBillScene = new Scene(addBillRoot);
			prepareAndShowStage(event, addBillScene, paymentEventHandler);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@FXML
	void editPayment(ActionEvent event)
	{

		try
		{
			FXMLLoader createBillLoader = LoadUtils.loadFxml(this, addPaymentFxmlFile);
			Parent addBillRoot = createBillLoader.load();
			AddPaymentController ctrl = createBillLoader.getController();
			SchoolPayment sPayment = paymentData.getSelectionModel().getSelectedItem();
			ctrl.initData(this.school, sPayment);
			Scene addBillScene = new Scene(addBillRoot);
			prepareAndShowStage(event, addBillScene, paymentEventHandler);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@FXML
	void deletePayment(ActionEvent event)
	{

		SchoolPayment selectedPayment = paymentData.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Payment Confirmation");
		alert.setHeaderText("Are you sure you want to delete the payment: " + selectedPayment.getAmount());
		alert.setContentText("Click Ok to Delete");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			schoolService.deletePayment(selectedPayment);
			loadPayments(event);
		}

	}

	@FXML
	void loadStatement(Event e)
	{
		if (statementTab.isSelected())
		{
			LocalDate toDateVal = LocalDate.now();
			LocalDate fromDateVal = toDateVal.minusMonths(6);
			fromDate.setValue(fromDateVal);
			toDate.setValue(toDateVal);
			List<SalesTransaction> txnItems = schoolService.fetchTransactions(school, fromDateVal, toDateVal);
			transactionData.setItems(FXCollections.observableList(txnItems));
		}
	}

	@FXML
	void generateStmt(ActionEvent event)
	{
		LocalDate toDateVal = toDate.getValue();
		LocalDate fromDateVal = fromDate.getValue();

		List<SalesTransaction> txnItems = schoolService.fetchTransactions(school, fromDateVal, toDateVal);
		transactionData.setItems(FXCollections.observableList(txnItems));
	}

	private void prepareAndShowStage(ActionEvent e, Scene childScene)
	{
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.setOnHiding(new EventHandler<WindowEvent>() {
			@Override
			public void handle(final WindowEvent event)
			{
				System.out.println("Calling close");
				initData(school);
			}
		});
		stage.show();
	}

	private void prepareAndShowStage(ActionEvent e, Scene childScene, EventHandler<WindowEvent> eventHandler)
	{
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.setOnHiding(eventHandler);
		stage.show();
	}

	private EventHandler<WindowEvent> paymentEventHandler = new EventHandler<WindowEvent>() {
		@Override
		public void handle(final WindowEvent event)
		{
			loadPayments(event);
		}
	};

	private EventHandler<WindowEvent> returnEventHandler = new EventHandler<WindowEvent>() {
		@Override
		public void handle(final WindowEvent event)
		{
			loadCreditNotes(event);
		}
	};
}

package com.matha.controller;

import static com.matha.util.UtilConstants.DELETED_STR;
import static com.matha.util.UtilConstants.addBillFxmlFile;
import static com.matha.util.UtilConstants.addPaymentFxmlFile;
import static com.matha.util.UtilConstants.addReturnFxmlFile;
import static com.matha.util.UtilConstants.buttonTypeCancel;
import static com.matha.util.UtilConstants.buttonTypeOne;
import static com.matha.util.UtilConstants.createOrderFxmlFile;
import static com.matha.util.UtilConstants.printOrderFxmlFile;
import static com.matha.util.UtilConstants.printSaleFxmlFile;
import static com.matha.util.UtilConstants.salesInvoiceJrxml;
import static com.matha.util.UtilConstants.*;
import static com.matha.util.Utils.*;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.matha.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.service.SchoolService;
import com.matha.util.LoadUtils;

import javafx.beans.binding.Bindings;
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
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sf.jasperreports.engine.JasperPrint;

@Component
public class SchoolDetailsController
{

	private Parent addOrderRoot;
	private Scene addOrderScene;
	private School school;

	@Autowired
	SchoolService schoolService;

	@FXML
	private TabPane saleTabs;

	@FXML
	private Tab ordersTab;

	@FXML
	private TableView<Order> txnData;

	@FXML
	private Tab billsTab;

	@FXML
	private TableView<Sales> billData;

	@FXML
	private TableColumn<Sales, String> amountColumn;

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

	private Map<String, Book> bookMap;
	private Collector<Book, ?, Map<String, Book>> bookMapCollector = toMap(o -> o.getShortName() + ": " + o.getName() + " - " + o.getPublisherName(), o -> o);

	// private JasperPrint jasperPrint;

	@FXML
	protected void initialize() throws IOException
	{

		List<Book> books = schoolService.fetchAllBooks();
		bookMap = books.stream().collect(bookMapCollector);

		txnData.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		this.amountColumn.setCellValueFactory(cellData -> Bindings.format("%.2f", cellData.getValue().getNetAmount()));

		this.billData.setRowFactory(row -> {

			TableRow<Sales> rowIn = new TableRow<Sales>() {
				@Override
				public void updateItem(Sales item, boolean empty)
				{
					super.updateItem(item, empty);
					setStyle("");
					if (item != null && !empty && item.getSalesTxn() != null && item.getSalesTxn().getTxnFlag() != null)
					{
						if (item.getSalesTxn().getTxnFlag().equals(DELETED_STR))
						{
							setStyle("-fx-background-color: pink");
						}
					}
				}
			};
			return rowIn;
		});
	}

	public void initData(School school)
	{
		this.school = school;
		this.schoolName.setText(school.getName());
		this.address.setText(school.addressText());
		this.txnData.setItems(FXCollections.observableList(schoolService.fetchOrderForSchool(school)));
		this.loadBalance();

		// List<String> saveTypes = Arrays.asList(PDF,Excel,Docx);
		// this.saveType.setItems(FXCollections.observableList(saveTypes));
		// this.saveType.getSelectionModel().selectFirst();
	}

	private void loadBalance()
	{
		Double balance = schoolService.fetchBalance(this.school);
		if (balance != null)
		{
			this.outBalance.setText(balance.toString());
		}
	}

	@FXML
	public void loadOrders()
	{
		if (this.ordersTab.isSelected())
		{
			List<Order> ordersIn = schoolService.fetchOrderForSchool(school);
			this.txnData.setItems(FXCollections.observableList(ordersIn.stream().sorted(comparing(Order::getOrderDate).reversed()).collect(toList())));
			this.loadBalance();
		}
		else
		{
			this.txnData.getSelectionModel().clearSelection();
		}
	}

	@FXML
	void addOrder(ActionEvent e) throws IOException
	{
		FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createOrderFxmlFile);
		addOrderRoot = createOrderLoader.load();
		AddOrderController ctrl = createOrderLoader.getController();
		ctrl.initData(this.school, this.bookMap, null);
		addOrderScene = new Scene(addOrderRoot);
		prepareAndShowStage(e, addOrderScene, orderEventHandler);
		saleTabs.getSelectionModel().select(ordersTab);
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
		prepareAndShowStage(e, addOrderScene, orderEventHandler);
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
	public void printOrder(ActionEvent ev)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, printOrderFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			PrintOrderController ctrl = createOrderLoader.getController();
			Order purchase = txnData.getSelectionModel().getSelectedItem();
			JasperPrint jasperPrint = ctrl.prepareJasperPrint(purchase);
			ctrl.initData(jasperPrint);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(ev, addOrderScene);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@FXML
	public void loadBills()
	{
		if (this.billsTab.isSelected())
		{
			List<Sales> billDataList = schoolService.fetchBills(this.school);
			this.billData.setItems(FXCollections.observableList(billDataList.stream().sorted(comparing(s -> ((Sales) s).getTxnDate()).reversed()).collect(toList())));
			this.loadBalance();
		}
		else
		{
			this.billData.getSelectionModel().clearSelection();
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
			ctrl.initData(selectedOrder, this.school, null, this.bookMap);
			Scene addBillScene = new Scene(addBillRoot);
			prepareAndShowStage(event, addBillScene, billEventHandler);
			saleTabs.getSelectionModel().select(billsTab);

			Sales savedSale = schoolService.fetchSale(ctrl.getSelectedSale().getId());
			showPrintDialog(savedSale, event);
		}
		catch (Exception e)
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
			ctrl.initData(selectedOrder, this.school, null, this.bookMap);
			Scene addBillScene = new Scene(addBillRoot);
			prepareAndShowStage(event, addBillScene, billEventHandler);

			Sales saleIn = schoolService.fetchSale(ctrl.getSelectedSale().getId());
			showPrintDialog(saleIn, event);

		}
		catch (Exception e)
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
			if (selectedSale.getSalesTxn() != null && selectedSale.getSalesTxn().getTxnFlag() != null)
			{
				showAlert(selectedSale);
				return;
			}
			FXMLLoader createBillLoader = LoadUtils.loadFxml(this, addBillFxmlFile);
			Parent addBillRoot = createBillLoader.load();
			AddBillController ctrl = createBillLoader.getController();
			ctrl.initData(null, this.school, selectedSale, this.bookMap);
			Scene addBillScene = new Scene(addBillRoot);
			prepareAndShowStage(event, addBillScene, billEventHandler);
			showPrintDialog(ctrl.getSelectedSale(), event);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private void showAlert(Sales selectedSale)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Edit Bill Error");
		alert.setHeaderText("Unable to edit a deleted bill: " + selectedSale.getSerialNo());
		alert.setContentText("Cannot Edit; Please use View option");
		alert.showAndWait();
	}

	@FXML
	void viewBill(ActionEvent event)
	{

		try
		{
			Sales selectedSale = billData.getSelectionModel().getSelectedItem();
			FXMLLoader createBillLoader = LoadUtils.loadFxml(this, viewBillFxmlFile);
			Parent addBillRoot = createBillLoader.load();
			ViewBillController ctrl = createBillLoader.getController();
			ctrl.initData(this.school, selectedSale);
			Scene addBillScene = new Scene(addBillRoot);
			prepareAndShowStage(event, addBillScene, billEventHandler);

		}
		catch (Exception e)
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
		alert.setHeaderText("Are you sure you want to delete the bill: " + selectedSale.getSerialNo());
		alert.setContentText("Click Ok to Delete");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			schoolService.deleteBill(selectedSale);
			loadBills();
		}
	}

	@FXML
	public void printPurchaseBill(ActionEvent ev)
	{
		try
		{
			Sales purchase = billData.getSelectionModel().getSelectedItem();
			printBill(purchase, ev);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@FXML
	void loadCreditNotes(Event e)
	{
		if (creditNoteTab.isSelected())
		{
			List<SchoolReturn> creditNotes = schoolService.fetchReturnsForSchool(this.school);
			creditNoteData.setItems(FXCollections.observableList(creditNotes.stream().sorted(comparing(SchoolReturn::getTxnDate).reversed()).collect(toList())));
			this.loadBalance();
		}
		else
		{
			creditNoteData.getSelectionModel().clearSelection();
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
			ctrl.initData(this.school, this.bookMap, null);
			Scene addCreditNoteScene = new Scene(addReturnRoot);
			prepareAndShowStage(event, addCreditNoteScene, returnEventHandler);

		}
		catch (Exception e)
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
			ctrl.initData(this.school, this.bookMap, this.creditNoteData.getSelectionModel().getSelectedItem());
			Scene addCreditNoteScene = new Scene(addReturnRoot);
			prepareAndShowStage(event, addCreditNoteScene, returnEventHandler);

		}
		catch (Exception e)
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
			ObservableList<SchoolPayment> paymentItems = FXCollections.observableList(paymentList.stream().sorted(comparing(SchoolPayment::getTxnDate).reversed()).collect(toList()));
			paymentData.setItems(paymentItems);
			this.loadBalance();
		}
		else
		{
			paymentData.getSelectionModel().clearSelection();
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

		}
		catch (Exception e)
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

		}
		catch (Exception e)
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
		try
		{
			LocalDate toDateVal = toDate.getValue();
			LocalDate fromDateVal = fromDate.getValue();

			List<SalesTransaction> txnItems = schoolService.fetchTransactions(school, fromDateVal, toDateVal);
			transactionData.setItems(FXCollections.observableList(txnItems));

			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, salesStmtFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			PrintSalesStmtController ctrl = createOrderLoader.getController();
			JasperPrint jasperPrint = ctrl.prepareJasperPrint(this.school, txnItems, fromDateVal, toDateVal);
			ctrl.initData(this.school, jasperPrint, fromDateVal, toDateVal);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// @FXML
	// public void exportAndSave(ActionEvent ev)
	// {
	// try
	// {
	//
	// String selection = saveType.getSelectionModel().getSelectedItem();
	// String filterStr = "*.*";
	// if(selection.equals(PDF))
	// {
	// filterStr = "*.pdf";
	// }
	// else if(selection.equals(Excel))
	// {
	// filterStr = "*.xls";
	// }
	// FileChooser fileChooser = new FileChooser();
	// fileChooser.setTitle("Save File");
	// fileChooser.getExtensionFilters().addAll(
	// new FileChooser.ExtensionFilter(selection, filterStr)
	// );
	//
	// File file = fileChooser.showSaveDialog(((Node)
	// ev.getSource()).getScene().getWindow());
	// if(selection.equals(PDF))
	// {
	// JasperExportManager.exportReportToPdfFile(jasperPrint,
	// file.getAbsolutePath());
	// }
	// else if(selection.equals(Excel))
	// {
	// JRXlsxExporter exporter = new JRXlsxExporter();
	// exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	// exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
	//
	// exporter.exportReport();
	// }
	// else if(selection.equals(Docx))
	// {
	// JRDocxExporter exporter = new JRDocxExporter();
	// exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	// exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
	//
	// exporter.exportReport();
	// }
	//
	// }
	// catch (Throwable e)
	// {
	// e.printStackTrace();
	// }
	// }

	private void printBill(Sales purchase, ActionEvent ev)
	{
		FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, printSaleFxmlFile);
		InputStream jasperStream = getClass().getResourceAsStream(salesInvoiceJrxml);
		Address salesAddr = schoolService.fetchAddress("Sales");
		Account acct = schoolService.fetchAccount("Matha Agencies");
		Scene addOrderScene = preparePrintScene(purchase, createOrderLoader, jasperStream, salesAddr, acct);
		prepareAndShowStage(ev, addOrderScene);
	}

	private void prepareAndShowStage(ActionEvent e, Scene childScene)
	{
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.setOnHiding(new EventHandler<WindowEvent>() {
			@Override
			public void handle(final WindowEvent event)
			{
				initData(school);
			}
		});
		stage.show();
	}

	private void prepareAndShowStage(ActionEvent e, Scene childScene, EventHandler<WindowEvent> eventHandler)
	{
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.setOnHiding(eventHandler);
		stage.showAndWait();
	}

	private EventHandler<WindowEvent> orderEventHandler = new EventHandler<WindowEvent>() {
		@Override
		public void handle(final WindowEvent event)
		{
			loadOrders();
		}
	};

	private EventHandler<WindowEvent> billEventHandler = new EventHandler<WindowEvent>() {
		@Override
		public void handle(final WindowEvent event)
		{
			loadBills();
		}
	};

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

	private void showPrintDialog(Sales sales, ActionEvent event)
	{
		if (sales == null)
		{
			return;
		}

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Print Bill Confirmation");
		alert.setHeaderText("Do you want to print the bill?");
		alert.setContentText("Click Yes to Print");
		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne)
		{
			printBill(sales, event);
		}
	}
}
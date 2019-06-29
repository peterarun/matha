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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;

import com.matha.domain.*;
import com.matha.util.Utils;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

@Component
public class SchoolDetailsController
{

	private static final Logger LOGGER = LogManager.getLogger(SchoolDetailsController.class);

	private Parent addOrderRoot;
	private Scene addOrderScene;
	private School school;

	@Autowired
	SchoolService schoolService;

	@Value("${agencyName}")
	private String agencyName;

	@Value("${agencyAddress1}")
	private String agencyAddress1;

	@Value("${agencyAddress2}")
	private String agencyAddress2;

	@Value("${purPaymentModes}")
	private String[] schoolPaymentModes;

	@Value("${salesBankDetails}")
	private String salesBankDetails;

	@Value("#{'${datedPurPaymentModes}'.split(',')}")
	private List<String> datedSchoolPaymentModes;

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
	private DatePicker fromDateSt;

	@FXML
	private DatePicker toDateSt;

	@FXML
	private ChoiceBox<String> saveType;

	@FXML
	private WebView invoiceData;

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
	private Collector<Book, ?, Map<String, Book>> bookMapCollector = toMap(o -> o.getBookNum() + ": " + o.getName() + " - " + o.getPublisherName(), o -> o);
	private JasperPrint print;

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
					if (item != null && !empty && item.getStatusStr() != null)
					{
						if (item.getStatusStr().equals(DELETED_STR))
						{
							setStyle("-fx-background-color: pink");
						}
					}
				}
			};
			return rowIn;
		});

		List<String> saveTypes = Arrays.asList(PDF,Excel,Docx);
		this.saveType.setItems(FXCollections.observableList(saveTypes));
		this.saveType.getSelectionModel().selectFirst();
	}

	public void initData(School school)
	{
		this.school = school;
		this.schoolName.setText(school.getName());
		this.address.setText(school.addressText());
		this.txnData.setItems(FXCollections.observableList(schoolService.fetchOrderForSchool(school)));
		this.loadBalance();
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

			this.txnData.setOnMouseClicked(ev -> {
				if(verifyDblClick(ev)) editOrder(ev) ;
			});
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
		prepareAndShowStage(e, addOrderScene, ev ->loadOrders());
		saleTabs.getSelectionModel().select(ordersTab);
	}

	@FXML
	void editOrder(Event e)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createOrderFxmlFile);
			addOrderRoot = createOrderLoader.load();
			AddOrderController ctrl = createOrderLoader.getController();
			Order selectedOrder = txnData.getSelectionModel().getSelectedItem();
			ctrl.initData(this.school, this.bookMap, selectedOrder);
			// ctrl.updateFormData(selectedOrder);
			addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(e, addOrderScene, ev ->loadOrders());
		}
		catch (Exception ex)
		{
			showErrorAlert("Error", "An Error Occurred", "An Error ocurred while trying to edit the order");
		}
	}


	@FXML
	void copyOrder(Event e)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createOrderFxmlFile);
			addOrderRoot = createOrderLoader.load();
			AddOrderController ctrl = createOrderLoader.getController();
			Order selectedOrder = txnData.getSelectionModel().getSelectedItem();
			selectedOrder.setId(null);
			selectedOrder.setSerialNo(null);
			ctrl.initData(this.school, this.bookMap, selectedOrder);
			// ctrl.updateFormData(selectedOrder);
			addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(e, addOrderScene, ev ->loadOrders());
		}
		catch (Exception ex)
		{
			showErrorAlert("Error", "An Error Occurred", "An Error ocurred while trying to edit the order");
		}
	}

	@FXML
	void deleteOrder(ActionEvent event)
	{
		Order selectedOrder = txnData.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Order Confirmation");
		alert.setHeaderText("Are you sure you want to delete the order: " + selectedOrder.getSerialNo());
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
			LOGGER.error("Error...", e);
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

			this.billData.setOnMouseClicked(ev -> {
				if(verifyDblClick(ev)) editBill(ev);
			});
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
			prepareAndShowStage(event, addBillScene, ev ->loadBills());
			saleTabs.getSelectionModel().select(billsTab);

			if(ctrl.getSelectedSale() != null && ctrl.getSelectedSale().getId() != null)
			{
				Sales savedSale = schoolService.fetchSale(ctrl.getSelectedSale().getId());
				showPrintDialog(savedSale, event);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
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
			prepareAndShowStage(event, addBillScene, ev ->loadBills());

			if(ctrl.getSelectedSale() != null && ctrl.getSelectedSale().getId() != null)
			{
				Sales saleIn = schoolService.fetchSale(ctrl.getSelectedSale().getId());
				showPrintDialog(saleIn, event);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void editBill(Event event)
	{
		try
		{
			Sales selectedSale = billData.getSelectionModel().getSelectedItem();
			if (selectedSale.getStatusInd() != null && selectedSale.getStatusInd().equals(DELETED_IND))
			{
				showAlert(selectedSale);
				return;
			}
			FXMLLoader createBillLoader = LoadUtils.loadFxml(this, addBillFxmlFile);
			Parent addBillRoot = createBillLoader.load();
			AddBillController ctrl = createBillLoader.getController();
			ctrl.initData(null, this.school, selectedSale, this.bookMap);
			Scene addBillScene = new Scene(addBillRoot);
			prepareAndShowStage(event, addBillScene, ev ->loadBills());
			if(ctrl.getSelectedSale() != null && ctrl.getSelectedSale().getId() != null)
			{
				Sales saleIn = schoolService.fetchSale(ctrl.getSelectedSale().getId());
				showPrintDialog(saleIn, event);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
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
			prepareAndShowStage(event, addBillScene, ev -> loadBills());

		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
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
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void loadCreditNotes(Event e)
	{
		if (this.creditNoteTab.isSelected())
		{
			List<SchoolReturn> creditNotes = this.schoolService.fetchReturnsForSchool(this.school);
			this.creditNoteData.setItems(FXCollections.observableList(creditNotes.stream().sorted(comparing(SchoolReturn::getTxnDate).reversed()).collect(toList())));
			this.loadBalance();

			this.creditNoteData.setOnMouseClicked(ev -> {
				if(verifyDblClick(ev)) editCredit(ev); ;
			});
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
			prepareAndShowStage(event, addCreditNoteScene, ev ->loadCreditNotes(ev));

		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

	}

	@FXML
	void editCredit(Event event)
	{
		try
		{
			FXMLLoader createReturnLoader = LoadUtils.loadFxml(this, addReturnFxmlFile);
			Parent addReturnRoot = createReturnLoader.load();
			AddReturnController ctrl = createReturnLoader.getController();
			ctrl.initData(this.school, this.bookMap, this.creditNoteData.getSelectionModel().getSelectedItem());
			Scene addCreditNoteScene = new Scene(addReturnRoot);
			prepareAndShowStage(event, addCreditNoteScene, ev ->loadCreditNotes(ev));
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void printCreditNote(ActionEvent ev)
	{
		try
		{
			SchoolReturn purchase = this.creditNoteData.getSelectionModel().getSelectedItem();
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, printCreditNoteFxmlFile);

			InputStream jasperStream = getClass().getResourceAsStream(creditNoteJrxml);
			Address salesAddr = schoolService.fetchAddress("Sales");

			Parent addOrderRoot = createOrderLoader.load();
			PrintCreditNoteController ctrl = createOrderLoader.getController();
			ctrl.initData(purchase.getSalesTxn().getSchool(), purchase, salesAddr, salesBankDetails, jasperStream);
			addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(ev, addOrderScene);
		}
		catch (Exception e)
		{
			LOGGER.error("Error....", e);
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
		if (this.paymentTab.isSelected())
		{
			List<SchoolPayment> paymentList = this.schoolService.fetchPayments(this.school);
			ObservableList<SchoolPayment> paymentItems = FXCollections.observableList(paymentList.stream().sorted(comparing(SchoolPayment::getTxnDate).reversed()).collect(toList()));
			this.paymentData.setItems(paymentItems);
			this.loadBalance();

			this.paymentData.setOnMouseClicked(ev -> {
				if(verifyDblClick(ev)) editPayment(ev); ;
			});
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
			prepareAndShowStage(event, addBillScene, ev ->loadPayments(ev));

		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

	}

	@FXML
	void editPayment(Event event)
	{
		try
		{
			FXMLLoader createBillLoader = LoadUtils.loadFxml(this, addPaymentFxmlFile);
			Parent addBillRoot = createBillLoader.load();
			AddPaymentController ctrl = createBillLoader.getController();
			SchoolPayment sPayment = paymentData.getSelectionModel().getSelectedItem();
			ctrl.initData(this.school, sPayment);
			Scene addBillScene = new Scene(addBillRoot);
			prepareAndShowStage(event, addBillScene, ev -> loadPayments(ev));

		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
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
	void loadStatement(Event ev)
	{
		if (statementTab.isSelected())
		{
			try
			{
				LocalDate toDateVal = LocalDate.now();
				LocalDate fromDateVal = toDateVal.minusMonths(6);

				List<SalesTransaction> tableData = schoolService.fetchTransactions(school, fromDateVal, toDateVal);

				JasperPrint print = prepareJasperPrint(school, tableData, fromDateVal, toDateVal);
				this.print = print;
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				HtmlExporter exporter = new HtmlExporter();
				exporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.exportReport();

				String content = StringUtils.toEncodedString(outputStream.toByteArray(), Charset.defaultCharset());
				invoiceData.getEngine().loadContent(content);
			}
			catch (Exception e)
			{
				LOGGER.error("Error...", e);
				e.printStackTrace();
			}
		}
	}

//	@FXML
	void generateStmtOld(ActionEvent event)
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
			List<SalesTransaction> tableData = schoolService.fetchTransactions(school, fromDateVal, toDateVal);
			ctrl.initData(this.school, tableData, fromDateVal, toDateVal);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene);
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	private void printBill(Sales purchase, Event ev)
	{
		FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, printSaleFxmlFile);
		InputStream jasperStream = getClass().getResourceAsStream(salesInvoiceJrxml);
		Address salesAddr = schoolService.fetchAddress("Sales");
		Scene addOrderScene = preparePrintScene(purchase, createOrderLoader, jasperStream, salesAddr, salesBankDetails);
		prepareAndShowStage(ev, addOrderScene);
	}

	private void prepareAndShowStage(Event e, Scene childScene)
	{
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.setOnHiding(ev -> initData(school));
//				new EventHandler<WindowEvent>() {
//			@Override
//			public void handle(final WindowEvent event)
//			{
//				initData(school);
//			}
//		});
		stage.show();
	}

	private void prepareAndShowStage(Event e, Scene childScene, EventHandler<WindowEvent> eventHandler)
	{
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.setOnHiding(eventHandler);
		stage.showAndWait();
	}

//	private EventHandler<WindowEvent> orderEventHandler = new EventHandler<WindowEvent>() {
//		@Override
//		public void handle(final WindowEvent event)
//		{
//			loadOrders();
//		}
//	};
//
//	private EventHandler<WindowEvent> billEventHandler = new EventHandler<WindowEvent>() {
//		@Override
//		public void handle(final WindowEvent event)
//		{
//			loadBills();
//		}
//	};
//
//	private EventHandler<WindowEvent> paymentEventHandler = new EventHandler<WindowEvent>() {
//		@Override
//		public void handle(final WindowEvent event)
//		{
//			loadPayments(event);
//		}
//	};
//
//	private EventHandler<WindowEvent> returnEventHandler = new EventHandler<WindowEvent>() {
//		@Override
//		public void handle(final WindowEvent event)
//		{
//			loadCreditNotes(event);
//		}
//	};

	private void showPrintDialog(Sales sales, Event event)
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

	public JasperPrint prepareJasperPrint(School school, List<SalesTransaction> tableDataIn, LocalDate fromDateVal, LocalDate toDateVal)
	{
		JasperPrint jasperPrint = null;
		InputStream jasperStream = getClass().getResourceAsStream(salesStmtJrxml);
		HashMap<String, Object> hm = new HashMap<>();
		try
		{
			if (toDateVal == null)
			{
				LOGGER.debug("Null toDateVal");
				toDateVal = LocalDate.now();
			}

			hm.put("agencyName", agencyName);
			hm.put("agencyAddress1", agencyAddress1);
			hm.put("agencyAddress2", agencyAddress2);
			hm.put("schoolName", school.getAddress1());
			hm.put("schoolDetails", school.getStmtAddress());
			hm.put("fromDate", DATE_CONV.toString(fromDateVal));
			hm.put("toDate", DATE_CONV.toString(toDateVal));
			hm.put("datedSchoolPaymentModes", datedSchoolPaymentModes);

			JasperReport compiledFile = JasperCompileManager.compileReport(jasperStream);
			LOGGER.info("Total Size: " + tableDataIn.size());
			for (int i = 0; i <= tableDataIn.size() / 38; i++)
			{
				int fromIndex = i * 38;
				int toIndex = Math.min(fromIndex + 38, tableDataIn.size());
				List<SalesTransaction> tableData = tableDataIn.subList(fromIndex, toIndex);
				LOGGER.debug("i: " + i + " fromIndex: " + fromIndex + " toIndex: " + toIndex + " tabSize: " + tableData.size());

				Map<String, Object> hmOut = Utils.prepareSalesStmtParmMap(hm, tableData);
				LOGGER.debug("hmOut");
				for (Map.Entry<String, Object> obj : hmOut.entrySet())
				{
					LOGGER.debug(obj.getKey());
					LOGGER.debug(obj.getValue());
				}

				if(jasperPrint == null)
				{
					jasperPrint = JasperFillManager.fillReport(compiledFile, hmOut, new JRBeanCollectionDataSource(tableData));
				}
				else
				{
					JasperPrint jasperPrintNxt = JasperFillManager.fillReport(compiledFile, hmOut, new JRBeanCollectionDataSource(tableData));
					jasperPrint.addPage(jasperPrintNxt.getPages().get(0));
				}
			}
		}
		catch (JRException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

		return jasperPrint;
	}

	@FXML
	void generateStmt(ActionEvent event)
	{
		LocalDate fromDateVal = this.fromDate.getValue();
		LocalDate toDateVal = this.toDate.getValue();
		List<SalesTransaction> tableData = schoolService.fetchTransactions(school, fromDateVal, toDateVal);
		JasperPrint jPrint = prepareJasperPrint(this.school, tableData, fromDateVal, toDateVal);
		this.loadWebInvoice(jPrint);
	}


	@FXML
	public void exportAndSave(ActionEvent ev)
	{
		try
		{

			String selection = saveType.getSelectionModel().getSelectedItem();
			String filterStr = "*.*";
			if(selection.equals(PDF))
			{
				filterStr = "*.pdf";
			}
			else if(selection.equals(Excel))
			{
				filterStr = "*.xls";
			}
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save File");
			fileChooser.setInitialFileName(this.school.getName() + "_statement");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter(selection, filterStr)
			);

			File file = fileChooser.showSaveDialog(((Node) ev.getSource()).getScene().getWindow());
			if(file == null)
			{
				return;
			}
			if(selection.equals(PDF))
			{
				JasperExportManager.exportReportToPdfFile(print, file.getAbsolutePath());
			}
			else if(selection.equals(Excel))
			{
				JRXlsxExporter exporter = new JRXlsxExporter();
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));

				exporter.exportReport();
			}
			else if(selection.equals(Docx))
			{
				JRDocxExporter exporter = new JRDocxExporter();
				exporter.setExporterInput(new SimpleExporterInput(print));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));

				exporter.exportReport();
			}

		}
		catch (Throwable e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	public void printStmt(ActionEvent ev)
	{
		try
		{
			printJasper(print);
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	private void loadWebInvoice(JasperPrint printIn)
	{
		try
		{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			HtmlExporter exporter = new HtmlExporter();
			exporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
			exporter.setExporterInput(new SimpleExporterInput(printIn));
			exporter.exportReport();

			String content = StringUtils.toEncodedString(outputStream.toByteArray(), Charset.defaultCharset());
			this.invoiceData.getEngine().loadContent(content);

		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

	}

}
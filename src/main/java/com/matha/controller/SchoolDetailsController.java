package com.matha.controller;

import static com.matha.util.UtilConstants.NEW_LINE;
import static com.matha.util.UtilConstants.addBillFxmlFile;
import static com.matha.util.UtilConstants.addPaymentFxmlFile;
import static com.matha.util.UtilConstants.addReturnFxmlFile;
import static com.matha.util.UtilConstants.createOrderFxmlFile;
import static com.matha.util.UtilConstants.printSaleFxmlFile;
import static com.matha.util.UtilConstants.salesInvoiceJrxml;
import static com.matha.util.Utils.getStringVal;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.Book;
import com.matha.domain.Order;
import com.matha.domain.OrderItem;
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
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

		List<Book> schools = schoolService.fetchAllBooks();
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
			this.txnData.setItems(FXCollections.observableList(schoolService.fetchOrderForSchool(school)));
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
	public void loadBills()
	{
		if (this.billsTab.isSelected())
		{
			List<Sales> billDataList = schoolService.fetchBills(this.school);
			this.billData.setItems(FXCollections.observableList(billDataList));
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
			ctrl.initData(selectedOrder, this.school, null);
			Scene addBillScene = new Scene(addBillRoot);
			prepareAndShowStage(event, addBillScene, billEventHandler);
			saleTabs.getSelectionModel().select(billsTab);
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
			ctrl.initData(selectedOrder, this.school, null);
			Scene addBillScene = new Scene(addBillRoot);
			prepareAndShowStage(event, addBillScene, billEventHandler);

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
			FXMLLoader createBillLoader = LoadUtils.loadFxml(this, addBillFxmlFile);
			Parent addBillRoot = createBillLoader.load();
			AddBillController ctrl = createBillLoader.getController();
			ctrl.initData(null, this.school, selectedSale);
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
		alert.setHeaderText("Are you sure you want to delete the bill: " + selectedSale.getInvoiceNo());
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
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, printSaleFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			PrintSalesBillController ctrl = createOrderLoader.getController();
			Sales purchase = billData.getSelectionModel().getSelectedItem();
			JasperPrint jasperPrint = prepareJasperPrint(purchase.getSalesTxn().getSchool(), purchase);
			ctrl.initData(jasperPrint);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(ev, addOrderScene);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	private JasperPrint prepareJasperPrint(School sch, Sales sale)
	{

		JasperPrint jasperPrint = null;
		InputStream jasperStream = getClass().getResourceAsStream(salesInvoiceJrxml);
		HashMap<String, Object> hm = new HashMap<>();
		try
		{			
			Set<OrderItem> tableData = sale.getOrderItems();
			SalesTransaction txn = sale.getSalesTxn();
			Set<String> orderIdSet = tableData.stream().map(OrderItem::getOrder).map(Order::getSerialNo).collect(Collectors.toSet());
			String orderIds = String.join(",", orderIdSet);
			Double subTotal = sale.getSubTotal();
			Double discAmt = sale.getDiscAmt();
			if(discAmt != null)
			{
				discAmt = sale.getDiscType() ? subTotal * discAmt /100 : subTotal - discAmt;  
			}
			 			
			hm.put("partyName", sch.getName());
			hm.put("partyAddress", sch.addressText());
			hm.put("agencyName", "MATHA DISTRIBUTORS.");
			hm.put("agencyDetails", "No.88, 8th Street, A.K.Swamy Nagar, " + NEW_LINE + "Kilpauk, " + NEW_LINE + "Chennai - 600010");
			hm.put("partyPhone", sch.getPhone1() == null ? sch.getPhone2() : sch.getPhone1());
			hm.put("documentsThrough", sale.getDocsThru());
			hm.put("invoiceNo", getStringVal(sale.getInvoiceNo()));
			hm.put("txnDate", txn.getTxnDate());
			hm.put("orderNumbers", orderIds);
			hm.put("despatchedPer", sale.getDespatch());
			hm.put("grNo", sale.getGrNum());
			hm.put("packageCnt", getStringVal(sale.getPackages()));
			hm.put("total", sale.getSubTotal());
			hm.put("discount", discAmt);
			hm.put("grandTotal", sale.getNetAmount());
			
			JasperReport compiledFile = JasperCompileManager.compileReport(jasperStream);

			jasperPrint = JasperFillManager.fillReport(compiledFile, hm, new JRBeanCollectionDataSource(tableData));
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return jasperPrint;
	
	}

	@FXML
	void loadCreditNotes(Event e)
	{
		if (creditNoteTab.isSelected())
		{
			List<SchoolReturn> creditNotes = schoolService.fetchReturnsForSchool(this.school);
			creditNoteData.setItems(FXCollections.observableList(creditNotes));
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
			ctrl.initData(this.school, null);
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
			ctrl.initData(this.school, this.creditNoteData.getSelectionModel().getSelectedItem());
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
			ObservableList<SchoolPayment> paymentItems = FXCollections.observableList(paymentList);
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
}

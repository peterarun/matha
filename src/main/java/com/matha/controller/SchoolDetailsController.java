package com.matha.controller;

import static com.matha.util.UtilConstants.DELETED_STR;
import static com.matha.util.UtilConstants.addBillFxmlFile;
import static com.matha.util.UtilConstants.addPaymentFxmlFile;
import static com.matha.util.UtilConstants.addReturnFxmlFile;
import static com.matha.util.UtilConstants.buttonTypeCancel;
import static com.matha.util.UtilConstants.buttonTypeOne;
import static com.matha.util.UtilConstants.createOrderFxmlFile;
import static com.matha.util.UtilConstants.printSaleFxmlFile;
import static com.matha.util.UtilConstants.salesInvoiceJrxml;
import static com.matha.util.UtilConstants.viewBillFxmlFile;
import static com.matha.util.Utils.preparePrintScene;

import java.io.IOException;
import java.io.InputStream;
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
import javafx.scene.control.Labeled;
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

	private HashMap<String, Book> bookMap;
//	private JasperPrint jasperPrint;

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
		this.amountColumn.setCellValueFactory(cellData -> 
			Bindings.format("%.2f", cellData.getValue().getNetAmount())
		);
				
		this.billData.setRowFactory(row -> {
			
			TableRow<Sales> rowIn = new TableRow<Sales>()
		{
		    @Override
		    public void updateItem(Sales item, boolean empty)
		    {
		    	System.out.println("empty.." + empty + " " + item);
		        super.updateItem(item, empty);

		        if (item != null && !empty && item.getSalesTxn() != null && item.getSalesTxn().getTxnFlag() != null) 
		        {	
		        	System.out.println(item.getSalesTxn() + " " + item.getSalesTxn().getTxnFlag());
		            if (item.getSalesTxn().getTxnFlag().equals(DELETED_STR)) 
		            {
		            	setStyle("-fx-background-color: pink");
//		                for(int i=0; i<getChildren().size();i++)
//		                {
//		                    ((Labeled) getChildren().get(i)).setStyle("-fx-background-color: pink");
//		                }                        
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
		
//		List<String> saveTypes = Arrays.asList(PDF,Excel,Docx);
//		this.saveType.setItems(FXCollections.observableList(saveTypes));
//		this.saveType.getSelectionModel().selectFirst();
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
			ctrl.initData(selectedOrder, this.school, null, this.bookMap);
			Scene addBillScene = new Scene(addBillRoot);
			prepareAndShowStage(event, addBillScene, billEventHandler);
			saleTabs.getSelectionModel().select(billsTab);
			showPrintDialog(ctrl.getSelectedSale(), event);
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
			showPrintDialog(ctrl.getSelectedSale(), event);
						
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
			if(selectedSale.getSalesTxn().getTxnFlag() != null)
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
		alert.setHeaderText("Unable to edit a deleted bill: " + selectedSale.getInvoiceNo());
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

//	@FXML
//	public void exportAndSave(ActionEvent ev)
//	{
//		try
//		{
//
//			String selection = saveType.getSelectionModel().getSelectedItem(); 
//			String filterStr = "*.*";
//			if(selection.equals(PDF))
//			{
//				filterStr = "*.pdf";
//			}
//			else if(selection.equals(Excel))
//			{
//				filterStr = "*.xls";
//			}
//			FileChooser fileChooser = new FileChooser();
//			fileChooser.setTitle("Save File");
//            fileChooser.getExtensionFilters().addAll(
//                    new FileChooser.ExtensionFilter(selection, filterStr)
//                );
//            
//			File file = fileChooser.showSaveDialog(((Node) ev.getSource()).getScene().getWindow());
//			if(selection.equals(PDF))
//			{
//				JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
//			}
//			else if(selection.equals(Excel))
//			{
//		        JRXlsxExporter exporter = new JRXlsxExporter();
//		        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));		        
//				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
//
//		        exporter.exportReport();
//			}
//			else if(selection.equals(Docx))
//			{
//				JRDocxExporter exporter = new JRDocxExporter();
//		        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));		        
//				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
//
//		        exporter.exportReport();
//			}
//			
//		}
//		catch (Throwable e)
//		{
//			e.printStackTrace();
//		}
//	}
	

	private void printBill(Sales purchase, ActionEvent ev)
	{
		FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, printSaleFxmlFile);			
		InputStream jasperStream = getClass().getResourceAsStream(salesInvoiceJrxml);
		Scene addOrderScene = preparePrintScene(purchase, createOrderLoader, jasperStream, schoolService);
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
		if(sales == null)
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
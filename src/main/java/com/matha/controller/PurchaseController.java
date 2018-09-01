package com.matha.controller;

import static com.matha.util.UtilConstants.*;
import static com.matha.util.Utils.*;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.matha.domain.*;
import javafx.event.Event;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.matha.service.SchoolService;
import com.matha.util.Converters;
import com.matha.util.LoadUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;

@Component
public class PurchaseController
{

	private static final int ROWS_PER_PAGE = 10;
	private static final Logger LOGGER = LogManager.getLogger(PurchaseController.class);

	@Value("#{'${datedPurPaymentModes}'.split(',')}")
	private List<String> datedSchoolPaymentModes;

	@Value("${defaultPublisher}")
	private String defaultPublisher;

	@Autowired
	private SchoolService schoolService;

	@FXML
	private ChoiceBox<Publisher> publishers;

	@FXML
	private TextArea publisherDet;

    @FXML
    private TabPane purTabs;
    
	@FXML
	private Tab ordersTab;

	@FXML
	private AnchorPane ordersPane;

	@FXML
	private TableView<Order> orderTable;
	
	@FXML
	private Pagination orderPaginator;

	@FXML
	private TextField orderTyped;
	
	@FXML
	private Tab purchaseBillTab;

    @FXML
    private CheckBox billedToggle;
    
	@FXML
	private TableView<Purchase> purchaseData;

	@FXML
	private Pagination billPaginator;

	@FXML
	private Tab returnsTab;

	@FXML
	private TableView<PurchaseReturn> returnsData;

	@FXML
	private Tab paymentTab;

	@FXML
	private TableView<PurchasePayment> paymentData;

	@FXML
	private Tab statementTab;

	@FXML
	private Tab statementTabHtml;

	@FXML
	private DatePicker fromDate;

	@FXML
	private DatePicker toDate;

	@FXML
	private ChoiceBox<String> saveType;

	@FXML
	private WebView reportData;

	private JasperPrint print;
//	private Map<String, Publisher> pubMap;
	private Map<String, Book> bookMap;
	private Collector<Book, ?, Map<String, Book>> bookMapCollector = toMap(o -> o.getBookNum() + ": " + o.getName() + " - " + o.getPublisherName(), o -> o);

	@FXML
	protected void initialize()
	{
		orderTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		List<Publisher> allPublishers = schoolService.fetchAllPublishers();
		publishers.setConverter(Converters.getPublisherConverter());
		publishers.setItems(FXCollections.observableList(allPublishers));

		Map<String, Publisher> pubMap = allPublishers.stream().collect(toMap(Publisher::getName, pub -> pub));

		Optional<String> pubSel = selectPublisher(pubMap);
		if(pubSel.isPresent())
		{
			publishers.getSelectionModel().select(pubMap.get(pubSel.get()));
		}
		else
		{
			return;
		}
		
		
		LocalDate toDateVal = LocalDate.now();
		LocalDate fromDateVal = toDateVal.minusMonths(6);
		fromDate.setValue(fromDateVal);
		toDate.setValue(toDateVal);

		orderPaginator.setPageFactory(this::createPage);
		billPaginator.setPageFactory(this::createBillPage);

		List<String> saveTypes = Arrays.asList(PDF,Excel,Docx);
		this.saveType.setItems(FXCollections.observableList(saveTypes));
		this.saveType.getSelectionModel().selectFirst();

		this.ordersPane.prefHeightProperty().bind(this.purTabs.heightProperty());
	}

	private Node createPage(int pageIndex)
	{
		loadOrderTable(pageIndex);

		if(orderTable.getItems() != null && !orderTable.getItems().isEmpty())
		{
			orderTable.prefHeightProperty().set(ROWS_PER_PAGE * orderTable.getFixedCellSize() + 30);
		}
		return new BorderPane(orderTable);
	}

	private Node createBillPage(int pageIndex)
	{
		loadPurchases(pageIndex);

		if(purchaseData.getItems() != null && !purchaseData.getItems().isEmpty())
		{
			purchaseData.prefHeightProperty().set(ROWS_PER_PAGE * purchaseData.getFixedCellSize() + 30);
		}
		return new BorderPane(purchaseData);
	}

	private Node createPageSearch(int pageIndex)
	{
		loadOrderSearchTable(pageIndex);

		if(orderTable.getItems() != null && !orderTable.getItems().isEmpty())
		{
			orderTable.prefHeightProperty().set(ROWS_PER_PAGE * orderTable.getFixedCellSize() + 30);
		}
		return new BorderPane(orderTable);
	}
	
	@FXML
	void changedState(ActionEvent event)
	{
		Publisher pub = publishers.getSelectionModel().getSelectedItem();
		publisherDet.setText(pub.getAddress());
		
		orderPaginator.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
		purTabs.getSelectionModel().select(ordersTab);

		List<Book> allBooks = schoolService.fetchBooksForPublisher(pub);
		this.bookMap = allBooks.stream().collect(bookMapCollector);
	}

	@FXML
	void addPublisher(ActionEvent event)
	{

		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, addPublisherFxml);
			Parent addOrderRoot = createOrderLoader.load();
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
	void editPublisher(ActionEvent event)
	{

		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, addPublisherFxml);
			Parent addOrderRoot = createOrderLoader.load();
			AddPublisherController ctrl = createOrderLoader.getController();
			ctrl.initData(this.publishers.getSelectionModel().getSelectedItem());
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
	void editOrder(Event event)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createOrderFxmlFile);
			Parent addOrderRoot;

			addOrderRoot = createOrderLoader.load();

			AddOrderController ctrl = createOrderLoader.getController();
			Order order = orderTable.getSelectionModel().getSelectedItem();

			ctrl.initData(order.getSchool(), this.bookMap, order);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene);
		}
		catch (IOException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void searchOrder(ActionEvent event)
	{
		orderPaginator.setPageFactory((Integer pageIndex) -> createPageSearch(pageIndex));
	}
	
	@FXML
	void createPurchase(ActionEvent event)
	{

		try
		{
			ObservableList<Order> selectedOrders = orderTable.getSelectionModel().getSelectedItems();
			Set<Order> orderSet = new HashSet<>(selectedOrders);

			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createPurchaseFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddPurchaseBillController ctrl = createOrderLoader.getController();
			ctrl.initData(orderSet, this.publishers.getSelectionModel().getSelectedItem(), null, this.bookMap);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene, ev -> loadPurchases(this.billPaginator.getCurrentPageIndex()));
			
			purTabs.getSelectionModel().select(purchaseBillTab);
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void editPurchase(Event event)
	{
		try
		{
			Purchase purchase = this.purchaseData.getSelectionModel().getSelectedItem();

			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createPurchaseFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddPurchaseBillController ctrl = createOrderLoader.getController();
			ctrl.initData(null, this.publishers.getSelectionModel().getSelectedItem(), purchase, this.bookMap);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene, ev -> loadPurchases(this.billPaginator.getCurrentPageIndex()));

		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

	}

	@FXML
	void deletePurchase(ActionEvent event)
	{
		Purchase purchase = this.purchaseData.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Purchase Bill Confirmation");
		alert.setHeaderText("Are you sure you want to delete the purchase: " + purchase.getInvoiceNo());
		alert.setContentText("Click Ok to Delete");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			schoolService.deletePurchase(purchase);
			int pageIdx = this.billPaginator.getCurrentPageIndex();
			loadPurchases(pageIdx);
		}
	}

	
	@FXML
	public void printPurchaseBill(ActionEvent ev)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, printPurchaseFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			PrintPurchaseBillController ctrl = createOrderLoader.getController();
			Purchase purchase = purchaseData.getSelectionModel().getSelectedItem();
			Address salesAddr = schoolService.fetchAddress("Purchase");
			InputStream iStream = getClass().getResourceAsStream(invoiceJrxml);
			JasperPrint jasperPrint = prepareJasperPrint(purchase.getPublisher(), purchase, salesAddr, iStream);
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

	private void loadOrderTable(int idx)
	{
		Publisher pub = publishers.getSelectionModel().getSelectedItem();
		if(pub == null)
		{
			return;
		}
		Page<Order> orderPages = schoolService.fetchOrders(pub, idx, ROWS_PER_PAGE, billedToggle.isSelected());
		List<Order> orderList = orderPages.getContent();
		this.orderPaginator.setPageCount(orderPages.getTotalPages());
		this.orderTable.setItems(FXCollections.observableList(orderList));

		this.orderTable.setOnMouseClicked(ev -> {
			if(verifyDblClick(ev)) editOrder(ev);
		});
	}

	private void loadOrderSearchTable(int idx)
	{		
		Publisher pub = publishers.getSelectionModel().getSelectedItem();
		List<Order> orderList = schoolService.fetchOrderSearch(pub, idx, ROWS_PER_PAGE, billedToggle.isSelected(), orderTyped.getText()).getContent();
		orderTable.setItems(FXCollections.observableList(orderList));
	}

	@FXML
	public void loadOrders()
	{
		if (ordersTab.isSelected())
		{
			int idx = orderPaginator.getCurrentPageIndex();
			loadOrderTable(idx);
		}
		else
		{
			orderTable.getSelectionModel().clearSelection();
		}
	}


    @FXML
    void updateOrderData(ActionEvent event) {
    	loadOrders();
    }
    
	@FXML
	public void loadPurchases()
	{
		int pageNum = this.billPaginator.getCurrentPageIndex();
		loadPurchases(pageNum);
	}

	public void loadPurchases(int pageNum)
	{
		if (purchaseBillTab.isSelected())
		{
			Publisher pub = publishers.getSelectionModel().getSelectedItem();
			Page<Purchase> purchasePages = schoolService.fetchPurchasesForPublisher(pub, pageNum, ROWS_PER_PAGE);
			List<Purchase> purchaseList	= purchasePages.getContent();
			this.billPaginator.setPageCount(purchasePages.getTotalPages());
			this.purchaseData.setItems(FXCollections.observableList(purchaseList));

			this.purchaseData.setOnMouseClicked(ev -> {
				if(verifyDblClick(ev)) editPurchase(ev);
			});
		}


	}
    
	@FXML
	public void loadReturns()
	{
		if (this.returnsTab.isSelected())
		{
			Publisher pub = this.publishers.getSelectionModel().getSelectedItem();
			List<PurchaseReturn> returnDataList = this.schoolService.fetchPurchaseReturns(pub);
			this.returnsData.setItems(FXCollections.observableList(returnDataList));

			this.returnsData.setOnMouseClicked(ev -> {
				if(verifyDblClick(ev)) editReturn(ev);
			});
		}
	}

	@FXML
	void addReturn(ActionEvent event)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createPurchaseRetFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddPurchaseRetController ctrl = createOrderLoader.getController();
			ctrl.initData(this.publishers.getSelectionModel().getSelectedItem(), null);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene, ev -> loadReturns());
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void editReturn(Event event)
	{
		try
		{
			PurchaseReturn purchase = this.returnsData.getSelectionModel().getSelectedItem();

			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createPurchaseRetFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddPurchaseRetController ctrl = createOrderLoader.getController();
			ctrl.initData(this.publishers.getSelectionModel().getSelectedItem(), purchase);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene, ev -> loadReturns());

		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

	}

	@FXML
	void deleteReturn(ActionEvent event)
	{
		PurchaseReturn purchase = this.returnsData.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Purchase Return Confirmation");
		alert.setHeaderText("Are you sure you want to delete the Credit Note: " + purchase.getCreditNoteNum());
		alert.setContentText("Click Ok to Delete");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			schoolService.deletePurchaseReturn(purchase);
			loadReturns();
		}
	}

	@FXML
	public void loadPayments()
	{
		if (this.paymentTab.isSelected())
		{
			Publisher pub = this.publishers.getSelectionModel().getSelectedItem();
			List<PurchasePayment> returnDataList = this.schoolService.fetchPurchasePayments(pub);
			this.paymentData.setItems(FXCollections.observableList(returnDataList));

			this.paymentData.setOnMouseClicked(ev -> {
				if(verifyDblClick(ev)) editPayment(ev);
			});
		}
	}

	@FXML
	void addPayment(ActionEvent event)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createPurchasePayFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddPurchasePayController ctrl = createOrderLoader.getController();
			ctrl.initData(this.publishers.getSelectionModel().getSelectedItem(), null);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene, ev -> loadPayments());
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
			PurchasePayment purchase = this.paymentData.getSelectionModel().getSelectedItem();

			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createPurchasePayFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddPurchasePayController ctrl = createOrderLoader.getController();
			ctrl.initData(this.publishers.getSelectionModel().getSelectedItem(), purchase);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene, ev -> loadPayments());

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
		PurchasePayment purchase = this.paymentData.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Purchase Payment Confirmation");
		alert.setHeaderText("Are you sure you want to delete the Payment: " + purchase.getReceiptNum());
		alert.setContentText("Click Ok to Delete");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			schoolService.deletePurchasePayment(purchase);
			loadPayments();
		}
	}

//	@FXML
//	public void loadStatement()
//	{
//		if (statementTab.isSelected())
//		{
//
//		}
//	}

	@FXML
	public void loadStatementHtml()
	{
		if (statementTabHtml.isSelected())
		{
			try
			{
//				if(print == null)
				{
					loadWebStmt();
				}
			}
			catch (Exception e)
			{
				LOGGER.error("Error...", e);
				e.printStackTrace();
			}
		}
	}

	@FXML
	public void updateStatement(ActionEvent ev)
	{
		loadWebStmt();
	}

	private void loadWebStmt()
	{
		try
		{
			print = prepareJasperPrintForStmt();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			HtmlExporter exporter = new HtmlExporter();
			exporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
			exporter.setExporterInput(new SimpleExporterInput(print));
			exporter.exportReport();

			String content = StringUtils.toEncodedString(outputStream.toByteArray(), Charset.defaultCharset());
			reportData.getEngine().loadContent(content);
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

	}

	private JasperPrint prepareJasperPrintForStmt()
	{
		JasperPrint jasperPrint = null;
		InputStream jasperStream = getClass().getResourceAsStream(statementJrxml);
		HashMap<String, Object> hm = new HashMap<>();
		try
		{
			LocalDate fromDateVal = fromDate.getValue();
			LocalDate toDateVal = toDate.getValue();
			if (toDateVal == null)
			{
				toDateVal = LocalDate.now();
			}

			Sort sort = new Sort(new Sort.Order(Direction.ASC, "txnDate"), new Sort.Order(Direction.ASC, "id"));
			Publisher pub = publishers.getSelectionModel().getSelectedItem();
			List<PurchaseTransaction> tableData = schoolService.fetchPurTransactions(pub, fromDateVal, toDateVal, sort);
			Double openingBalance = 0.0;
			if(tableData != null && !tableData.isEmpty())
			{
				if(tableData.get(0).getPrevTxn() != null)
				{
					openingBalance = tableData.get(0).getPrevTxn().getBalance();
				}
			} 
			Double totalDebit = tableData.stream().collect(Collectors.summingDouble(o->  o.getMultiplier() == 1 ? o.getAmount() : 0.0)); 
			Double totalCredit = tableData.stream().collect(Collectors.summingDouble(o->  o.getMultiplier() == -1 ? o.getAmount() : 0.0));
			hm.put("openingBalance", openingBalance);			
//			hm.put("reportData", tableData);
			hm.put("publisherName", pub.getName());
			hm.put("publisherDetails", pub.getStmtAddress());
			hm.put("fromDate", fromDateVal);
			hm.put("toDate", toDateVal);
			hm.put("accountDetails", "Matha Distributors (Chennai)");
			hm.put("totalDebit", totalDebit);
			hm.put("totalCredit", totalCredit);
			hm.put("datedSchoolPaymentModes", datedSchoolPaymentModes);
			JasperReport compiledFile = JasperCompileManager.compileReport(jasperStream);

			jasperPrint = JasperFillManager.fillReport(compiledFile, hm, new JRBeanCollectionDataSource(tableData));
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
			fileChooser.setInitialFileName(this.publishers.getSelectionModel().getSelectedItem().getName() + "_statement");
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

	@FXML
	public void printOrder(ActionEvent ev)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, printOrderFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			PrintOrderController ctrl = createOrderLoader.getController();
			Order purchase = orderTable.getSelectionModel().getSelectedItem();
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

	public Optional<String> selectPublisher(Map<String, Publisher> pubMap)
	{
		Set<String> pubNames = new TreeSet<>(pubMap.keySet());
		ChoiceDialog<String> dialog = new ChoiceDialog<>(this.defaultPublisher, pubNames);
		
		dialog.setTitle("Publisher Selection");
		dialog.setHeaderText("Publisher Selection");
		dialog.setContentText("Please select a publisher:");
		
		Optional<String> result = dialog.showAndWait();		
		return result;
	}
	
	private void prepareAndShowStage(Event e, Scene childScene)
	{
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.show();
	}

	private void prepareAndShowStage(Event e, Scene childScene, EventHandler<WindowEvent> eventHandler)
	{
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.setOnHiding(eventHandler);
		stage.show();
	}

//	private EventHandler<WindowEvent> purchaseEventHandler = new EventHandler<WindowEvent>() {
//		@Override
//		public void handle(final WindowEvent event)
//		{
//			int pageIdx = billPaginator.getCurrentPageIndex();
//			loadPurchases(pageIdx);
//		}
//	};
//
//	private EventHandler<WindowEvent> purchaseRetEventHandler = new EventHandler<WindowEvent>() {
//		@Override
//		public void handle(final WindowEvent event)
//		{
//			loadReturns();
//		}
//	};
//
//	private EventHandler<WindowEvent> purchasePayEventHandler = new EventHandler<WindowEvent>() {
//		@Override
//		public void handle(final WindowEvent event)
//		{
//			loadPayments();
//		}
//	};
}

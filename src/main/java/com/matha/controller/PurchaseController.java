package com.matha.controller;

import static com.matha.util.UtilConstants.COMMA_SIGN;
import static com.matha.util.UtilConstants.HYPHEN_SPC_SIGN;
import static com.matha.util.UtilConstants.NEW_LINE;
import static com.matha.util.UtilConstants.addPublisherFxml;
import static com.matha.util.UtilConstants.createOrderFxmlFile;
import static com.matha.util.UtilConstants.createPurchaseFxmlFile;
import static com.matha.util.UtilConstants.createPurchasePayFxmlFile;
import static com.matha.util.UtilConstants.createPurchaseRetFxmlFile;
import static com.matha.util.UtilConstants.invoiceJrxml;
import static com.matha.util.UtilConstants.printOrderFxmlFile;
import static com.matha.util.UtilConstants.printPurchaseFxmlFile;
import static com.matha.util.UtilConstants.statementJrxml;
import static com.matha.util.Utils.convertDouble;
import static com.matha.util.Utils.getStringVal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.matha.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
	private WebView reportData;

	private JasperPrint print;
	private Map<String, Publisher> pubMap;

	@FXML
	protected void initialize()
	{
		orderTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		List<Publisher> allPublishers = schoolService.fetchAllPublishers();
		publishers.setConverter(Converters.getPublisherConverter());
		publishers.setItems(FXCollections.observableList(allPublishers));
//		publishers.getSelectionModel().selectFirst();
		
		pubMap = allPublishers.stream().collect(Collectors.toMap(Publisher::getName, pub -> pub ));

		Optional<String> pubSel = selectPublisher();
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
			e.printStackTrace();
		}
	}
	
	@FXML
	void editOrder(ActionEvent event)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createOrderFxmlFile);
			Parent addOrderRoot;

			addOrderRoot = createOrderLoader.load();

			AddOrderController ctrl = createOrderLoader.getController();

			List<Book> schools = schoolService.fetchAllBooks();
			// LOGGER.info(schools.toString());
			HashMap<String, Book> bookMap = new HashMap<>();
			for (Book bookIn : schools)
			{
				bookMap.put(bookIn.getName(), bookIn);
			}

			Order order = orderTable.getSelectionModel().getSelectedItem();

			ctrl.initData(order.getSchool(), bookMap, order);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
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
			ctrl.initData(orderSet, this.publishers.getSelectionModel().getSelectedItem(), null);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene, purchaseEventHandler);
			
			purTabs.getSelectionModel().select(purchaseBillTab);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@FXML
	void editPurchase(ActionEvent event)
	{

		try
		{

			Purchase purchase = this.purchaseData.getSelectionModel().getSelectedItem();

			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createPurchaseFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddPurchaseBillController ctrl = createOrderLoader.getController();
			ctrl.initData(null, this.publishers.getSelectionModel().getSelectedItem(), purchase);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene, purchaseEventHandler);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@FXML
	void deletePurchase(ActionEvent event)
	{
		Purchase purchase = this.purchaseData.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Purchase Bill Confirmation");
		alert.setHeaderText("Are you sure you want to delete the purchase: " + purchase.getId());
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
			JasperPrint jasperPrint = prepareJasperPrint(purchase.getPublisher(), purchase);
			ctrl.initData(jasperPrint);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(ev, addOrderScene);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}

	private JasperPrint prepareJasperPrint(Publisher pub, Purchase purchase)
	{
		JasperPrint jasperPrint = null;
		InputStream jasperStream = getClass().getResourceAsStream(invoiceJrxml);
		HashMap<String, Object> hm = new HashMap<>();
		try
		{			
			Address salesAddr = schoolService.fetchAddress("Purchase");
			StringBuilder strBuild = new StringBuilder();
			strBuild.append(salesAddr.getAddress1());
			strBuild.append(NEW_LINE); 
			strBuild.append(salesAddr.getAddress2());
			strBuild.append(COMMA_SIGN); 
			strBuild.append(salesAddr.getAddress3());
			strBuild.append(HYPHEN_SPC_SIGN);
			strBuild.append(salesAddr.getPin());
			
			Set<PurchaseDet> tableData = purchase.getPurchaseItems();
			Set<String> orderIdSet = tableData.stream()
					.filter(pd -> pd.getOrderItem() != null)
					.map(PurchaseDet::getOrderItem)
					.filter(oi -> oi.getOrder() != null)
					.map(oi -> oi.getOrder().getSerialNo())
					.collect(Collectors.toSet());
			String orderIds = String.join(",", orderIdSet);
			Double subTotal = purchase.getSubTotal();
			Double discAmt = purchase.getCalculatedDisc();

			hm.put("publisherName", pub.getName());
			hm.put("publisherDetails", pub.getInvAddress());
			hm.put("partyName", "MATHA DISTRIBUTORS.");
			hm.put("partyAddress", strBuild.toString());
			hm.put("partyPhone", "Ph - " + salesAddr.getPhone1());
			hm.put("documentsThrough", purchase.getDocsThrough());
			hm.put("despatchedTo", purchase.getDespatchedTo());
			hm.put("invoiceNo", purchase.getId());
			hm.put("txnDate", purchase.getTxnDate());
			hm.put("orderNumbers", orderIds);
			hm.put("despatchedPer", purchase.getDespatchPer());
			hm.put("grNo", purchase.getGrNum());
			hm.put("packageCnt", getStringVal(purchase.getPackages()));
			hm.put("total", purchase.getSubTotal());
			hm.put("discount", discAmt);
			hm.put("grandTotal", purchase.getNetAmount());
			hm.put("grandTotalInWords", convertDouble(purchase.getNetAmount()));
			hm.put("imageFileName",pub.getLogoFileName());
			
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
		orderTable.setItems(FXCollections.observableList(orderList));
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
			purchaseData.setItems(FXCollections.observableList(purchaseList));
		}
	}
    
	@FXML
	public void loadReturns()
	{
		if (returnsTab.isSelected())
		{
			Publisher pub = publishers.getSelectionModel().getSelectedItem();
			List<PurchaseReturn> returnDataList = schoolService.fetchPurchaseReturns(pub);
			returnsData.setItems(FXCollections.observableList(returnDataList));
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
			prepareAndShowStage(event, addOrderScene, purchaseRetEventHandler);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@FXML
	void editReturn(ActionEvent event)
	{
		try
		{
			PurchaseReturn purchase = this.returnsData.getSelectionModel().getSelectedItem();

			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createPurchaseRetFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddPurchaseRetController ctrl = createOrderLoader.getController();
			ctrl.initData(this.publishers.getSelectionModel().getSelectedItem(), purchase);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene, purchaseRetEventHandler);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@FXML
	void deleteReturn(ActionEvent event)
	{
		PurchaseReturn purchase = this.returnsData.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Purchase Return Confirmation");
		alert.setHeaderText("Are you sure you want to delete the Credit Note: " + purchase.getId());
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
		if (paymentTab.isSelected())
		{
			Publisher pub = publishers.getSelectionModel().getSelectedItem();
			List<PurchasePayment> returnDataList = schoolService.fetchPurchasePayments(pub);
			paymentData.setItems(FXCollections.observableList(returnDataList));
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
			prepareAndShowStage(event, addOrderScene, purchasePayEventHandler);
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
			PurchasePayment purchase = this.paymentData.getSelectionModel().getSelectedItem();

			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createPurchasePayFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddPurchasePayController ctrl = createOrderLoader.getController();
			ctrl.initData(this.publishers.getSelectionModel().getSelectedItem(), purchase);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene, purchasePayEventHandler);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@FXML
	void deletePayment(ActionEvent event)
	{
		PurchasePayment purchase = this.paymentData.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Purchase Payment Confirmation");
		alert.setHeaderText("Are you sure you want to delete the Payment: " + purchase.getId());
		alert.setContentText("Click Ok to Delete");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		{
			schoolService.deletePurchasePayment(purchase);
			loadPayments();
		}
	}

	@FXML
	public void loadStatement()
	{
		if (statementTab.isSelected())
		{

		}
	}

	@FXML
	public void loadStatementHtml()
	{
		if (statementTabHtml.isSelected())
		{
			try
			{
				if(print == null)
				{
					loadWebStmt();
				}
			}
			catch (Exception e)
			{
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
			print = prepareJasperPrint();
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
			e.printStackTrace();
		}

	}

	private JasperPrint prepareJasperPrint()
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
	public void exportAndSave(ActionEvent ev)
	{
		try
		{

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save File");

			File file = fileChooser.showSaveDialog(((Node) ev.getSource()).getScene().getWindow());
			JasperExportManager.exportReportToPdfFile(print, file.getAbsolutePath());
		}
		catch (Throwable e)
		{
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
			e.printStackTrace();
		}		
	}

	public Optional<String> selectPublisher()
	{
		ChoiceDialog<String> dialog = new ChoiceDialog<>(null, pubMap.keySet());
		
		dialog.setTitle("Publisher Selection");
		dialog.setHeaderText("Publisher Selection");
		dialog.setContentText("Please select a publisher:");
		
		Optional<String> result = dialog.showAndWait();		
		return result;
	}
	
	private void prepareAndShowStage(ActionEvent e, Scene childScene)
	{
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.show();
	}

	private void prepareAndShowStage(ActionEvent e, Scene childScene, EventHandler<WindowEvent> eventHandler)
	{
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.setOnHiding(eventHandler);
		stage.show();
	}

	private EventHandler<WindowEvent> purchaseEventHandler = new EventHandler<WindowEvent>() {
		@Override
		public void handle(final WindowEvent event)
		{
			int pageIdx = billPaginator.getCurrentPageIndex();
			loadPurchases(pageIdx);
		}
	};

	private EventHandler<WindowEvent> purchaseRetEventHandler = new EventHandler<WindowEvent>() {
		@Override
		public void handle(final WindowEvent event)
		{
			loadReturns();
		}
	};

	private EventHandler<WindowEvent> purchasePayEventHandler = new EventHandler<WindowEvent>() {
		@Override
		public void handle(final WindowEvent event)
		{
			loadPayments();
		}
	};
}

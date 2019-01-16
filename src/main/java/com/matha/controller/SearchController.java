package com.matha.controller;

import com.matha.domain.*;
import com.matha.service.SchoolService;
import com.matha.util.LoadUtils;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collector;

import static com.matha.util.UtilConstants.*;
import static com.matha.util.Utils.prepareJasperPrint;
import static com.matha.util.Utils.prepareSaleBillPrint;
import static com.matha.util.Utils.verifyDblClick;
import static java.util.stream.Collectors.toMap;

@Component
public class SearchController
{
	private static final int ROWS_PER_PAGE = 10;
	private static final Logger LOGGER = LogManager.getLogger(SearchController.class);

	@Autowired
	private SchoolService schoolService;

	@FXML
	private TabPane saleTabs;

	@FXML
	private TextField orderIdStr;

	@FXML
	private Tab ordersTab;

	@FXML
	private TableView<Order> orderTable;

	@FXML
	private Pagination orderPaginator;

	@FXML
	private Label schoolName;

	@FXML
	private TextField salesBillNumStr;

	@FXML
	private Tab billsTab;

	@FXML
	private TableView<Sales> billData;

	@FXML
	private Pagination billPaginator;

	@FXML
	private TextField purBillNumStr;

	@FXML
	private Tab purBillsTab;

	@FXML
	private TableView<Purchase> purBillData;

	@FXML
	private Pagination purBillPaginator;

	private Map<String, Book> bookMap;
	private Collector<Book, ?, Map<String, Book>> bookMapCollector = toMap(o -> o.getBookNum() + ": " + o.getName() + " - " + o.getPublisherName(), o -> o);

	@FXML
	protected void initialize()
	{
		orderPaginator.setPageFactory(this::createOrderPage);
//		billPaginator.setPageFactory(this::createBillPage);
		billPaginator.setPageFactory((Integer pageIndex) -> createBillPage(pageIndex));
		purBillPaginator.setPageFactory(this::createPurBillPage);
	}

	@FXML
	void editOrder(Event event)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createOrderFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddOrderController ctrl = createOrderLoader.getController();
			Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
			List<Book> schools = schoolService.fetchAllBooks();
			this.bookMap = this.schoolService.fetchAllBooks().stream().collect(this.bookMapCollector);

			int idx = this.orderPaginator.getCurrentPageIndex();
			ctrl.initData(selectedOrder.getSchool(), bookMap, selectedOrder);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene, ev -> loadOrderTable(idx));

		}
		catch (IOException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void printOrder(Event event)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, printOrderFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			PrintOrderController ctrl = createOrderLoader.getController();
			Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
			JasperPrint jasperPrint = ctrl.prepareJasperPrint(selectedOrder);
			ctrl.initData(jasperPrint);
			Scene addOrderScene = new Scene(addOrderRoot);

			int idx = this.orderPaginator.getCurrentPageIndex();
			prepareAndShowStage(event, addOrderScene, ev -> loadOrderTable(idx));
		}
		catch (IOException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void searchOrder(Event event)
	{
//		loadOrders();
		orderPaginator.setPageFactory((Integer pageIndex) -> createOrderPage(pageIndex));
	}

	@FXML
	void printPurBill(Event event)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, printPurchaseFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			PrintPurchaseBillController ctrl = createOrderLoader.getController();

			Purchase purchase = purBillData.getSelectionModel().getSelectedItem();
			Address salesAddr = schoolService.fetchAddress("Purchase");
			InputStream iStream = getClass().getResourceAsStream(invoiceJrxml);
			JasperPrint jasperPrint = prepareJasperPrint(purchase.getPublisher(), purchase, salesAddr, iStream);
			ctrl.initData(jasperPrint);
			Scene addOrderScene = new Scene(addOrderRoot);

			int idx = this.purBillPaginator.getCurrentPageIndex();
			prepareAndShowStage(event, addOrderScene, ev -> loadPurchaseBills(idx));

		}
		catch (IOException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void editPurBill(Event event)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createPurchaseFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddPurchaseBillController ctrl = createOrderLoader.getController();

			Purchase selectedOrder = purBillData.getSelectionModel().getSelectedItem();

			List<Book> books = schoolService.fetchBooksForPublisher(selectedOrder.getPublisher());
			this.bookMap = books.stream().collect(this.bookMapCollector);

			ctrl.initData(new HashSet<>(), selectedOrder.getPublisher(), selectedOrder, this.bookMap);

			Scene addOrderScene = new Scene(addOrderRoot);
			int idx = this.purBillPaginator.getCurrentPageIndex();
			prepareAndShowStage(event, addOrderScene, ev -> loadPurchaseBills(idx));

		}
		catch (IOException e)
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
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, addBillFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddBillController ctrl = createOrderLoader.getController();

			List<Book> books = schoolService.fetchAllBooks();
			bookMap = books.stream().collect(bookMapCollector);
			Sales bill = billData.getSelectionModel().getSelectedItem();
			ctrl.initData(null, bill.getSchool(), bill, this.bookMap);

			Scene addOrderScene = new Scene(addOrderRoot);
			int idx = this.billPaginator.getCurrentPageIndex();
			prepareAndShowStage(event, addOrderScene, ev -> loadSalesBillsTab(idx));

		}
		catch (IOException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void printSalesBill(Event event)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, printSaleFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			PrintSalesBillController ctrl = createOrderLoader.getController();

			Sales purchase = billData.getSelectionModel().getSelectedItem();
			InputStream iStream = getClass().getResourceAsStream(salesInvoiceJrxml);
			Address salesAddr = schoolService.fetchAddress("Sales");
			Account acct = schoolService.fetchAccount("Matha Agencies");
			JasperPrint jasperPrint = prepareSaleBillPrint(purchase.getSchool(), purchase, salesAddr, acct, iStream);
			ctrl.initData(jasperPrint);
			Scene addOrderScene = new Scene(addOrderRoot);

			int idx = this.billPaginator.getCurrentPageIndex();
			prepareAndShowStage(event, addOrderScene, ev -> loadSalesBillsTab(idx));
		}
		catch (IOException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void viewBill(Event event) {
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, viewBillFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			ViewBillController ctrl = createOrderLoader.getController();

			Sales bill = billData.getSelectionModel().getSelectedItem();
			ctrl.initData(bill.getSchool(), bill);

			Scene addOrderScene = new Scene(addOrderRoot);

			int idx = this.billPaginator.getCurrentPageIndex();
			prepareAndShowStage(event, addOrderScene, ev -> loadSalesBillsTab(idx));

		}
		catch (IOException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	void searchBill(Event event)
	{
//		loadSalesBillsTab();
		billPaginator.setPageFactory((Integer pageIndex) -> createBillPage(pageIndex));
	}

	@FXML
	void searchPurchaseBill(Event event)
	{
//		loadPurchaseBills();
		purBillPaginator.setPageFactory((Integer pageIndex) -> createPurBillPage(pageIndex));
	}

	public void initData(String orderSearchStrIn)
	{

		switch (orderSearchStrIn)
		{
			case ORDER_SEARCH_STR:
				saleTabs.getSelectionModel().select(ordersTab);
				break;

			case PUR_BILL_SEARCH_STR:
				saleTabs.getSelectionModel().select(purBillsTab);
				break;

			case SALE_BILL_SEARCH_STR:
				saleTabs.getSelectionModel().select(billsTab);
				break;
		}
	}

	private void prepareAndShowStage(Event e, Scene childScene, EventHandler<WindowEvent> eventHandler)
	{
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.setOnHiding(eventHandler);
		stage.showAndWait();
	}

	private Node createOrderPage(int pageIndex)
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
		loadSalesBillsTab(pageIndex);

		if(billData.getItems() != null && !billData.getItems().isEmpty())
		{
			billData.prefHeightProperty().set(ROWS_PER_PAGE * billData.getFixedCellSize() + 30);
		}
		return new BorderPane(billData);
	}

	private Node createPurBillPage(int pageIndex)
	{
		loadPurchaseBills(pageIndex);

		if(purBillData.getItems() != null && !purBillData.getItems().isEmpty())
		{
			purBillData.prefHeightProperty().set(ROWS_PER_PAGE * purBillData.getFixedCellSize() + 30);
		}
		return new BorderPane(purBillData);
	}

	@FXML
	public void loadOrderTable(int idx)
	{
		if(this.ordersTab.isSelected())
		{
			String searchVal = orderIdStr.getText();
			Page<Order> orderPages = schoolService.fetchOrdersForSearchStr(searchVal, idx, ROWS_PER_PAGE);
			List<Order> orderList = orderPages.getContent();
			this.orderPaginator.setPageCount(orderPages.getTotalPages());
			this.orderTable.setItems(FXCollections.observableList(orderList));
			this.orderTable.refresh();

			this.orderTable.setOnMouseClicked(ev -> {
				if(verifyDblClick(ev)) editOrder(ev);
			});

//			orderData.setItems(FXCollections.observableList(orderList));
		}
//		Page<Order> orderPages = schoolService.fetchOrders(pub, idx, ROWS_PER_PAGE, billedToggle.isSelected());
	}

	@FXML
	void loadSalesBillsTab(int idx)
	{
		if(this.billsTab.isSelected())
		{
			String searchVal = salesBillNumStr.getText();
			Page<Sales> purBillList = schoolService.fetchBillsForSearchStr(searchVal, idx, ROWS_PER_PAGE);
//			billData.setItems(FXCollections.observableList(purBillList));

			List<Sales> orderList = purBillList.getContent();
			this.billPaginator.setPageCount(purBillList.getTotalPages());
			this.billData.setItems(FXCollections.observableList(orderList));
			this.billData.refresh();

			this.billData.setOnMouseClicked(ev -> {
				if(verifyDblClick(ev)) editBill(ev);
			});
		}
	}

	@FXML
	void loadPurchaseBills(int idx)
	{
		if (this.purBillsTab.isSelected()) {
			String searchVal = purBillNumStr.getText();
//			List<Purchase> purBillList = schoolService.fetchPurchasesForSearchStr(searchVal).getContent();
//			purBillData.setItems(FXCollections.observableList(purBillList));

			Page<Purchase> purBillList = schoolService.fetchPurchasesForSearchStr(searchVal, idx, ROWS_PER_PAGE);
//			billData.setItems(FXCollections.observableList(purBillList));

			List<Purchase> orderList = purBillList.getContent();
			this.purBillPaginator.setPageCount(purBillList.getTotalPages());
			this.purBillData.setItems(FXCollections.observableList(orderList));
			this.purBillData.refresh();

			this.billData.setOnMouseClicked(ev -> {
				if (verifyDblClick(ev)) editPurBill(ev);
			});
		}
	}

	@FXML
	public void loadOrderTable()
	{
		int pageNum = 0;
		loadOrderTable(pageNum);
	}

	@FXML
	void loadPurchaseBills() {
		int pageNum = 0;
//		loadPurchaseBills(pageNum);
		if (this.purBillsTab.isSelected()) {
			purBillPaginator.setPageFactory(this::createPurBillPage);
		}
	}

	@FXML
	void loadSalesBillsTab()
	{
		int pageNum = 0;
//		loadSalesBillsTab(pageNum);
		if (this.billsTab.isSelected()) {
			billPaginator.setPageFactory((Integer pageIndex) -> createBillPage(pageIndex));
		}
	}
}
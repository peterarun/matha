package com.matha.controller;

import com.matha.domain.*;
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
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.View;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

import static com.matha.util.UtilConstants.*;
import static com.matha.util.Utils.prepareJasperPrint;
import static com.matha.util.Utils.preparePrintScene;
import static com.matha.util.Utils.prepareSaleBillPrint;

@Component
public class SearchController
{
	@Autowired
	private SchoolService schoolService;

	@FXML
	private TabPane saleTabs;

	@FXML
	private TextField orderIdStr;

	@FXML
	private Tab ordersTab;

	@FXML
	private TextField purBillNumStr;

	@FXML
	private Tab purBillsTab;

	@FXML
	private TableView<Order> orderData;

	@FXML
	private Label schoolName;

	@FXML
	private Tab billsTab;

	@FXML
	private TableView<Sales> billData;

	@FXML
	private TableView<Purchase> purBillData;

	@FXML
	private TextField salesBillNumStr;

//	private String orderSearchStr;

	@FXML
	void loadOrders()
	{
		if(this.ordersTab.isSelected())
		{
			String searchVal = orderIdStr.getText();
			List<Order> orderList = schoolService.fetchOrdersForSearchStr(searchVal);
			orderData.setItems(FXCollections.observableList(orderList));
		}
	}

	@FXML
	void editOrder(ActionEvent event)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createOrderFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddOrderController ctrl = createOrderLoader.getController();
			Order selectedOrder = orderData.getSelectionModel().getSelectedItem();
			Map<String, Book> bookMap = new HashMap<>();
			List<Book> schools = schoolService.fetchAllBooks();
			for (Book bookIn : schools)
			{
				bookMap.put(bookIn.getName() + " - " + bookIn.getPublisherName(), bookIn);
			}
			ctrl.initData(selectedOrder.getSchool(), bookMap, selectedOrder);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene);

		}catch (IOException e)
		{
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
			Order selectedOrder = orderData.getSelectionModel().getSelectedItem();
			JasperPrint jasperPrint = ctrl.prepareJasperPrint(selectedOrder);
			ctrl.initData(jasperPrint);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene);

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@FXML
	void searchOrder(Event event)
	{
		loadOrders();
	}

	@FXML
	void loadPurchaseBills()
	{
		if(this.purBillsTab.isSelected())
		{
			String searchVal = purBillNumStr.getText();
			List<Purchase> purBillList = schoolService.fetchPurchasesForSearchStr(searchVal).getContent();
			purBillData.setItems(FXCollections.observableList(purBillList));
		}
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
			prepareAndShowStage(event, addOrderScene);

		}
		catch (IOException e)
		{
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
			ctrl.initData(new HashSet<>(), selectedOrder.getPublisher(), selectedOrder);

			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene);

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@FXML
	void searchPurchaseBill(Event event)
	{
		loadPurchaseBills();
	}

	@FXML
	void loadSalesBillsTab()
	{
		if(this.billsTab.isSelected())
		{
			String searchVal = salesBillNumStr.getText();
			List<Sales> purBillList = schoolService.fetchBillsForSearchStr(searchVal).getContent();
			billData.setItems(FXCollections.observableList(purBillList));
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

			HashMap<String, Book> bookMap = new HashMap<>();
			List<Book> schools = schoolService.fetchAllBooks();
			for (Book bookIn : schools)
			{
				bookMap.put(bookIn.getName() + " - " + bookIn.getPublisherName(), bookIn);
			}

			Sales bill = billData.getSelectionModel().getSelectedItem();
			ctrl.initData(null, bill.getSchool(), bill, bookMap);

			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene);

		}
		catch (IOException e)
		{
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
			prepareAndShowStage(event, addOrderScene);
		}
		catch (IOException e)
		{
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
			prepareAndShowStage(event, addOrderScene);

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@FXML
	void searchBill(Event event)
	{
		loadSalesBillsTab();
	}

//	@FXML
//	protected void initialize()
//	{
//		System.out.println("Initialized");
//	}

	public void initData(String orderSearchStrIn)
	{
//		this.orderSearchStr = orderSearchStrIn;

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

	private void prepareAndShowStage(Event e, Scene childScene)
	{
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.showAndWait();
	}
}
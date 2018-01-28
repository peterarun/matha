package com.matha.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.Book;
import com.matha.domain.Order;
import com.matha.domain.Publisher;
import com.matha.domain.Purchase;
import com.matha.service.SchoolService;
import com.matha.util.Converters;
import com.matha.util.LoadUtils;
import com.matha.util.UtilConstants;
import com.matha.util.Utils;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

@Component
public class PurchaseController
{

	private static final int ROWS_PER_PAGE = 10;

	@Autowired
	private SchoolService schoolService;

	@FXML
	private ChoiceBox<Publisher> publishers;

	@FXML
	private Tab ordersTab;

	@FXML
	private TableView<Order> orderTable;

	// @FXML
	// private TableView<OrderItem> orderData;

	@FXML
	private Pagination orderPaginator;

	// @FXML
	// private TreeTableView<Object> orders;
	//
	// @FXML
	// private TreeTableColumn<Order, String> orderNumCol;

	@FXML
	private Tab purchaseBillTab;

	@FXML
	private TableView<Purchase> purchaseData;

	@FXML
	private Tab returnsTab;

	@FXML
	private Tab paymentTab;

	@FXML
	private Tab statementTab;

	@FXML
	protected void initialize()
	{
		orderTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		orderPaginator.setPageFactory(this::createPage);

		List<Publisher> allPublishers = schoolService.fetchAllPublishers();
		publishers.setConverter(Converters.getPublisherConverter());
		publishers.setItems(FXCollections.observableList(allPublishers));
		publishers.getSelectionModel().selectFirst();
	}

	private Node createPage(int pageIndex)
	{
		loadOrderTable(pageIndex);

		orderTable.prefHeightProperty()
				.bind(Bindings.size(orderTable.getItems()).multiply(orderTable.getFixedCellSize()).add(30));

		return new BorderPane(orderTable);
	}

	@FXML
	void changedState(ActionEvent event)
	{
		Publisher pub = publishers.getSelectionModel().getSelectedItem();
		int idx = orderPaginator.getCurrentPageIndex();
		List<Order> orderList = schoolService.fetchOrders(pub, idx, ROWS_PER_PAGE).getContent();
		orderTable.setItems(FXCollections.observableList(orderList));
	}

	@FXML
	void editOrder(ActionEvent event)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, UtilConstants.createOrderFxmlFile);
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
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void createPurchase(ActionEvent event)
	{

		try
		{
			ObservableList<Order> selectedOrders = orderTable.getSelectionModel().getSelectedItems();
			Set<Order> orderSet = new HashSet<>(selectedOrders);

			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, UtilConstants.createPurchaseFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddPurchaseBillController ctrl = createOrderLoader.getController();
			ctrl.initData(orderSet, this.publishers.getSelectionModel().getSelectedItem(), null);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene);

		} catch (Exception e)
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

			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, UtilConstants.createPurchaseFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			AddPurchaseBillController ctrl = createOrderLoader.getController();
			ctrl.initData(null, this.publishers.getSelectionModel().getSelectedItem(), purchase);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	@FXML
	void deletePurchase(ActionEvent event)
	{
		
	}

	private void loadOrderTable(int idx)
	{
		Publisher pub = publishers.getSelectionModel().getSelectedItem();
		List<Order> orderList = schoolService.fetchOrders(pub, idx, ROWS_PER_PAGE).getContent();
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
	}

	@FXML
	public void loadPurchases()
	{
		if (purchaseBillTab.isSelected())
		{
			Publisher pub = publishers.getSelectionModel().getSelectedItem();
			List<Purchase> purchaseList = schoolService.fetchPurchasesForPublisher(pub);
			purchaseData.setItems(FXCollections.observableList(purchaseList));
		}
	}

	@FXML
	public void loadReturns()
	{
		if (returnsTab.isSelected())
		{

		}
	}

	@FXML
	public void loadPayments()
	{
		if (paymentTab.isSelected())
		{

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
	public void printOrder(ActionEvent ev)
	{
		try
		{

			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, UtilConstants.printOrderFxmlFile);
			Parent addOrderRoot;

			addOrderRoot = createOrderLoader.load();
			PrintOrderController ctrl = createOrderLoader.getController();

			Order orderItem = orderTable.getSelectionModel().getSelectedItem();
			Publisher pub = publishers.getSelectionModel().getSelectedItem();
			ctrl.initData(orderItem, pub);

			Scene parentScene = ((Node) ev.getSource()).getScene();
			Window parentWindow = parentScene.getWindow();

			Utils.print(addOrderRoot, parentWindow, new Label());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void prepareAndShowStage(ActionEvent e, Scene childScene)
	{
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.show();
	}
}

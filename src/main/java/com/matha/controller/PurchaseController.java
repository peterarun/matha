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
import com.matha.domain.OrderItem;
import com.matha.domain.Publisher;
import com.matha.domain.Purchase;
import com.matha.service.SchoolService;
import com.matha.util.Converters;
import com.matha.util.LoadUtils;
import com.matha.util.UtilConstants;
import com.matha.util.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

@Component
public class PurchaseController {

	@Autowired
	private SchoolService schoolService;

	@FXML
	private ChoiceBox<Publisher> publishers;

	@FXML
	private TableView<OrderItem> orderData;

	@FXML
	private TableView<Purchase> purchaseData;

	@FXML
	private Pagination orderPaginator;

	@FXML
	private TreeTableView<Order> orders;

	@FXML
	protected void initialize() {
		orderData.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		List<Publisher> allPublishers = schoolService.fetchAllPublishers();
		publishers.setItems(FXCollections.observableList(allPublishers));
		publishers.getSelectionModel().selectFirst();
		publishers.setConverter(Converters.getPublisherConverter());

		// Publisher pub = publishers.getSelectionModel().getSelectedItem();
		// List<OrderItem> orderItems = schoolService.fetchOrderItemsForPublisher(pub);
		// orderData.setItems(FXCollections.observableList(orderItems));
		//
		// List<Purchase> purchases = schoolService.fetchPurchasesForPublisher(pub);
		// purchaseData.setItems(FXCollections.observableList(purchases));
	}

	private Node createPage(int pageIndex) {

		int rowsPerPage = 10;
		// int fromIndex = pageIndex * rowsPerPage;
		// int toIndex = Math.min(fromIndex + rowsPerPage, data.size());
		// orders.setItems(FXCollections.observableArrayList(data.subList(fromIndex,
		// toIndex)));

		Publisher pub = publishers.getSelectionModel().getSelectedItem();
		List<Order> orderList = schoolService.fetchOrders(pub, pageIndex, rowsPerPage).getContent();

		orders.setShowRoot(false);
		TreeItem<Order> root = new TreeItem<Order>();
		root.setExpanded(true);
		for (Order o : orderList) {
			root.getChildren().add(new TreeItem<Order>(o));
		}

		orders.setRoot(root);
		return new BorderPane(orders);
	}

	@FXML
	void changedState(ActionEvent event) {
		Publisher pub = publishers.getSelectionModel().getSelectedItem();
		List<OrderItem> orderItems = schoolService.fetchOrderItemsForPublisher(pub);
		orderData.setItems(FXCollections.observableList(orderItems));

		List<Purchase> purchases = schoolService.fetchPurchasesForPublisher(pub);
		purchaseData.setItems(FXCollections.observableList(purchases));
	}

	@FXML
	void editOrder(ActionEvent event) {
		try {
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, UtilConstants.createOrderFxmlFile);
			Parent addOrderRoot;

			addOrderRoot = createOrderLoader.load();

			AddOrderController ctrl = createOrderLoader.getController();

			List<Book> schools = schoolService.fetchAllBooks();
			// LOGGER.info(schools.toString());
			HashMap<String, Book> bookMap = new HashMap<>();
			for (Book bookIn : schools) {
				bookMap.put(bookIn.getName(), bookIn);
			}

			OrderItem orderItem = orderData.getSelectionModel().getSelectedItem();

			ctrl.initData(orderItem.getOrder().getSchool(), bookMap);
			ctrl.updateFormData(orderItem.getOrder());
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void createPurchase(ActionEvent event) {

		try {
			ObservableList<OrderItem> selectedOrders = orderData.getSelectionModel().getSelectedItems();
			Set<OrderItem> orderSet = new HashSet<>(selectedOrders);

			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, UtilConstants.createPurchaseFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			CreatePurchaseController ctrl = createOrderLoader.getController();
			ctrl.init(orderSet, this.publishers.getSelectionModel().getSelectedItem());
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void editPurchase(ActionEvent event) {

		try {

			Purchase purchase = this.purchaseData.getSelectionModel().getSelectedItem();

			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, UtilConstants.createPurchaseFxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			CreatePurchaseController ctrl = createOrderLoader.getController();
			ctrl.initEdit(purchase);
			Scene addOrderScene = new Scene(addOrderRoot);
			prepareAndShowStage(event, addOrderScene);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void loadPurchases() {
		Publisher pub = publishers.getSelectionModel().getSelectedItem();
		List<Purchase> purchases = schoolService.fetchPurchasesForPublisher(pub);
		purchaseData.setItems(FXCollections.observableList(purchases));
	}

	@FXML
	public void printOrder(ActionEvent ev) {
		try {

			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, UtilConstants.printOrderFxmlFile);
			Parent addOrderRoot;

			addOrderRoot = createOrderLoader.load();
			PrintOrderController ctrl = createOrderLoader.getController();
			HashMap<String, Book> bookMap = new HashMap<>();

			OrderItem orderItem = orderData.getSelectionModel().getSelectedItem();
			Publisher pub = publishers.getSelectionModel().getSelectedItem();
			ctrl.initData(orderItem, pub);

			Scene parentScene = ((Node) ev.getSource()).getScene();
			Window parentWindow = parentScene.getWindow();

			Utils.print(addOrderRoot, parentWindow, new Label());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void prepareAndShowStage(ActionEvent e, Scene childScene) {
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.show();
	}
}

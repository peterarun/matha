package com.matha.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.sun.glass.events.MouseEvent;

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
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

@Component
public class PurchaseController {

	private static final int ROWS_PER_PAGE = 10;
	
	@Autowired
	private SchoolService schoolService;

	@FXML
	private ChoiceBox<Publisher> publishers;

	@FXML
	private TableView<Order> orderTable;
	
	@FXML
	private TableView<OrderItem> orderData;

	@FXML
	private TableView<Purchase> purchaseData;

	@FXML
	private Pagination orderPaginator;

	@FXML
	private TreeTableView<Object> orders;

	@FXML
	private TreeTableColumn<Order, String> orderNumCol;

	@FXML
	protected void initialize() {
		// orders.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		orderPaginator.setPageFactory(this::createPage);
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

		
		// int fromIndex = pageIndex * rowsPerPage;
		// int toIndex = Math.min(fromIndex + rowsPerPage, data.size());
		// orders.setItems(FXCollections.observableArrayList(data.subList(fromIndex,
		// toIndex)));

		Publisher pub = publishers.getSelectionModel().getSelectedItem();
		List<Order> orderList = schoolService.fetchOrders(pub, pageIndex, ROWS_PER_PAGE).getContent();
		orderTable.setItems(FXCollections.observableList(orderList));
		
		orderTable.prefHeightProperty().bind(Bindings.size(orderTable.getItems()).multiply(orderTable.getFixedCellSize()).add(30));

//		TreeItem<Object> it = new TreeItem<>();
//		orders = new TreeTableView<Object>();
//		orders.setRoot(it);
//		orders.setShowRoot(false);
//		orders.getColumns().addAll(prepareFirstLevelColumns());
//				
//		for (Order order : orderList) {
//		
//			TreeItem<Object> orderIn = new TreeItem<>(order);
//			it.getChildren().add(orderIn);		
//			
//			TreeTableView<Object> tView = new TreeTableView<>();
//			tView.getColumns().addAll(prepareSecondLevelColumns());
//			tView.setRoot(orderIn);
//			for (OrderItem orderItem : order.getOrderItem()) {
//				TreeItem<Object> itOr = new TreeItem<>(orderItem);
//				orderIn.getChildren().add(itOr);
//			}			
//		
//		}

		// List<OrderItem> orderItems = schoolService.fetchOrderItemsForPublisher(pub);
		// orderData.setItems(FXCollections.observableList(orderItems));

		// orders.setShowRoot(false);
		// TreeItem<Order> root = new TreeItem<Order>();
		// root.setExpanded(true);
		// for (Order o : orderList) {
		// root.getChildren().add(new TreeItem<Order>(o));
		// }
		//
		// orders.setRoot(root);
		return new BorderPane(orderTable);
	}

	public List<TreeTableColumn<Object, ?>> prepareFirstLevelColumns()
	{
		List<TreeTableColumn<Object, ?>> cols = new ArrayList<>();
		
        TreeTableColumn<Object, String> idColumn = new TreeTableColumn<>("Order Num");
        idColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("serialNo"));
        cols.add(idColumn);

        TreeTableColumn<Object, String> schoolColumn = new TreeTableColumn<>("School");
        schoolColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("schoolName"));
        cols.add(schoolColumn);

        return cols;
		
	}
	
	public List<TreeTableColumn<Object, ?>> prepareSecondLevelColumns()
	{
		List<TreeTableColumn<Object, ?>> cols = new ArrayList<>();
		
        TreeTableColumn<Object, String> idColumn = new TreeTableColumn<>("Book Name");
        idColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("bookName"));
        cols.add(idColumn);

        TreeTableColumn<Object, String> schoolColumn = new TreeTableColumn<>("Quantity");
        schoolColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("count"));
        cols.add(schoolColumn);

        return cols;
		
	}
	
	@FXML
	void changedState(ActionEvent event) {
		// Publisher pub = publishers.getSelectionModel().getSelectedItem();
		// List<OrderItem> orderItems = schoolService.fetchOrderItemsForPublisher(pub);
		// orderData.setItems(FXCollections.observableList(orderItems));
		//
		// List<Purchase> purchases = schoolService.fetchPurchasesForPublisher(pub);
		// purchaseData.setItems(FXCollections.observableList(purchases));
		
		Publisher pub = publishers.getSelectionModel().getSelectedItem();	
	
//		orderPaginator.setCurrentPageIndex(1);
		int idx = orderPaginator.getCurrentPageIndex();
		List<Order> orderList = schoolService.fetchOrders(pub, idx, ROWS_PER_PAGE).getContent();
		orderTable.setItems(FXCollections.observableList(orderList));
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

			Order order = orderTable.getSelectionModel().getSelectedItem();

			ctrl.initData(order.getSchool(), bookMap, order);
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
//		List<Purchase> purchases = schoolService.fetchPurchasesForPublisher(pub);
//		purchaseData.setItems(FXCollections.observableList(purchases));
		
	
		int idx = orderPaginator.getCurrentPageIndex();
		List<Order> orderList = schoolService.fetchOrders(pub, idx, ROWS_PER_PAGE).getContent();
		orderTable.setItems(FXCollections.observableList(orderList));

	}

	@FXML
	public void printOrder(ActionEvent ev) {
		try {

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void prepareAndShowStage(ActionEvent e, Scene childScene) {
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.show();
	}
}

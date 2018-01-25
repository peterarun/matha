package com.matha.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.Book;
import com.matha.domain.Order;
import com.matha.domain.School;
import com.matha.service.SchoolService;
import com.matha.util.LoadUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import static com.matha.util.UtilConstants.*;

@Component
public class SchoolDetailsController
{

	private Parent addOrderRoot;
	private Scene addOrderScene;
	private School school;

	@Autowired
	SchoolService schoolService;

	@FXML
	private TableView<Order> txnData;

	@FXML
	private TextArea address;

	@FXML
	private TextField phone;

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

		// if (addOrderRoot == null) {
		// addOrderRoot = createOrderLoader.load();
		// }
		// AddOrderController ctrl = createOrderLoader.getController();
		// ctrl.initData();
		// addOrderScene = new Scene(addOrderRoot);
		// SchoolService srvc = Main.getContext().getBean(SchoolService.class);
		System.out.println(schoolService);

		List<Book> schools = schoolService.fetchAllBooks();
		// LOGGER.info(schools.toString());
		bookMap = new HashMap<>();
		for (Book bookIn : schools)
		{
			bookMap.put(bookIn.getName(), bookIn);
		}

		txnData.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	public void initData(School school)
	{
		schoolName.setText(school.getName());
		address.setText(school.addressText());
		txnData.setItems(FXCollections.observableList(schoolService.fetchOrderForSchool(school)));
		this.school = school;
	}

	@FXML
	void addOrder(ActionEvent e) throws IOException
	{

		FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createOrderFxmlFile);
		addOrderRoot = createOrderLoader.load();
		AddOrderController ctrl = createOrderLoader.getController();
		ctrl.initData(this.school, this.bookMap, null);
		addOrderScene = new Scene(addOrderRoot);
		prepareAndShowStage(e, addOrderScene);
	}

	@FXML
	void editOrder(ActionEvent e) throws IOException
	{
		FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, createOrderFxmlFile);
		addOrderRoot = createOrderLoader.load();
		AddOrderController ctrl = createOrderLoader.getController();
		Order selectedOrder = txnData.getSelectionModel().getSelectedItem();
		ctrl.initData(this.school, this.bookMap, selectedOrder);		
//		ctrl.updateFormData(selectedOrder);
		addOrderScene = new Scene(addOrderRoot);
		prepareAndShowStage(e, addOrderScene);
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
	void createBill(ActionEvent event)
	{

		try
		{
			FXMLLoader createBillLoader = LoadUtils.loadFxml(this, addBillFxmlFile);
			Parent addBillRoot = createBillLoader.load();
			AddBillController ctrl = createBillLoader.getController();
			ObservableList<Order> selectedOrder = txnData.getSelectionModel().getSelectedItems();
			ctrl.initData(selectedOrder, this.school);
			Scene addBillScene = new Scene(addBillRoot);
			prepareAndShowStage(event, addBillScene);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@FXML
	void addBill(ActionEvent event)
	{

	}

	@FXML
	void editBill(ActionEvent event)
	{

	}

	@FXML
	void deleteBill(ActionEvent event)
	{

	}

	@FXML
	void addCredit(ActionEvent event)
	{

	}

	@FXML
	void editCredit(ActionEvent event)
	{

	}

	@FXML
	void deleteCredit(ActionEvent event)
	{

	}

	@FXML
	void addPayment(ActionEvent event)
	{

	}

	@FXML
	void editPayment(ActionEvent event)
	{

	}

	@FXML
	void deletePayment(ActionEvent event)
	{

	}

	@FXML
	void generateStmt(ActionEvent event)
	{

	}

	private void prepareAndShowStage(ActionEvent e, Scene childScene)
	{
		Stage stage = LoadUtils.loadChildStage(e, childScene);
		stage.setOnHiding(new EventHandler<WindowEvent>() {
			@Override
			public void handle(final WindowEvent event)
			{
				System.out.println("Calling close");
				initData(school);
			}
		});
		stage.show();
	}
}

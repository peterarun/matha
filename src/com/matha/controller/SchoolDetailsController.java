package com.matha.controller;

import java.io.IOException;

import com.matha.domain.School;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class SchoolDetailsController {

	private final String createOrderFxmlFile = "../view/createOrder.fxml";
	private final FXMLLoader createOrderLoader = new FXMLLoader(getClass().getResource(createOrderFxmlFile));

	Parent root;

	@FXML
	private TableView<?> txnData;

	@FXML
	private TextArea address;

	@FXML
	private TextField phone;

	@FXML
	private Label schoolName;

	@FXML
	private TextField email;

	public void initData(School school) {
		schoolName.setText(school.getName());
		address.setText(school.addressText());
	}

	@FXML
	void addOrder(ActionEvent e) throws IOException {
		root = createOrderLoader.load();
//		AddOrderController ctrl = createOrderLoader.getController();
//		ctrl.initData();
		Scene scene = new Scene(root);
//		Scene mainScene = Main.getScene();
		Window parentWindow = ((Node)e.getSource()).getScene().getWindow();
		Stage stage = new Stage();
		stage.initOwner(parentWindow);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setScene(scene);
		stage.show();
		
//		((Pane) mainScene.getRoot()).getChildren().add(scene.getRoot());
	}

	@FXML
	void editOrder(ActionEvent event) {

	}

	@FXML
	void deleteOrder(ActionEvent event) {

	}

	@FXML
	void addBill(ActionEvent event) {

	}

	@FXML
	void editBill(ActionEvent event) {

	}

	@FXML
	void deleteBill(ActionEvent event) {

	}

	@FXML
	void addCredit(ActionEvent event) {

	}

	@FXML
	void editCredit(ActionEvent event) {

	}

	@FXML
	void deleteCredit(ActionEvent event) {

	}

	@FXML
	void addPayment(ActionEvent event) {

	}

	@FXML
	void editPayment(ActionEvent event) {

	}

	@FXML
	void deletePayment(ActionEvent event) {

	}
}

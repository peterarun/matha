package com.matha.controller;

import static com.matha.util.UtilConstants.schoolsFxml;

import java.io.IOException;

import com.matha.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class AppMainController {

	private static Parent root;	
	
	@FXML
	private TitledPane purchases;

	@FXML
	void loadPurchases(ActionEvent event) {

	}

	@FXML
	void loadSales(MouseEvent event) 
	{
		try 
		{
			root = FXMLLoader.load(getClass().getResource(schoolsFxml));
			Parent one = ((Node)event.getSource()).getParent().getParent();
			SplitPane paneAll = (SplitPane)one.getParent().getParent();
			AnchorPane aPane1 = (AnchorPane) paneAll.getItems().get(1);
			VBox vBox = (VBox) aPane1.getChildren().get(0);
			TitledPane tPane = (TitledPane) vBox.getChildren().get(1);
			SubScene scene = new SubScene(root, 800, 600);
			tPane.setText("Title Here");
			AnchorPane actPane = (AnchorPane) tPane.getContent();
			Main.setMainPane(actPane);
//			Stage dow = (Stage) actPane.getScene().getWindow();
//			dow.setScene(scene);
//			dow.sizeToScene();\
			actPane.getChildren().add(scene);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void loadAccounts(ActionEvent event) {

	}

	@FXML
	void loadCashBook(ActionEvent event) {

	}

	@FXML
	void loadBooks(ActionEvent event) {

	}

}

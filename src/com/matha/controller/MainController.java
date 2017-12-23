package com.matha.controller;

import java.io.IOException;

import com.matha.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MainController {

	String fxmlFile = "../view/Schools.fxml";
	FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
	Parent root;

	@FXML
	void loadSales(ActionEvent event) {
		try {
			root = loader.load();
			Main.loadScene(new Scene(root));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

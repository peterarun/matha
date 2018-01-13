package com.matha.util;

import com.matha.sales.SalesApplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoadUtils {
	public static Stage loadPopupScene(Window parentWindow, Scene scene) {
		Stage stage = new Stage();
		stage.initOwner(parentWindow);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setScene(scene);
		return stage;

	}

	public static FXMLLoader loadFxml(Object obj, String urlStr) {
		FXMLLoader fxmlLoader = new FXMLLoader(obj.getClass().getResource(urlStr));
		fxmlLoader.setControllerFactory(SalesApplication.ctx::getBean);
		return fxmlLoader;
	}

	public static Stage loadChildStage(ActionEvent e, Scene childScene) {
		Scene parentScene = ((Node) e.getSource()).getScene();
		Window parentWindow = parentScene.getWindow();
		Stage stage = loadPopupScene(parentWindow, childScene);

		return stage;
	}
}

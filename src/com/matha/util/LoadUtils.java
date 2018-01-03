package com.matha.util;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoadUtils {
	public static void loadPopupScene(Window parentWindow, Scene scene) 
	{
		Stage stage = new Stage();
		stage.initOwner(parentWindow);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setScene(scene);
		stage.show();
	}

}

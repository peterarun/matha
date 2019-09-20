package com.matha.sales;

import java.io.File;
import java.io.FileInputStream;

import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Value;

public class MyPreloader extends Preloader
{
	private Stage preloaderStage;

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		String activeProfile = System.getProperty("spring.profiles.active");
		this.preloaderStage = primaryStage;

		VBox loading = new VBox(20);
		loading.setMaxWidth(Region.USE_PREF_SIZE);
		loading.setMaxHeight(Region.USE_PREF_SIZE);
		Image img = new Image(new FileInputStream(new File("javaLoading-" + activeProfile + ".jpg")));
		loading.getChildren().add(new ImageView(img));
		loading.getChildren().add(new Label("Please wait..."));

		BorderPane root = new BorderPane(loading);
		Scene scene = new Scene(root);

		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setWidth(img.getWidth());
		primaryStage.setHeight(img.getHeight());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void handleStateChangeNotification(StateChangeNotification stateChangeNotification)
	{
		if (stateChangeNotification.getType() == Type.BEFORE_START)
		{
			preloaderStage.hide();
		}
	}
}
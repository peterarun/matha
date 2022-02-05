package com.matha.sales;

import com.matha.domain.Address;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.matha.util.LoadUtils;
import com.matha.util.UtilConstants;
import com.sun.javafx.application.LauncherImpl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.matha.util.Utils.showConfirmation;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.matha.repository")
@EntityScan("com.matha.domain")
@ComponentScan(basePackages = { "com.matha.controller", "com.matha.service", "com.matha.generator"})
@EnableAutoConfiguration
public class SalesApplication extends Application
{

	private static final Logger LOGGER = LogManager.getLogger(SalesApplication.class);

	private Parent root;

	public static ConfigurableApplicationContext ctx;

	public static void main(String[] args)
	{
//		launch(args);
		LauncherImpl.launchApplication(SalesApplication.class, MyPreloader.class, args);
	}

	@Override
	public void init() throws Exception
	{
		ctx = SpringApplication.run(SalesApplication.class);

		FXMLLoader fxmlLoader = LoadUtils.loadFxml(this, UtilConstants.landingPageFxmlFile);

		root = fxmlLoader.load();

		
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		try
		{
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();

			primaryStage.setOnCloseRequest(confirmCloseEventHandler);

		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
			throw e;
		}
	}

	// Not used - setting location causes some other error. FXML location has to be
	// passed as constructor arg itself.
	// @Bean
	// @Scope("prototype")
	public FXMLLoader fxmlLoader()
	{
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setControllerFactory(SalesApplication.ctx::getBean);
		return fxmlLoader;
	}

	private EventHandler<WindowEvent> confirmCloseEventHandler = event ->
	{
		if (!showConfirmation("Closing Application!",
				"Are you sure you want to exit?",
				"Click Ok to Exit"))
		{
			event.consume();
			return;
		}
	};


	@Bean
	@ConfigurationProperties(prefix = "purchase")
	public Address purchaseProps() {
		return new Address();
	}
}

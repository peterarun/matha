package com.matha.sales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.matha.controller.SchoolController;
import com.matha.controller.SchoolDetailsController;
import com.matha.repository.BookRepository;
import com.matha.service.SchoolService;
import com.matha.util.LoadUtils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.matha.repository")
@EntityScan("com.matha.domain")
@ComponentScan(basePackages = { "com.matha.controller", "com.matha.service" })
@EnableAutoConfiguration
public class SalesApplication extends Application {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private SchoolService schoolService;
	//
	// @Autowired
	// private FXMLLoader fxmlLoader;

	private Parent root;

	public static ConfigurableApplicationContext ctx;

	public static void main(String[] args) {
		// ctx = SpringApplication.run(SalesApplication.class, args);
		// BookRepository bookRepository1 = ctx.getBean(BookRepository.class);
		// Iterable<Book> books = bookRepository1.findAll();
		// for (Book book : books) {
		//
		// System.out.println(book);
		// }
		launch(args);
	}

	// @Override
	// public void run(String... args) throws Exception {
	//
	// launch(args);
	// }

	@Override
	public void init() throws Exception {
		ctx = SpringApplication.run(SalesApplication.class);

		// FXMLLoader fxmlLoader = new
		// FXMLLoader(getClass().getResource("../view/landing.fxml"));
		// fxmlLoader.setControllerFactory(ctx::getBean);

		FXMLLoader fxmlLoader = LoadUtils.loadFxml(this, "/fxml/landing.fxml");
		// fxmlLoader.setLocation(getClass().getResource("../view/landing.fxml"));
		root = fxmlLoader.load();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Stage mainStage;
			mainStage = primaryStage;
			// FXMLLoader loader = new
			// FXMLLoader(getClass().getResource("../view/landing.fxml"));
			// loader.setControllerFactory(ctx::getBean);
			Scene scene = new Scene(root);
			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			mainStage.setScene(scene);
			mainStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// Not used - setting location causes some other error. FXML location has to be
	// passed as constructor arg itself.
	// @Bean
	// @Scope("prototype")
	public FXMLLoader fxmlLoader() {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setControllerFactory(SalesApplication.ctx::getBean);
		return fxmlLoader;
	}
}

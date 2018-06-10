package com.matha.controller;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.matha.util.LoadUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;

import java.io.IOException;

import static com.matha.util.UtilConstants.*;

@Component
public class LandingController
{

	private static final Logger LOGGER = LogManager.getLogger(LandingController.class);

	@FXML
	private Tab salesTab;

	@FXML
	private Tab purchaseTab;

	@FXML
	private Tab cashBookTab;

	@FXML
	private Tab bookMgmtTab;

	@FXML
	private Tab statementTab;

	private void loadPage(Tab tabIn, String fxmlFile)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, fxmlFile);
			Parent addOrderRoot = createOrderLoader.load();
			tabIn.setContent(addOrderRoot);
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}					
	}
	
	@FXML
	public void loadSalesPage()
	{
		if (salesTab.isSelected())
		{
			loadPage(salesTab, schoolsFxml);
		}
	}

	@FXML
	public void loadPurchasePage()
	{
		if (purchaseTab.isSelected())
		{
			loadPage(purchaseTab, purchasePageFxmlFile);
		}
	}

	@FXML
	public void loadCashBook()
	{
		if (cashBookTab.isSelected())
		{
			loadPage(cashBookTab, cashBookFxml);		
		}
	}

	@FXML
	public void loadBooksPage()
	{
		if (bookMgmtTab.isSelected())
		{
			loadPage(bookMgmtTab, booksFxml);	
		}
	}

	@FXML
	public void loadStatementsPage()
	{
		if (statementTab.isSelected())
		{
			loadPage(statementTab, statementsFxml);
		}
	}

	@FXML
	public void openSearchTab(Event event)
	{
		try
		{
			FXMLLoader fxmlLoader = LoadUtils.loadFxml(this, masterSearchFxmlFile);
			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root);

			Stage stage = LoadUtils.loadChildStageFromMenu(salesTab.getTabPane().getScene(), scene);
			stage.show();
		}
		catch (IOException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

	}

	@FXML
	public void openOrderSearchTab(Event event)
	{
		try
		{
			FXMLLoader fxmlLoader = LoadUtils.loadFxml(this, masterSearchFxmlFile);
			Parent root = fxmlLoader.load();
			SearchController sc = fxmlLoader.getController();
			sc.initData(ORDER_SEARCH_STR);
			Scene scene = new Scene(root);

			Stage stage = LoadUtils.loadChildStageFromMenu(salesTab.getTabPane().getScene(), scene);
			stage.show();
		}
		catch (IOException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

	}

	@FXML
	public void openPurBillSearchTab(Event event)
	{
		try
		{
			FXMLLoader fxmlLoader = LoadUtils.loadFxml(this, masterSearchFxmlFile);
			Parent root = fxmlLoader.load();
			SearchController sc = fxmlLoader.getController();
			sc.initData(PUR_BILL_SEARCH_STR);
			Scene scene = new Scene(root);

			Stage stage = LoadUtils.loadChildStageFromMenu(salesTab.getTabPane().getScene(), scene);
			stage.show();
		}
		catch (IOException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

	}

	@FXML
	public void openSaleBillSearchTab(Event event)
	{
		try
		{
			FXMLLoader fxmlLoader = LoadUtils.loadFxml(this, masterSearchFxmlFile);
			Parent root = fxmlLoader.load();
			SearchController sc = fxmlLoader.getController();
			sc.initData(SALE_BILL_SEARCH_STR);
			Scene scene = new Scene(root);

			Stage stage = LoadUtils.loadChildStageFromMenu(salesTab.getTabPane().getScene(), scene);
			stage.show();
		}
		catch (IOException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

	}

}

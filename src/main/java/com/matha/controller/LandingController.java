package com.matha.controller;

import static com.matha.util.UtilConstants.booksFxml;
import static com.matha.util.UtilConstants.cashBookFxml;
import static com.matha.util.UtilConstants.purchasePageFxmlFile;
import static com.matha.util.UtilConstants.schoolsFxml;

import org.springframework.stereotype.Component;

import com.matha.util.LoadUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;

@Component
public class LandingController
{

	@FXML
	private Tab salesTab;

	@FXML
	private Tab purchaseTab;

	@FXML
	private Tab cashBookTab;

	@FXML
	private Tab bookMgmtTab;

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
}

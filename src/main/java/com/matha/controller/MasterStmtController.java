package com.matha.controller;

import com.matha.domain.PurchaseTransaction;
import com.matha.domain.SalesTransaction;
import com.matha.service.SchoolService;
import com.matha.util.LoadUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.matha.util.UtilConstants.*;

@Component
public class MasterStmtController
{

	private static final Logger LOGGER = LogManager.getLogger(MasterStmtController.class);
	
	@Autowired
	private SchoolService schoolService;

	@Value("${agencyName}")
	private String agencyName;

	@Value("${agencyAddress1}")
	private String agencyAddress1;

	@Value("${agencyAddress2}")
	private String agencyAddress2;

	@FXML
	private DatePicker purToDate;

	@FXML
	private DatePicker saleToDate;

	@FXML
	private ChoiceBox<String> saleType;

	@FXML
	private ChoiceBox<String> purType;

	@FXML
	private DatePicker saleFromDate;

	@FXML
	private DatePicker purFromDate;

	@FXML
	protected void initialize() throws IOException
	{
		List<String> purTxnTypes = Arrays.asList(PURCHASE_STR, PAYMENT_STR, RETURN_STR);
		this.purType.setItems(FXCollections.observableList(purTxnTypes));

		List<String> saleTxnTypes = Arrays.asList(SALE_STR, PAYMENT_STR, CREDIT_NOTE_STR);
		this.saleType.setItems(FXCollections.observableList(saleTxnTypes));
	}

	@FXML
	void generatePurStmt(ActionEvent event)
	{
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, masterStmtFxmlFile);
			Parent stmtRoot = createOrderLoader.load();
			PrintStmtController ctrl = createOrderLoader.getController();
			JasperPrint jasperPrint = prepareJasperPrintForPurchases(purFromDate.getValue(), purToDate.getValue(), purType.getValue());
			ctrl.initData(jasperPrint);
			Scene stmtScene = new Scene(stmtRoot);
			Stage stage = LoadUtils.loadChildStage(event, stmtScene);
			stage.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@FXML
	void prepareSaleStmt(ActionEvent event) {
		try
		{
			FXMLLoader createOrderLoader = LoadUtils.loadFxml(this, masterStmtFxmlFile);
			Parent stmtRoot = createOrderLoader.load();
			PrintStmtController ctrl = createOrderLoader.getController();
			JasperPrint jasperPrint = prepareJasperPrintForSales(saleFromDate.getValue(), saleToDate.getValue(), saleType.getValue());
			ctrl.initData(jasperPrint);
			Scene stmtScene = new Scene(stmtRoot);
			Stage stage = LoadUtils.loadChildStage(event, stmtScene);
			stage.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private JasperPrint prepareJasperPrintForPurchases(LocalDate fromDateVal, LocalDate toDateVal, String purTypeStr)
	{
		JasperPrint jasperPrint = null;
		InputStream jasperStream = getClass().getResourceAsStream(masterPurStmtJrxml);
		HashMap<String, Object> hm = new HashMap<>();
		try
		{
			if (toDateVal == null)
			{
				toDateVal = LocalDate.now();
			}

			Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "txnDate"), new Sort.Order(Sort.Direction.ASC, "id"));
			List<PurchaseTransaction> tableData = schoolService.fetchPurchaseTxnsBetween(fromDateVal, toDateVal, Optional.ofNullable(purTypeStr), sort);

			Double openingBalance = 0.0;
			if(tableData != null && !tableData.isEmpty())
			{
				if(tableData.get(0).getPrevTxn() != null)
				{
					openingBalance = tableData.get(0).getPrevTxn().getBalance();
				}
			}
			Double totalDebit = tableData.stream().collect(Collectors.summingDouble(o->  o.getMultiplier() == 1 ? o.getAmount() : 0.0));
			Double totalCredit = tableData.stream().collect(Collectors.summingDouble(o->  o.getMultiplier() == -1 ? o.getAmount() : 0.0));
			hm.put("openingBalance", openingBalance);
//			hm.put("reportData", tableData);
			hm.put("publisherName", "All");
			hm.put("publisherDetails", "N/A");
			hm.put("fromDate", fromDateVal);
			hm.put("toDate", toDateVal);
			hm.put("accountDetails", "Matha Distributors (Chennai)");
			hm.put("totalDebit", totalDebit);
			hm.put("totalCredit", totalCredit);

			LOGGER.info("Before" + new Date());
			JasperReport compiledFile = JasperCompileManager.compileReport(jasperStream);
			LOGGER.info("After" + new Date());

			jasperPrint = JasperFillManager.fillReport(compiledFile, hm, new JRBeanCollectionDataSource(tableData));
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return jasperPrint;
	}

	private JasperPrint prepareJasperPrintForSales(LocalDate fromDateVal, LocalDate toDateVal, String purTypeStr)
	{
		JasperPrint jasperPrint = null;
		InputStream jasperStream = getClass().getResourceAsStream(masterSaleStmtJrxml);
		HashMap<String, Object> hm = new HashMap<>();
		try
		{
			if (toDateVal == null)
			{
				toDateVal = LocalDate.now();
			}

			Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "txnDate"), new Sort.Order(Sort.Direction.ASC, "id"));
			List<SalesTransaction> tableData = schoolService.fetchSaleTxnsBetween(fromDateVal, toDateVal, Optional.ofNullable(purTypeStr), sort);

			Double openingBalance = 0.0;
			if(tableData != null && !tableData.isEmpty())
			{
				if(tableData.get(0).getPrevTxn() != null)
				{
					openingBalance = tableData.get(0).getPrevTxn().getBalance();
				}
			}
			Double totalDebit = tableData.stream().collect(Collectors.summingDouble(o->  o.getMultiplier() == 1 ? o.getAmount() : 0.0));
			Double totalCredit = tableData.stream().collect(Collectors.summingDouble(o->  o.getMultiplier() == -1 ? o.getAmount() : 0.0));
			hm.put("agencyName", agencyName);
			hm.put("agencyAddress1", agencyAddress1);
			hm.put("agencyAddress2", agencyAddress2);
			hm.put("openingBalance", openingBalance);
			hm.put("fromDate", fromDateVal);
			hm.put("toDate", toDateVal);
			hm.put("accountDetails", "Matha Distributors (Chennai)");
			hm.put("totalDebit", totalDebit);
			hm.put("totalCredit", totalCredit);

			LOGGER.info("Before" + new Date());
			JasperReport compiledFile = JasperCompileManager.compileReport(jasperStream);
			LOGGER.info("After" + new Date());

			jasperPrint = JasperFillManager.fillReport(compiledFile, hm, new JRBeanCollectionDataSource(tableData));
		}
		catch (JRException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return jasperPrint;
	}

}

package com.matha.controller;

import static com.matha.util.UtilConstants.Docx;
import static com.matha.util.UtilConstants.Excel;
import static com.matha.util.UtilConstants.PDF;
import static com.matha.util.UtilConstants.invoiceJrxml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.OrderItem;
import com.matha.domain.Publisher;
import com.matha.domain.Purchase;
import com.matha.service.SchoolService;
import com.matha.util.Utils;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Component
public class PrintPurchaseBillController
{

	@Autowired
	private SchoolService schoolService;

    @FXML
    private TextField publisherName;

    @FXML
    private ChoiceBox<String> saveType;

    @FXML
    private WebView invoiceData;

	private JasperPrint print;
	private Purchase purchase;

	public void initData(JasperPrint printIn, Purchase purchaseIn)
	{
		this.print = printIn;
		this.purchase = purchaseIn;
		this.loadWebInvoice(printIn);
		List<String> saveTypes = Arrays.asList(PDF,Excel,Docx);
		this.saveType.setItems(FXCollections.observableList(saveTypes));
		this.saveType.getSelectionModel().selectFirst();
	}

	private void loadWebInvoice(JasperPrint printIn)
	{
		try
		{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			HtmlExporter exporter = new HtmlExporter();
			exporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
			exporter.setExporterInput(new SimpleExporterInput(printIn));
			exporter.exportReport();

			String content = StringUtils.toEncodedString(outputStream.toByteArray(), Charset.defaultCharset());
			invoiceData.getEngine().loadContent(content);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private JasperPrint prepareJasperPrint(Publisher pub)
	{
		JasperPrint jasperPrint = null;
		InputStream jasperStream = getClass().getResourceAsStream(invoiceJrxml);
		HashMap<String, Object> hm = new HashMap<>();
		try
		{			
			Set<OrderItem> tableData = purchase.getOrderItems();
			 			
			hm.put("publisherName", pub.getName());
			hm.put("publisherDetails", pub.getStmtAddress());
			hm.put("partyName", "Matha");
			hm.put("partyAddress", "Chennai");
			hm.put("partyPhone", "934376232");
			hm.put("documentsThrough", "DIRECT");
			hm.put("despatchedTo", "SOmone");
			hm.put("invoiceNo", "2322M");
			hm.put("txnDate", "14-08-2017");
			hm.put("orderNumbers", "2223,5343/T,3234");
			hm.put("despatchedPer", "Person");
			hm.put("grNo", "3435232");
			hm.put("packageCnt", "5");
			hm.put("total", 20500.00);
			hm.put("discount", 500.00);
			hm.put("grandTotal", 20000.00);
			hm.put("imageFileName","Mayur_logo.jpg");
			
			JasperReport compiledFile = JasperCompileManager.compileReport(jasperStream);

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

	@FXML
	public void exportAndSave(ActionEvent ev)
	{
		try
		{

			String selection = saveType.getSelectionModel().getSelectedItem(); 
			String filterStr = "*.*";
			if(selection.equals(PDF))
			{
				filterStr = "*.pdf";
			}
			else if(selection.equals(Excel))
			{
				filterStr = "*.xls";
			}
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter(selection, filterStr)
                );
            
			File file = fileChooser.showSaveDialog(((Node) ev.getSource()).getScene().getWindow());
			if(selection.equals(PDF))
			{
				JasperExportManager.exportReportToPdfFile(print, file.getAbsolutePath());
			}
			else if(selection.equals(Excel))
			{
		        JRXlsxExporter exporter = new JRXlsxExporter();
		        exporter.setExporterInput(new SimpleExporterInput(print));		        
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));

		        exporter.exportReport();
			}
			else if(selection.equals(Docx))
			{
				JRDocxExporter exporter = new JRDocxExporter();
		        exporter.setExporterInput(new SimpleExporterInput(print));		        
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));

		        exporter.exportReport();
			}
			
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	@FXML
	public void printInvoice(ActionEvent ev)
	{
		try
		{
			Scene parentScene = ((Node) ev.getSource()).getScene();
			Window parentWindow = parentScene.getWindow();

			Utils.print(invoiceData, parentWindow, new Label());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}

package com.matha.controller;

import static com.matha.util.UtilConstants.Docx;
import static com.matha.util.UtilConstants.Excel;
import static com.matha.util.UtilConstants.PDF;
import static com.matha.util.UtilConstants.*;
import static com.matha.util.Utils.printJasper;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.matha.domain.Order;
import com.matha.domain.OrderItem;
import com.matha.domain.School;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
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
public class PrintOrderController
{
	
	@Value("${agencyName}")
	private String agencyName;
	
	@Value("${agencyAddress1}")
	private String agencyAddress1;
	
	@Value("${agencyAddress2}")
	private String agencyAddress2;

	@FXML
	private TextField schoolName;

	@FXML
	private ChoiceBox<String> saveType;

	@FXML
	private WebView invoiceData;

	private JasperPrint print;

	public void initData(JasperPrint printIn)
	{
		this.print = printIn;
		this.loadWebInvoice(printIn);
		List<String> saveTypes = Arrays.asList(PDF,Excel,Docx);
		this.saveType.setItems(FXCollections.observableList(saveTypes));
		this.saveType.getSelectionModel().selectFirst();
	}

	public JasperPrint prepareJasperPrint(Order orderIn)
	{
		JasperPrint jasperPrint = null;
		InputStream jasperStream = getClass().getResourceAsStream(orderJrxml);
		HashMap<String, Object> hm = new HashMap<>();
		try
		{			

			School school = orderIn.getSchool();
			this.schoolName.setText(school.getName());
			List<OrderItem> tableData = orderIn.getOrderItem().stream().sorted(comparing(oi -> oi.getSerialNum())).collect(toList());
			
			hm.put("agencyName", agencyName);
			hm.put("agencyAddress1", agencyAddress1);
			hm.put("agencyAddress2", agencyAddress2);
			hm.put("partyAddress", school.addressText());
			hm.put("partyPhone", "Ph - " + school.getPhone1());
			hm.put("txnDate", DATE_CONV.toString(orderIn.getOrderDate()));
			hm.put("deliveryDate", DATE_CONV.toString(orderIn.getDeliveryDate()));
			hm.put("desLocation", orderIn.getDesLocation());
			hm.put("orderNumber", orderIn.getSerialNo());
			
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
//			
//			invoiceData2 = new WebView();
//			invoiceData2.getEngine().loadContent(content);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

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
	public void printOrder(ActionEvent ev)
	{
		try
		{
			printJasper(print);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}

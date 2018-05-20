package com.matha.controller;

import com.matha.domain.SalesTransaction;
import com.matha.domain.School;
import com.matha.service.SchoolService;
import com.matha.util.Utils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.matha.util.UtilConstants.*;
import static com.matha.util.Utils.printJasper;

@Component
public class PrintStmtController
{

	private static final Logger LOGGER = LogManager.getLogger(PrintStmtController.class);

    @FXML
    private DatePicker fromDate;
    
    @FXML
    private DatePicker toDate;
    
	@FXML
	private ChoiceBox<String> saveType;

	@FXML
	private WebView invoiceData;

	private JasperPrint print;
	private School school;

	public void initData(JasperPrint printIn)
	{
		this.print = printIn;
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
			this.invoiceData.getEngine().loadContent(content);

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
	public void printStmt(ActionEvent ev)
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

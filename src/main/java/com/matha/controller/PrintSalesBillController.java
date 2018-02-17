package com.matha.controller;

import static com.matha.util.Utils.*;
import static com.matha.util.UtilConstants.Docx;
import static com.matha.util.UtilConstants.Excel;
import static com.matha.util.UtilConstants.PDF;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Component
public class PrintSalesBillController
{

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
	public void printInvoice(ActionEvent ev)
	{
		try
		{
			Scene parentScene = ((Node) ev.getSource()).getScene();
			Window parentWindow = parentScene.getWindow();

			printJasper(print);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}

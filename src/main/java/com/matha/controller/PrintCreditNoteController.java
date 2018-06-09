package com.matha.controller;

import com.matha.domain.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
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
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.matha.util.UtilConstants.*;
import static com.matha.util.Utils.convertDouble;
import static com.matha.util.Utils.getStringVal;
import static com.matha.util.Utils.printJasper;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Component
public class PrintCreditNoteController
{

	private static final Logger LOGGER = LogManager.getLogger(PrintCreditNoteController.class);

    @FXML
    private TextField schoolName;

    @FXML
    private ChoiceBox<String> saveType;

    @FXML
    private WebView invoiceData;
    
	private JasperPrint print;

	public void initData(School sch, SchoolReturn schoolReturn, Address salesAddr, Account acct, InputStream jasperStream)
	{
		this.print = prepareCreditNotePrint(sch, schoolReturn, salesAddr, acct, jasperStream);
		this.schoolName.setText(sch.getName());
		this.loadWebInvoice(this.print);
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
			LOGGER.error("Error...", e);
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
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	@FXML
	public void printInvoice(ActionEvent ev)
	{
		try
		{
			printJasper(print);
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	public static JasperPrint prepareCreditNotePrint(School sch, SchoolReturn schoolReturn, Address salesAddr, Account acct, InputStream jasperStream)
	{

		JasperPrint jasperPrint = null;
		HashMap<String, Object> hm = new HashMap<>();
		try
		{
			StringBuilder strBuild = new StringBuilder();
			strBuild.append(salesAddr.getAddress1());
			strBuild.append(NEW_LINE);
			strBuild.append(salesAddr.getAddress2());
			strBuild.append(COMMA_SIGN);
			strBuild.append(SPACE_SIGN);
			strBuild.append(salesAddr.getAddress3());
			strBuild.append(HYPHEN_SPC_SIGN);
			strBuild.append(salesAddr.getPin());
			strBuild.append(NEW_LINE);
			strBuild.append("Ph: ");
			strBuild.append(salesAddr.getPhone1());
			strBuild.append(SPACE_SIGN);
			strBuild.append("Mob: ");
			strBuild.append(salesAddr.getPhone2());
			strBuild.append(NEW_LINE);
			strBuild.append("Email: ");
			strBuild.append(salesAddr.getEmail());

			StringBuilder strBuildAcct = new StringBuilder(acct.getName());
			strBuildAcct.append(COMMA_SIGN);
			strBuildAcct.append(" State Bank of India");
			strBuildAcct.append(COMMA_SIGN);
			strBuildAcct.append(" Vazhakulam Branch");
			strBuildAcct.append(COMMA_SIGN);
			strBuildAcct.append(" A/C No: ");
			strBuildAcct.append(acct.getAccountNum());
			strBuildAcct.append(COMMA_SIGN);
			strBuildAcct.append(" IFSC");
			strBuildAcct.append(HYPHEN_SPC_SIGN);
			strBuildAcct.append(acct.getIfsc());

			List<SalesReturnDet> tableData = schoolReturn.getSalesReturnDetSet().stream().sorted(comparing(sd -> sd.getSlNum())).collect(toList());
			Double discAmt = schoolReturn.getDiscount();

			hm.put("partyName", sch.getName());
			hm.put("partyAddress", sch.addressText());
			hm.put("agencyName", "MATHA AGENCIES");
			hm.put("agencyDetails", strBuild.toString());
			hm.put("partyPhone", sch.getPhone1() == null ? sch.getPhone2() : sch.getPhone1());
			hm.put("creditNoteNum", schoolReturn.getCreditNoteNum());
			hm.put("txnDate", schoolReturn.getTxnDateStr());
			hm.put("total", schoolReturn.getSubTotal());
			hm.put("discount", discAmt);
			hm.put("grandTotal", schoolReturn.getNetAmount());
			hm.put("accountDet", strBuildAcct.toString());
			hm.put("grandTotalInWords", convertDouble(schoolReturn.getNetAmount()));

			JasperReport compiledFile = JasperCompileManager.compileReport(jasperStream);

			jasperPrint = JasperFillManager.fillReport(compiledFile, hm, new JRBeanCollectionDataSource(tableData));
		}
		catch (JRException e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}

		return jasperPrint;

	}

}

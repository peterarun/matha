package com.matha.util;

import static com.matha.util.UtilConstants.PERCENT_SIGN;
import static com.matha.util.UtilConstants.RUPEE_SIGN;

import java.util.Optional;

import javax.print.PrintException;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterName;

import org.apache.commons.lang.StringUtils;

import com.matha.domain.OrderItem;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextField;
import javafx.scene.transform.Scale;
import javafx.stage.Window;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

public class Utils
{

	public static Double getDoubleVal(TextField txt)
	{
		Double dblVal = null;
		String txtStr = txt.getText();
		if (StringUtils.isNotBlank(txtStr))
		{
			dblVal = Double.parseDouble(txtStr);
		}
		return dblVal;
	}

	public static Double getDoubleVal(String txt)
	{
		Double dblVal = null;		
		if (StringUtils.isNotBlank(txt))
		{
			dblVal = Double.parseDouble(txt);
		}
		return dblVal;
	}
	
	public static Integer getIntegerVal(TextField txt)
	{
		Integer dblVal = null;
		String txtStr = txt.getText();
		if (StringUtils.isNotBlank(txtStr))
		{
			dblVal = Integer.parseInt(txtStr);
		}
		return dblVal;
	}

	public static String getStringVal(Double txt)
	{
		String txtVal = null;
		if (txt != null)
		{
			txtVal = String.format("%.2f", txt);
		}
		return txtVal;
	}

	public static String getStringVal(Integer txt)
	{
		String txtVal = null;
		if (txt != null)
		{
			txtVal = txt.toString();
		}
		return txtVal;
	}

	public static void print(Node node, Window parentWindow, Label jobStatus)
	{

		if (jobStatus == null)
		{
			jobStatus = new Label();
		}
		// Define the Job Status Message
		jobStatus.textProperty().unbind();
		jobStatus.setText("Printing...");

		Printer printer = Printer.getDefaultPrinter();
		ChoiceDialog<Printer> dialog = new ChoiceDialog<Printer>(Printer.getDefaultPrinter(), Printer.getAllPrinters());
		dialog.setHeaderText("Choose the printer!");
		dialog.setContentText("Choose a printer from available printers");
		dialog.setTitle("Printer Choice");
		Optional<Printer> opt = dialog.showAndWait();
		if (opt.isPresent())
		{
			printer = opt.get();
		}

		// Create a printer job for the default printer
		PrinterJob job = PrinterJob.createPrinterJob(printer);
		Paper paper = Paper.A4;
		PageOrientation orient = PageOrientation.PORTRAIT;

		PageLayout lout = printer.createPageLayout(paper, orient, 0, 0, 0, 0);
		double scaleX = 0.75;
		// double scaleY = 0.5;
		node.getTransforms().add(new Scale(scaleX, 1));

		if (job != null && job.showPageSetupDialog(parentWindow))
		{
			// Show the printer job status
			jobStatus.textProperty().bind(job.jobStatusProperty().asString());

			// Print the node
			boolean printed = job.printPage(lout, node);

			if (printed)
			{
				// End the printer job
				job.endJob();
			}
			else
			{
				// Write Error Message
				jobStatus.textProperty().unbind();
				jobStatus.setText("Printing failed.");
			}
		}
		else
		{
			// Write Error Message
			jobStatus.setText("Could not create a printer job.");
		}
	}

	public static void printJasper(JasperPrint jasperPrint) throws PrintException, JRException
	{
		Label jobStatus = new Label();

		// Define the Job Status Message
		jobStatus.textProperty().unbind();
		jobStatus.setText("Printing...");

		Printer printerJob = Printer.getDefaultPrinter();
		ChoiceDialog<Printer> dialog = new ChoiceDialog<Printer>(Printer.getDefaultPrinter(), Printer.getAllPrinters());
		dialog.setHeaderText("Choose the printer!");
		dialog.setContentText("Choose a printer from available printers");
		dialog.setTitle("Printer Choice");
		Optional<Printer> opt = dialog.showAndWait();
		if (opt.isPresent())
		{
			printerJob = opt.get();
		}

		// Set the printing settings
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
		printRequestAttributeSet.add(MediaSizeName.ISO_A4);
		if (jasperPrint.getOrientationValue() == net.sf.jasperreports.engine.type.OrientationEnum.LANDSCAPE)
		{
			printRequestAttributeSet.add(OrientationRequested.LANDSCAPE);
		}
		else
		{
			printRequestAttributeSet.add(OrientationRequested.PORTRAIT);
		}
		PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
		printServiceAttributeSet.add(new PrinterName(printerJob.getName(), null));

		JRPrintServiceExporter exporter = new JRPrintServiceExporter();
		SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
		configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
		configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
		// configuration.setDisplayPageDialog(true);
		// configuration.setDisplayPrintDialog(true);

		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setConfiguration(configuration);

		exporter.exportReport();
	}

	public static Callback<CellDataFeatures<OrderItem, String>, ObservableValue<String>> fetchPriceColumnFactory()
	{
		Callback<CellDataFeatures<OrderItem, String>, ObservableValue<String>> priceColumnFactory = new Callback<CellDataFeatures<OrderItem, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<OrderItem, String> p)
			{
				// p.getValue() returns the Person instance for a particular TableView row
				return new ReadOnlyStringWrapper(getStringVal(p.getValue().getBookPrice()));
			}
		};
		return priceColumnFactory;
	}

	public static void calcNetAmountGen(String discAmtStr, TextField subTotal, RadioButton percentRad, RadioButton rupeeRad, String otherChargesField, TextField netAmt)
	{
		String netTotalStr = netAmt.getText();
		Double netTotalDbl = StringUtils.isEmpty(netTotalStr) ? 0.0 : Double.parseDouble(netTotalStr);
		Double otherCharges = getDoubleVal(otherChargesField);

		String subTotalStr = subTotal.getText();
		if (subTotalStr != null)
		{
			double subTotalDbl = Double.parseDouble(subTotalStr);
			if (subTotalDbl > 0)
			{
				double discAmtDbl = StringUtils.isEmpty(discAmtStr) ? 0 : Double.parseDouble(discAmtStr);

				if (discAmtDbl > 0)
				{
					if (rupeeRad.isSelected())
					{
						netTotalDbl = subTotalDbl - discAmtDbl;
					}
					else if (percentRad.isSelected())
					{
						netTotalDbl = subTotalDbl - subTotalDbl * discAmtDbl / 100;
					}
				}
				else
				{
					netTotalDbl = subTotalDbl;
				}
			}
		}
		if(otherCharges != null)
		{
			netTotalDbl += otherCharges;
		}
		netAmt.setText(getStringVal(netTotalDbl));
	}

	public static void loadDiscSymbol(RadioButton percentRad, RadioButton rupeeRad, Label discTypeInd)
	{
		if (percentRad.isSelected())
		{
			discTypeInd.setText(PERCENT_SIGN);
		}
		else if (rupeeRad.isSelected())
		{
			discTypeInd.setText(RUPEE_SIGN);
		}
	}

	public static String convertDouble(double dbl)
	{
		int mainPart = (int) dbl;
		int decPart = (int) ((dbl - mainPart) * 100);
		return convert(mainPart) + " rupees and " + convert(decPart) + " paisa only";
	}
	
	public static String convert(int n)
	{
		final String[] units = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
				"Eighteen", "Nineteen" };

		final String[] tens = { "", // 0
				"", // 1
				"Twenty", // 2
				"Thirty", // 3
				"Forty", // 4
				"Fifty", // 5
				"Sixty", // 6
				"Seventy", // 7
				"Eighty", // 8
				"Ninety" // 9
		};

		if (n < 0)
		{
			return "Minus " + convert(-n);
		}

		if (n < 20)
		{
			return units[n];
		}

		if (n < 100)
		{
			return tens[n / 10] + ((n % 10 != 0) ? " " : "") + units[n % 10];
		}

		if (n < 1000)
		{
			return units[n / 100] + " Hundred" + ((n % 100 != 0) ? " And " : "") + convert(n % 100);
		}

		if (n < 100000)
		{
			return convert(n / 1000) + " Thousand" + ((n % 10000 != 0) ? " " : "") + convert(n % 1000);
		}

		if (n < 10000000)
		{
			return convert(n / 100000) + " Lakh" + ((n % 100000 != 0) ? " " : "") + convert(n % 100000);
		}

		return convert(n / 10000000) + " Crore" + ((n % 10000000 != 0) ? " " : "") + convert(n % 10000000);
	}
}

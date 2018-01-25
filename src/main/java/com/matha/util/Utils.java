package com.matha.util;

import java.util.Optional;

import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class Utils {

	public static Double getDoubleVal(TextField txt) {
		Double dblVal = null;
		String txtStr = txt.getText();
		if (txtStr != null) {
			dblVal = Double.parseDouble(txtStr);
		}
		return dblVal;
	}

	public static void print(Node node, Window parentWindow, Label jobStatus) {

		if(jobStatus == null)
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
		if (opt.isPresent()) {
		    printer = opt.get();
		}
		
		// Create a printer job for the default printer
		PrinterJob job = PrinterJob.createPrinterJob(printer);
		Paper paper = Paper.A3;
		PageOrientation orient = PageOrientation.PORTRAIT;
		PageLayout lout = printer.createPageLayout(paper, orient, 0, 0, 0, 0);

		if (job != null && job.showPrintDialog(parentWindow))
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
}

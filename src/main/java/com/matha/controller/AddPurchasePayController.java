package com.matha.controller;

import static com.matha.util.UtilConstants.NEW_LINE;
import static com.matha.util.Utils.getDoubleVal;
import static com.matha.util.Utils.getStringVal;
import static com.matha.util.Utils.showErrorAlert;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.Publisher;
import com.matha.domain.PurchasePayment;
import com.matha.domain.PurchaseTransaction;
import com.matha.service.SchoolService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class AddPurchasePayController
{

	@Autowired
	private SchoolService schoolService;

	@FXML
	private TextField publisherDetails;

	@FXML
	private Label message;

	@FXML
	private DatePicker payDate;

	@FXML
	private TextField amount;

	@FXML
	private TextField mode;

	@FXML
	private TextField notes;

	@FXML
	private Button cancelBtn;

	private Publisher publisher;
	private PurchasePayment purchasePayment;


	private boolean validateData()
	{
		boolean valid = true;
		StringBuilder errorMsg = new StringBuilder();
		if(this.amount.getText() == null)
		{
			errorMsg.append("Please provide an Amount");
			errorMsg.append(NEW_LINE);
			valid = false;
		}
		if (this.payDate.getValue() == null)
		{
			errorMsg.append("Please provide a Payment Date");
			valid = false;
		}
		showErrorAlert("Error in Saving Order", "Please correct the following errors", errorMsg.toString());
		return valid;
	}

	public void initData(Publisher schoolIn, PurchasePayment schoolPaymentIn)
	{
		this.publisher = schoolIn;
		this.purchasePayment = schoolPaymentIn;
		this.publisherDetails.setText(this.publisher.getAddress());
		
		if (schoolPaymentIn != null)
		{
			payDate.setValue(schoolPaymentIn.getTxnDate());
			mode.setText(schoolPaymentIn.getPaymentMode());
			if (schoolPaymentIn.getSalesTxn() != null && schoolPaymentIn.getSalesTxn().getAmount() != null)
			{
				amount.setText(getStringVal(schoolPaymentIn.getSalesTxn().getAmount()));
			}
			notes.setText(StringUtils.defaultString(schoolPaymentIn.getSalesTxn().getNote()));
		}
	}

	private void loadMessage(String msg)
	{
		message.setText(msg);
		message.setVisible(true);
	}

	@FXML
	void saveData(ActionEvent event)
	{
		try
		{
			if(!validateData())
			{
				return;
			}

			PurchasePayment sPayment = this.purchasePayment;			
			if (sPayment == null)
			{
				sPayment = new PurchasePayment();
			}

			PurchaseTransaction sTxn = sPayment.getSalesTxn();
			if (sTxn == null)
			{
				sTxn = new PurchaseTransaction();
				sTxn.setPublisher(publisher);
			}

			sPayment.setPaymentMode(mode.getText());

			double amountVal = getDoubleVal(amount);
			sTxn.setAmount(amountVal);
			sTxn.setNote(notes.getText());
			sTxn.setTxnDate(payDate.getValue());

			schoolService.savePurchasePay(sPayment, sTxn);
			((Stage) cancelBtn.getScene().getWindow()).close();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			loadMessage(e.getMessage());
		}
	}

	@FXML
	void cancelOperation(ActionEvent event)
	{
		((Stage) cancelBtn.getScene().getWindow()).close();
	}
}

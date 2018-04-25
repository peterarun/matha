package com.matha.controller;

import static com.matha.util.UtilConstants.NEW_LINE;
import static com.matha.util.Utils.showErrorAlert;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.SalesTransaction;
import com.matha.domain.School;
import com.matha.domain.SchoolPayment;
import com.matha.service.SchoolService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
public class AddPaymentController
{

	@Autowired
	private SchoolService schoolService;

	@FXML
	private TextField mode;

	@FXML
	private TextField amount;

	@FXML
	private TextField notes;

	@FXML
	private Button cancelBtn;

	@FXML
	private Label schoolName;

	@FXML
	private DatePicker payDate;

	private School school;
	private SchoolPayment schoolPayment;

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
	
	@FXML
	void saveData(ActionEvent event)
	{
		if(!validateData())
		{
			return;
		}
		SchoolPayment sPayment = schoolPayment;
		if (schoolPayment == null)
		{
			sPayment = new SchoolPayment();
			SalesTransaction sTransaction = new SalesTransaction();
			sTransaction.setSchool(school);
			sPayment.setSalesTxn(sTransaction);
		}

		sPayment.setPaymentMode(mode.getText());
		SalesTransaction sTxn = sPayment.getSalesTxn();
		String amountStr = amount.getText();
		double amountVal = 0.0;
		if (StringUtils.isNotBlank(amountStr))
		{
			amountVal = Double.parseDouble(amountStr);
		}
		sTxn.setAmount(amountVal);
		sTxn.setNote(notes.getText());
		sTxn.setTxnDate(payDate.getValue());

		schoolService.savePayment(sPayment);
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void cancelOperation(ActionEvent event)
	{
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	public void initData(School schoolIn, SchoolPayment schoolPaymentIn)
	{
		this.school = schoolIn;
		this.schoolPayment = schoolPaymentIn;

		if (schoolPaymentIn != null)
		{
			payDate.setValue(schoolPaymentIn.getTxnDate());
			mode.setText(schoolPaymentIn.getPaymentMode());
			if (schoolPaymentIn.getSalesTxn().getAmount() != null)
			{
				amount.setText(schoolPaymentIn.getSalesTxn().getAmount().toString());
			}
			notes.setText(StringUtils.defaultString(schoolPaymentIn.getSalesTxn().getNote()));
		}

	}

}

package com.matha.controller;

import static com.matha.util.UtilConstants.NEW_LINE;
import static com.matha.util.Utils.getDoubleVal;
import static com.matha.util.Utils.getStringVal;
import static com.matha.util.Utils.showErrorAlert;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.matha.domain.Publisher;
import com.matha.domain.PurchasePayment;
import com.matha.domain.PurchaseTransaction;
import com.matha.service.SchoolService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class AddPurchasePayController
{

	private static final Logger LOGGER = LogManager.getLogger(AddPurchasePayController.class);

	@Autowired
	private SchoolService schoolService;

	@FXML
	private TextField publisherDetails;

	@FXML
	private Label datedStr;

	@FXML
	private DatePicker dated;

	@FXML
	private TextField receiptNum;

	@FXML
	private DatePicker payDate;

	@FXML
	private TextField amount;

	@FXML
	private ChoiceBox<String> mode;

	@FXML
	private TextField refNum;

	@FXML
	private TextField notes;

	@FXML
	private Button cancelBtn;

	private Publisher publisher;
	private PurchasePayment purchasePayment;

	@Value("${purPaymentModes}")
	private String[] schoolPaymentModes;

	@Value("${datedPurPaymentModes}")
	private String[] datedSchoolPaymentModes;

	@FXML
	protected void initialize() throws IOException
	{
		List datedSchoolPaymentModesList = Arrays.asList(datedSchoolPaymentModes);
		this.mode.setItems(FXCollections.observableList(Arrays.asList(schoolPaymentModes)));
		this.mode.setOnAction( ev -> {
			if(datedSchoolPaymentModesList.contains(this.mode.getValue()))
			{
				this.dated.setDisable(false);
				this.datedStr.setDisable(false);
				this.refNum.setDisable(false);
			}
			else
			{
				this.dated.setDisable(true);
				this.datedStr.setDisable(true);
				this.refNum.setDisable(true);
			}
		});
	}

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
		if(!valid)
		{
			showErrorAlert("Error in Saving Order", "Please correct the following errors", errorMsg.toString());
		}
		return valid;
	}

	public void initData(Publisher schoolIn, PurchasePayment schoolPaymentIn)
	{
		this.publisher = schoolIn;
		this.purchasePayment = schoolPaymentIn;
		this.publisherDetails.setText(this.publisher.getName());
		
		if (schoolPaymentIn != null)
		{
			payDate.setValue(schoolPaymentIn.getTxnDate());
			mode.getSelectionModel().select(schoolPaymentIn.getPaymentMode());
			dated.setValue(schoolPaymentIn.getDated());
			receiptNum.setText(schoolPaymentIn.getReceiptNum());
			if (schoolPaymentIn.getSalesTxn() != null && schoolPaymentIn.getSalesTxn().getAmount() != null)
			{
				amount.setText(getStringVal(schoolPaymentIn.getSalesTxn().getAmount()));
			}
			refNum.setText(schoolPaymentIn.getReferenceNum());
			notes.setText(StringUtils.defaultString(schoolPaymentIn.getSalesTxn().getNote()));
		}
		else
		{
			this.mode.getSelectionModel().select(0);
		}
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

			sPayment.setReceiptNum(receiptNum.getText());
			sPayment.setPaymentMode(mode.getValue());
			sPayment.setDated(dated.getValue());
			sPayment.setReferenceNum(refNum.getText());

			double amountVal = getDoubleVal(amount);
			sTxn.setAmount(amountVal);
			sTxn.setNote(notes.getText());
			sTxn.setTxnDate(payDate.getValue());

			schoolService.savePurchasePay(sPayment, sTxn);
			((Stage) cancelBtn.getScene().getWindow()).close();
		}
		catch (Throwable e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
			showErrorAlert("Error in Saving Order", "An Unexpected Error has occurred", e.getMessage());
		}
	}

	@FXML
	void cancelOperation(ActionEvent event)
	{
		((Stage) cancelBtn.getScene().getWindow()).close();
	}
}

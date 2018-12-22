package com.matha.controller;

import com.matha.domain.SalesTransaction;
import com.matha.domain.School;
import com.matha.domain.SchoolPayment;
import com.matha.service.SchoolService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.matha.util.UtilConstants.NEW_LINE;
import static com.matha.util.Utils.showErrorAlert;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddPaymentController
{

	private static final Logger LOGGER = LogManager.getLogger("AddPaymentController");

	@Autowired
	private SchoolService schoolService;

	@FXML
	private Label schoolName;

	@FXML
	private TextField receiptNum;

	@FXML
	private DatePicker payDate;

	@FXML
	private TextField amount;

	@FXML
	private ChoiceBox<String> mode;

	@FXML
	private Label datedStr;

	@FXML
	private DatePicker dated;

	@FXML
	private TextField refNum;

	@FXML
	private TextField notes;

	@FXML
	private Button cancelBtn;

	private School school;
	private SchoolPayment schoolPayment;

	@Value("${schoolPaymentModes}")
	private String[] schoolPaymentModes;

	@Value("${datedSchoolPaymentModes}")
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
		if(isBlank(this.receiptNum.getText()))
		{
			errorMsg.append("Please provide a Receipt Number");
			errorMsg.append(NEW_LINE);
			valid = false;
		}
		if(isBlank(this.amount.getText()))
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
	
	@FXML
	void saveData(ActionEvent event)
	{
		try
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

			sPayment.setReceiptNum(receiptNum.getText());
			sPayment.setPaymentMode(mode.getValue());
			sPayment.setDated(dated.getValue());
			sPayment.setReferenceNum(refNum.getText());

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
		catch (DataIntegrityViolationException e)
		{
			LOGGER.error("Error...", e);
			showErrorAlert("Error in Saving Payment", "Please correct the following errors", "Duplicate Entry Found");
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			showErrorAlert("Error in Saving Payment", "Please correct the following errors", e.getMessage());
		}
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
		this.schoolName.setText(schoolIn.getName());

		if (schoolPaymentIn != null)
		{
			this.receiptNum.setText(schoolPaymentIn.getReceiptNum());
			this.payDate.setValue(schoolPaymentIn.getTxnDate());
			this.mode.getSelectionModel().select(schoolPaymentIn.getPaymentMode());
			this.dated.setValue(schoolPaymentIn.getDated());
			this.refNum.setText(schoolPaymentIn.getReferenceNum());
			if (schoolPaymentIn.getSalesTxn().getAmount() != null)
			{
				this.amount.setText(schoolPaymentIn.getSalesTxn().getAmount().toString());
			}
			this.notes.setText(StringUtils.defaultString(schoolPaymentIn.getSalesTxn().getNote()));
		}
	}

}

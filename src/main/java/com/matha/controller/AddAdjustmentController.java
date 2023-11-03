package com.matha.controller;

import com.matha.domain.SalesTransaction;
import com.matha.domain.School;
import com.matha.domain.SchoolAdjustment;
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

import static com.matha.util.UtilConstants.NEW_LINE;
import static com.matha.util.Utils.showErrorAlert;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddAdjustmentController
{

	private static final Logger LOGGER = LogManager.getLogger("AddAdjustmentController");

	@Autowired
	private SchoolService schoolService;

	@FXML
	private Label schoolName;

	@FXML
	private DatePicker adjDate;

	@FXML
	private TextField amount;

	@FXML
	private ChoiceBox<String> type;

	@FXML
	private TextField notes;

	@FXML
	private Button cancelBtn;

	private School school;

	private SchoolAdjustment schoolAdjustment;

	@Value("${schoolAdjustmentModes}")
	private String[] schoolAdjustmentModes;

	@FXML
	protected void initialize() throws IOException
	{
		this.type.setItems(FXCollections.observableList(Arrays.asList(schoolAdjustmentModes)));
	}

	private boolean validateData()
	{
		boolean valid = true;
		StringBuilder errorMsg = new StringBuilder();

		if(isBlank(this.amount.getText()))
		{
			errorMsg.append("Please provide an Amount");
			errorMsg.append(NEW_LINE);
			valid = false;
		}
		if (this.adjDate.getValue() == null)
		{
			errorMsg.append("Please provide a Date");
			valid = false;
		}
		if(!valid)
		{
			showErrorAlert("Error in Saving Adjustment", "Please correct the following errors", errorMsg.toString());
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
			SchoolAdjustment adjustment = schoolAdjustment;
			if (schoolAdjustment == null)
			{
				adjustment = new SchoolAdjustment();
				SalesTransaction sTransaction = new SalesTransaction();
				sTransaction.setSchool(school);
				adjustment.setSalesTxn(sTransaction);
				adjustment.setSchool(school);
			}

			adjustment.setAdjType(type.getValue());
			adjustment.setAdjDate(adjDate.getValue());

			SalesTransaction sTxn = adjustment.getSalesTxn();
			String amountStr = amount.getText();
			double amountVal = 0.0;
			if (StringUtils.isNotBlank(amountStr))
			{
				amountVal = Double.parseDouble(amountStr);
			}
			sTxn.setAmount(amountVal);
			sTxn.setNote(notes.getText());
			sTxn.setTxnDate(adjDate.getValue());

			schoolService.saveAdjustment(adjustment);
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

	public void initData(School schoolIn, SchoolAdjustment schoolPaymentIn)
	{
		this.school = schoolIn;
		this.schoolAdjustment = schoolPaymentIn;
		this.schoolName.setText(schoolIn.getName());

		if (schoolPaymentIn != null)
		{

			this.adjDate.setValue(schoolPaymentIn.getTxnDate());
			this.type.getSelectionModel().select(schoolPaymentIn.getAdjType());
			this.adjDate.setValue(schoolPaymentIn.getAdjDate());
			if (schoolPaymentIn.getSalesTxn().getAmount() != null)
			{
				this.amount.setText(schoolPaymentIn.getSalesTxn().getAmount().toString());
			}
			this.notes.setText(StringUtils.defaultString(schoolPaymentIn.getSalesTxn().getNote()));
		}
	}

}

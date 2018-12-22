package com.matha.controller;

import java.time.LocalDate;
import java.util.List;

import com.matha.util.Converters;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.matha.domain.CashBook;
import com.matha.domain.CashHead;
import com.matha.service.SchoolService;
import com.matha.util.Utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import static com.matha.util.Converters.getCashHeadConverter;
import static com.matha.util.UtilConstants.NEW_LINE;
import static com.matha.util.Utils.*;
import static javafx.collections.FXCollections.observableList;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddTxnController {

    @FXML
    private TextField mode;

    @FXML
    private TextField amount;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField description;

    @FXML
    private TextField orderNum;

    @FXML
    private Label schoolName;

    @FXML
    private DatePicker txnDate;

	@FXML
	private ChoiceBox<CashHead> cashHead;
	
	@FXML
	private TextField representative;
	
	@Autowired
	private SchoolService srvc;

	private CashBook item = null;

	@FXML
	protected void initialize()
	{
		item = new CashBook();
		this.txnDate.setValue(LocalDate.now());
		List<CashHead> cashHeads = srvc.fetchCashHeads();
		this.cashHead.setConverter(getCashHeadConverter());
		this.cashHead.setItems(observableList(cashHeads));
	}

	@FXML
	void saveData(ActionEvent e)
	{
		if(!this.validateData())
		{
			return;
		}
		item.setId(getIntegerVal(this.orderNum.getText()));
		item.setAmount(getDoubleVal(this.amount));
		item.setTxnDate(this.txnDate.getValue());
		item.setDescription(this.description.getText());
		item.setMode(this.mode.getText());
		item.setType(this.cashHead.getSelectionModel().getSelectedItem());
		item.setRepresentative(this.representative.getText());
		srvc.saveCashBook(item);

		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	private boolean validateData()
	{
		boolean valid = true;
		StringBuilder errorMsg = new StringBuilder();
		if(this.orderNum.getText() == null)
		{
			errorMsg.append("Please provide a Transaction Number");
			errorMsg.append(NEW_LINE);
			valid = false;
		}
		if (this.txnDate.getValue() == null)
		{
			errorMsg.append("Please provide a Transaction Date");
			valid = false;
		}
		if (this.amount.getText() == null)
		{
			errorMsg.append("Please provide an Amount");
			valid = false;
		}
		if(!valid)
		{
			showErrorAlert("Error in Saving Cash Transaction", "Please correct the following errors", errorMsg.toString());
		}
		return valid;
	}

	@FXML
	void cancelOperation(ActionEvent e) {
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	public void initTxnId()
	{
		this.orderNum.setText(getStringVal(srvc.fetchNextTxnVal()));
	}

	public void updateFormData(CashBook item)
	{
		this.item = item;
		this.orderNum.setText(item.getId().toString());
		this.amount.setText(item.getAmount().toString());
		this.txnDate.setValue(item.getTxnDate());
		this.description.setText(item.getDescription());
		this.mode.setText(item.getMode());
		this.representative.setText(item.getRepresentative());
		this.cashHead.getSelectionModel().select(item.getType());
		this.orderNum.setDisable(true);
	}

}

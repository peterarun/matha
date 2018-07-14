package com.matha.controller;

import java.time.LocalDate;
import java.util.List;

import com.matha.util.Converters;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.CashBook;
import com.matha.domain.CashHead;
import com.matha.service.SchoolService;
import com.matha.util.Utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import static com.matha.util.Converters.getCashHeadConverter;
import static javafx.collections.FXCollections.observableList;

@Component
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

	
	@FXML
	protected void initialize() {
		this.txnDate.setValue(LocalDate.now());
		List<CashHead> cashHeads = srvc.fetchCashHeads();
		this.cashHead.setConverter(getCashHeadConverter());
		this.cashHead.setItems(observableList(cashHeads));
	}

	@FXML
	void saveData(ActionEvent e) {

		CashBook item = new CashBook();
		item.setAmount(Utils.getDoubleVal(this.amount));
		item.setTxnDate(this.txnDate.getValue());
		item.setDescription(this.description.getText());
		item.setMode(this.mode.getText());
		item.setType(this.cashHead.getSelectionModel().getSelectedItem());
		srvc.saveCashBook(item);

		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void cancelOperation(ActionEvent e) {
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	public void updateFormData(CashBook item) {
		this.orderNum.setText(item.getId().toString());
		this.amount.setText(item.getAmount().toString());
		this.txnDate.setValue(item.getTxnDate());
		this.description.setText(item.getDescription());
		this.mode.setText(item.getMode());
		this.cashHead.getSelectionModel().select(item.getType());		
	}

}

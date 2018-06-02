package com.matha.controller;

import static com.matha.util.UtilConstants.NEW_LINE;
import static com.matha.util.UtilConstants.PERCENT_SIGN;
import static com.matha.util.UtilConstants.RUPEE_SIGN;
import static com.matha.util.Utils.getDoubleVal;
import static com.matha.util.Utils.getStringVal;
import static com.matha.util.Utils.showErrorAlert;
import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.matha.domain.*;
import com.matha.util.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.service.SchoolService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

@Component
public class AddPurchaseRetController
{

	private static final Logger LOGGER = LogManager.getLogger(AddPurchaseRetController.class);

	@Autowired
	private SchoolService schoolService;

	@FXML
	private TextField publisherDetails;
	
	@FXML
	private TextField creditNoteNum;
	
	@FXML
	private DatePicker returnDate;

	@FXML
	private TextField bookName;
	
	@FXML
	private TextField quantity;
	
	@FXML
	private TextField price;

	@FXML
	private TableView<PurchaseReturnDet> addedBooks;

	@FXML
	private ToggleGroup discType;

	@FXML
	private RadioButton rupeeRad;

	@FXML
	private RadioButton percentRad;

	@FXML
	private TextField discText;

	@FXML
	private Label discTypeInd;

	@FXML
	private TextField subTotal;

	@FXML
	private TextField calcDiscount;

	@FXML
	private TextField netTotal;

	@FXML
	private TextField notes;

	@FXML
	private Button cancelBtn;

	private Publisher publisher;
	private PurchaseReturn purchaseReturn;
	private Map<String, Book> bookMap;
	private AtomicInteger index = new AtomicInteger();
	private Collector<PurchaseReturnDet, ?, Double> summingDblCollector = Collectors.summingDouble(PurchaseReturnDet::getTotalBought);
	private Collector<Book, ?, Map<String, Book>> bookMapCollector = Collectors.toMap(o -> o.getShortName() + ": " + o.getName() + " - " + o.getPublisherName(), o -> o);	
	
	void initData(Publisher publisherIn, PurchaseReturn purchaseReturnIn)
	{
		this.publisher = publisherIn;
		this.purchaseReturn = purchaseReturnIn;
		this.publisherDetails.setText(this.publisher.getName());
		
		this.loadReturnData();

		List<Book> allOrders = schoolService.fetchBooksForPublisher(publisher);
		bookMap = allOrders.stream().collect(bookMapCollector);

		List<String> items = new ArrayList<>(bookMap.keySet());
		TextFields.bindAutoCompletion(bookName, items);

		this.discText.textProperty().addListener((observable, oldValue, newValue) -> {
			updateNetAmt();
		});

		discType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle)
			{
				if (discType.getSelectedToggle() != null)
				{
					calcNetAmount(discText.getText());
					loadDiscSymbol();
				}
			}
		});
	}

	private void loadReturnData()
	{
		if (purchaseReturn != null)
		{
			if (purchaseReturn.getPurchaseReturnDetSet() != null)
			{
				ObservableList<PurchaseReturnDet> orderItemsIn = FXCollections
						.observableList(new ArrayList<PurchaseReturnDet>(purchaseReturn.getPurchaseReturnDetSet()));
				this.addedBooks.setItems(orderItemsIn);
				this.index.set(orderItemsIn.size());
			}
			this.returnDate.setValue(purchaseReturn.getSalesTxn().getTxnDate());
			this.notes.setText(purchaseReturn.getSalesTxn().getNote());
			this.creditNoteNum.setText(purchaseReturn.getCreditNoteNum());
			this.subTotal.setText(getStringVal(purchaseReturn.getSubTotal()));
			if(purchaseReturn.getDiscAmt() != null)
			{
				this.rupeeRad.setSelected(true);
				this.discText.setText(getStringVal(purchaseReturn.getDiscAmt()));
			}
			else
			{
				this.discText.setText(getStringVal(purchaseReturn.getDiscPerc()));
			}
			this.calcDiscount.setText(getStringVal(purchaseReturn.getDiscount()));
			
			if (purchaseReturn.getSalesTxn() != null)
			{
				this.netTotal.setText(getStringVal(purchaseReturn.getSalesTxn().getAmount()));
			}
		}
		else
		{
			purchaseReturn = new PurchaseReturn();
		}
	}

	private void loadSubTotal()
	{
		ObservableList<PurchaseReturnDet> orderItemsIn = this.addedBooks.getItems();
		Double subTotalDbl = orderItemsIn.stream().collect(summingDblCollector);
		this.subTotal.setText(subTotalDbl.toString());		
	}

	private void calcNetAmount(String discAmtStr)
	{
		String netTotalStr = netTotal.getText();
		Double netTotalDbl = StringUtils.isEmpty(netTotalStr) ? 0.0 : Double.parseDouble(netTotalStr);

		String subTotalStr = this.subTotal.getText();
		double discVal = 0;
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
						discVal = discAmtDbl;
						netTotalDbl = subTotalDbl - discVal;
					}
					else if (percentRad.isSelected())
					{
						discVal = subTotalDbl * discAmtDbl / 100;
						netTotalDbl = subTotalDbl - discVal;
					}
				}
				else
				{
					netTotalDbl = subTotalDbl;
				}
			}
		}
		this.netTotal.setText(getStringVal(netTotalDbl));
		this.calcDiscount.setText(getStringVal(discVal));
	}

	private void loadDiscSymbol()
	{
		if (this.percentRad.isSelected())
		{
			this.discTypeInd.setText(PERCENT_SIGN);
		}
		else if (this.rupeeRad.isSelected())
		{
			this.discTypeInd.setText(RUPEE_SIGN);
		}
	}

	@FXML
	void addBookData(ActionEvent event)
	{
		String bookStr = this.bookName.getText();
		PurchaseReturnDet itemIn = new PurchaseReturnDet(index.incrementAndGet(),
				Integer.parseInt(this.quantity.getText()),
				Double.parseDouble(this.price.getText()),this.bookMap.get(bookStr));
		
		this.addedBooks.getItems().add(itemIn);
		LOGGER.debug("Added Item: " + itemIn);
		
		loadSubTotal();
		String discAmtStr = StringUtils.defaultString(discText.getText());
		calcNetAmount(discAmtStr);
		clearBookFields();
		this.bookName.requestFocus();
	}
	
	private void clearBookFields()
	{
		this.bookName.clear();
		this.quantity.clear();
		this.price.clear();
		this.addedBooks.getSelectionModel().clearSelection();
	}

	private boolean validateData()
	{
		boolean valid = true;
		StringBuilder errorMsg = new StringBuilder();
		if(this.subTotal.getText() == null)
		{
			errorMsg.append("Please provide an Amount");
			errorMsg.append(NEW_LINE);
			valid = false;
		}
		if (this.returnDate.getValue() == null)
		{
			errorMsg.append("Please provide a Return Date");
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
		if(!validateData())
		{
			return;
		}
		
		PurchaseReturn returnIn = this.purchaseReturn;		
		if(returnIn == null)
		{
			returnIn = new PurchaseReturn();						
		}
		if(percentRad.isSelected())
		{
			returnIn.setDiscPerc(getDoubleVal(this.discText.getText()));
		}
		else
		{
			returnIn.setDiscAmt(getDoubleVal(this.discText.getText()));
		}
		returnIn.setSubTotal(getDoubleVal(this.subTotal.getText()));
		returnIn.setCreditNoteNum(this.creditNoteNum.getText());
		
		PurchaseTransaction salesTxn = returnIn.getSalesTxn();
		if(salesTxn == null)
		{
			salesTxn = new PurchaseTransaction();
			salesTxn.setPublisher(publisher);
		}
		
		TreeSet<PurchaseReturnDet> orderItems = new TreeSet<>(comparing(PurchaseReturnDet::getSlNum));
		orderItems.addAll(this.addedBooks.getItems());
		
		salesTxn.setTxnDate(this.returnDate.getValue());
		salesTxn.setNote(this.notes.getText());
		salesTxn.setAmount(getDoubleVal(this.netTotal));
		
		schoolService.savePurchaseReturn(returnIn, salesTxn, orderItems);
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	private void updateNetAmt()
	{
		String discAmtStr = StringUtils.defaultString(discText.getText());
		if (!StringUtils.isEmpty(discAmtStr))
		{
			calcNetAmount(discAmtStr);
		}
	}

	@FXML
	void cancelOperation(ActionEvent event)
	{
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void removeOperation(ActionEvent event)
	{
		PurchaseReturnDet itemIn = this.addedBooks.getSelectionModel().getSelectedItem();
		this.addedBooks.getItems().remove(itemIn);
		loadSubTotal();
		String discAmtStr = StringUtils.defaultString(discText.getText());
		calcNetAmount(discAmtStr);
		clearBookFields();
		this.bookName.requestFocus();
	}

}

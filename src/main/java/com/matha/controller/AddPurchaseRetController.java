package com.matha.controller;

import static com.matha.util.UtilConstants.*;
import static com.matha.util.Utils.*;
import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.matha.domain.*;
import com.matha.util.Utils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.matha.service.SchoolService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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

	@FXML
	private TableColumn<PurchaseReturnDet, String> qtyColumn;

	@FXML
	private TableColumn<PurchaseReturnDet, String> priceColumn;

	@FXML
	private TableColumn<PurchaseReturnDet, String> totalColumn;

	private Publisher publisher;
	private PurchaseReturn purchaseReturn;
	private Map<String, Book> bookMap;
	private AtomicInteger index = new AtomicInteger();
	private Collector<PurchaseReturnDet, ?, Double> summingDblCollector = Collectors.summingDouble(PurchaseReturnDet::getTotalBought);
	private Collector<Book, ?, Map<String, Book>> bookMapCollector = Collectors.toMap(o -> o.getBookNum() + ": " + o.getName() + " - " + o.getPublisherName(), o -> o);
	
	void initData(Publisher publisherIn, PurchaseReturn purchaseReturnIn)
	{
		this.publisher = publisherIn;
		this.purchaseReturn = purchaseReturnIn;
		this.publisherDetails.setText(this.publisher.getName());
		
		this.loadReturnData();
		this.loadAddDefaults();

		List<Book> allOrders = schoolService.fetchBooksForPublisher(publisher);
		bookMap = allOrders.stream().collect(bookMapCollector);

		List<String> items = new ArrayList<>(bookMap.keySet());
		TextFields.bindAutoCompletion(bookName, items);

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
			if(purchaseReturn.getDiscType() == null || !purchaseReturn.getDiscType())
			{
				this.rupeeRad.setSelected(true);
			}
			this.discText.setText(getStringVal(purchaseReturn.getDiscAmt()));
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

	private void loadAddDefaults()
	{
		this.addedBooks.getSelectionModel().cellSelectionEnabledProperty().set(true);

		this.priceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.priceColumn.setCellValueFactory(p -> new ReadOnlyStringWrapper(getStringVal(p.getValue().getBookPrice())));
		this.priceColumn.setOnEditCommit(t -> loadRateCol(t));

		this.qtyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.qtyColumn.setCellValueFactory(p -> new ReadOnlyStringWrapper(getStringVal(p.getValue().getQty())));
		this.qtyColumn.setOnEditCommit(t -> loadQtyCol(t));

		this.publisherDetails.setText(this.publisher.getName());
		this.totalColumn.setCellValueFactory(cellData ->
				Bindings.format("%.2f", cellData.getValue().getTotalBought()));

		this.discText.textProperty().addListener((observable, oldValue, newValue) -> {
			calcNetAmount(this.discText.getText());
		});

		discType.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (discType.getSelectedToggle() != null)
            {
                calcNetAmount(discText.getText());
                loadDiscSymbol();
            }
        });
	}


	private void loadRateCol(TableColumn.CellEditEvent<PurchaseReturnDet, String> t)
	{
		PurchaseReturnDet oItem = ((PurchaseReturnDet) t.getTableView().getItems().get(t.getTablePosition().getRow()));
		oItem.setRate(Double.parseDouble(t.getNewValue()));
		reCalculate(t);
	}

	private void loadQtyCol(TableColumn.CellEditEvent<PurchaseReturnDet, String> t)
	{
		PurchaseReturnDet oItem = ((PurchaseReturnDet) t.getTableView().getItems().get(t.getTablePosition().getRow()));
		oItem.setQty(Integer.parseInt(t.getNewValue()));
		reCalculate(t);
	}

	private void reCalculate(TableColumn.CellEditEvent<PurchaseReturnDet, String> t)
	{
		t.getTableView().refresh();
		loadSubTotal();
		calcNetAmount(this.discText.getText());
		t.getTableView().getSelectionModel().selectBelowCell();
		int rowId = t.getTableView().getSelectionModel().getSelectedIndex();
		if (rowId < t.getTableView().getItems().size())
		{
			if(rowId > 0)
			{
				t.getTableView().scrollTo(rowId - 1);
			}
			t.getTableView().edit(rowId, t.getTablePosition().getTableColumn());
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
		Double netTotalDbl = isEmpty(netTotalStr) ? 0.0 : Double.parseDouble(netTotalStr);

		String subTotalStr = this.subTotal.getText();
		double discVal = 0;
		if (subTotalStr != null)
		{
			double subTotalDbl = Double.parseDouble(subTotalStr);
			if (subTotalDbl > 0)
			{
				double discAmtDbl = isEmpty(discAmtStr) ? 0 : Double.parseDouble(discAmtStr);
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
		Book book = this.bookMap.get(bookStr);
		if(book == null)
		{
			showErrorAlert("Invalid Book", "Invalid Book Entry", bookStr + " is not corresponding to a valid Book Entry");
			return;
		}

		PurchaseReturnDet itemIn = new PurchaseReturnDet(index.incrementAndGet(),
				Integer.parseInt(this.quantity.getText()),
				Double.parseDouble(this.price.getText()),
				book);
		
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

		if(isBlank(this.subTotal.getText()))
		{
			errorMsg.append("Please provide an Amount");
			errorMsg.append(NEW_LINE);
			valid = false;
		}
		LocalDate retDt = this.returnDate.getValue();
		if (retDt == null)
		{
			errorMsg.append("Please provide a Return Date");
			valid = false;
		}
		String creditNoteNum = this.creditNoteNum.getText();
		if (isBlank(creditNoteNum))
		{
			errorMsg.append("Please provide a Credit Note Number");
			valid = false;
		}
		Integer fy = calcFinYear(retDt);
		PurchaseReturn existingRet = schoolService.fetchPurchaseReturn(creditNoteNum, fy);
		if(this.purchaseReturn == null && existingRet != null)
		{
			errorMsg.append("Return reference provided already exists for the Financial Year");
			valid = false;
		}

		if(this.addedBooks == null || this.addedBooks.getItems() == null || this.addedBooks.getItems().isEmpty())
		{
			errorMsg.append("Please add some records");
			valid = false;
		}
		else
		{
			if(!isFilled(this.addedBooks.getItems()))
			{
				errorMsg.append("Please provide quantity and rate for all records");
				valid = false;
			}
		}

		if(!valid)
		{
			showErrorAlert("Error in Saving Purchase Bill", "Please correct the following errors", errorMsg.toString());
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

			LocalDate returnDate = this.returnDate.getValue();

			PurchaseReturn returnIn = this.purchaseReturn;
			if(returnIn == null)
			{
				returnIn = new PurchaseReturn();
			}

			PurchaseTransaction salesTxn = returnIn.getSalesTxn();
			if(salesTxn == null)
			{
				salesTxn = new PurchaseTransaction();
				salesTxn.setPublisher(publisher);
			}
			salesTxn.setTxnDate(returnDate);
			salesTxn.setNote(this.notes.getText());
			salesTxn.setAmount(getDoubleVal(this.netTotal));

			if(returnIn.getPublisher() == null && salesTxn.getPublisher() != null)
			{
				returnIn.setPublisher(salesTxn.getPublisher());
			}

			if(percentRad.isSelected())
			{
				returnIn.setDiscType(true);
			}
			else
			{
				returnIn.setDiscType(false);
			}
			returnIn.setDiscAmt(getDoubleVal(this.discText.getText()));
			returnIn.setSubTotal(getDoubleVal(this.subTotal.getText()));
			returnIn.setCreditNoteNum(this.creditNoteNum.getText());
			returnIn.setReturnDate(returnDate);
			returnIn.setFy(calcFinYear(returnDate));

			TreeSet<PurchaseReturnDet> orderItems = new TreeSet<>(comparing(PurchaseReturnDet::getSlNum));
			orderItems.addAll(this.addedBooks.getItems());


			schoolService.savePurchaseReturn(returnIn, salesTxn, orderItems);
			((Stage) cancelBtn.getScene().getWindow()).close();
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			showErrorAlert("Error in Saving Purchase Bill", "Please correct the following errors", e.getMessage());
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
		int orderNumSel = this.addedBooks.getSelectionModel().getSelectedIndex();
		this.addedBooks.getItems().remove(orderNumSel);

		loadSubTotal();
		String discAmtStr = StringUtils.defaultString(discText.getText());
		calcNetAmount(discAmtStr);
		clearBookFields();
		this.bookName.requestFocus();
	}

}

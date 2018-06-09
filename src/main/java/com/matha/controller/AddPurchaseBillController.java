package com.matha.controller;

import static com.matha.util.UtilConstants.DATE_CONV;
import static com.matha.util.UtilConstants.NEW_LINE;
import static com.matha.util.UtilConstants.PERCENT_SIGN;
import static com.matha.util.UtilConstants.RUPEE_SIGN;
import static com.matha.util.Utils.*;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.matha.domain.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.service.SchoolService;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

@Component
public class AddPurchaseBillController
{

	private static final Logger LOGGER = LogManager.getLogger("AddPurchaseBillController");

	@Autowired
	private SchoolService schoolService;

	@FXML
	private TextArea publisherDetails;

	@FXML
	private Label message;

	@FXML
	private TextField invoiceNum;

	@FXML
	private DatePicker invoiceDate;

	@FXML
	private TextField despatchedTo;

	@FXML
	private TextField docsThrough;

	@FXML
	private ListView<String> orderList;

	@FXML
	private TextField orderNum;

	@FXML
	private TextField despatchedPer;

	@FXML
	private TextField grNum;

	@FXML
	private TextField packageCount;

	@FXML
	private TextField note;

	@FXML
	private TableView<PurchaseDet> addedBooks;

	@FXML
	private TableColumn<PurchaseDet, String> priceColumn;

	@FXML
	private TableColumn<PurchaseDet, String> totalColumn;

	@FXML
	private TextField bookName;

	@FXML
	private TextField quantity;

	@FXML
	private TextField price;
	
	@FXML
	private TextField subTotal;

	@FXML
	private ToggleGroup discType;

	@FXML
	private RadioButton rupeeRad;

	@FXML
	private RadioButton percentRad;

	@FXML
	private TextField discAmt;

	@FXML
	private TextField discCalc;
	
	@FXML
	private Label discTypeInd;

	@FXML
	private TextField totalCount;

	@FXML
	private TextField netAmt;

	@FXML
	private Button cancelBtn;

	private Publisher publisher;
	private Purchase purchase;
	private AtomicInteger index = new AtomicInteger();
	private Map<String, Order> orderMap = new HashMap<>();
	private List<String> items = new ArrayList<>();
	private Set<Order> orderSet = new HashSet<>();
	private Map<String, Book> bookMap;
	private Collector<Order, ?, Map<String, Order>> orderMapCollector = toMap(o -> o.getSerialNo(), o -> o);
	private Collector<PurchaseDet, ?, Double> summingDblCollector = summingDouble(PurchaseDet::getTotalBought);

	private void loadAddDefaults()
	{
		this.invoiceDate.setConverter(DATE_CONV);
		this.addedBooks.getSelectionModel().cellSelectionEnabledProperty().set(true);
		this.priceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.priceColumn.setCellValueFactory(fetchPurPriceColumnFactory());
		this.priceColumn.setOnEditCommit(new EventHandler<CellEditEvent<PurchaseDet, String>>() {
			public void handle(CellEditEvent<PurchaseDet, String> t)
			{
				PurchaseDet oItem = ((PurchaseDet) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				oItem.setRate(Double.parseDouble(t.getNewValue()));
				t.getTableView().refresh();
				loadSubTotal();
				calcNetAmount(discAmt.getText());
				calculateTotalQty();
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
		});
		this.publisherDetails.setText(this.publisher.getName());
		this.totalColumn.setCellValueFactory(cellData -> 
			Bindings.format("%.2f", cellData.getValue().getTotalBought()));
		
		this.discAmt.textProperty().addListener((observable, oldValue, newValue) -> {		    
		    updateNetAmt();
		});
	}

	private void prepareEditData(Purchase purchaseIn)
	{
		if (purchaseIn != null)
		{
			this.invoiceNum.setText(purchaseIn.getInvoiceNo());
			this.invoiceDate.setValue(purchaseIn.getTxnDate());
			this.despatchedTo.setText(purchaseIn.getDespatchedTo());
			this.docsThrough.setText(purchaseIn.getDocsThrough());
			this.despatchedPer.setText(purchaseIn.getDespatchPer());
			this.grNum.setText(purchaseIn.getGrNum());
			this.packageCount.setText(getStringVal(purchaseIn.getPackages()));
			this.subTotal.setText(getStringVal(purchaseIn.getSubTotal()));
			this.discAmt.setText(getStringVal(purchaseIn.getDiscAmt()));
			this.discCalc.setText(getStringVal(purchaseIn.getCalculatedDisc()));
			this.netAmt.setText(getStringVal(purchaseIn.getNetAmount()));

			if (purchaseIn.getPurchaseItems() != null && !purchaseIn.getPurchaseItems().isEmpty())
			{
				orderSet = purchaseIn.getPurchaseItems().stream()
						.filter(po -> po.getOrderItem() != null)
						.map(poi -> poi.getOrderItem().getOrder())
						.filter(o -> o != null)
						.collect(Collectors.toSet());
				List<String> orderIdList = orderSet.stream().map(Order::getSerialNo).collect(Collectors.toList());
				this.orderList.setItems(FXCollections.observableList(orderIdList));
				this.index.set(orderIdList.size());

				List<PurchaseDet> bookItems = new ArrayList<>();

				if (purchaseIn.getPurchaseItems() != null)
				{
					bookItems.addAll(purchaseIn.getPurchaseItems());
				}
				this.addedBooks.setItems(FXCollections.observableList(bookItems));
				this.calculateTotalQty();
			}
			if (purchaseIn.getDiscType() == null || !purchaseIn.getDiscType())
			{
				this.rupeeRad.setSelected(true);
				this.discTypeInd.setText(RUPEE_SIGN);
			}
		}
	}

	void initData(Set<Order> ordersIn, Publisher publisherIn, Purchase purchaseIn, Map<String, Book> bookMapIn)
	{
		this.publisher = publisherIn;
		this.purchase = purchaseIn;
		this.prepareEditData(purchaseIn);
		this.loadAddDefaults();

		List<Order> allOrders = schoolService.fetchUnBilledOrders(publisherIn);

		this.orderMap = allOrders.stream().collect(orderMapCollector);
		items = new ArrayList<>(orderMap.keySet());
		TextFields.bindAutoCompletion(orderNum, items);

		if (ordersIn != null)
		{
			List<String> orderStrings = ordersIn.stream().map(o -> o.getSerialNo()).collect(Collectors.toList());
			orderList.setItems(FXCollections.observableList(orderStrings));

			// To avoid different objects of the same type getting loaded.
			for (Order order : ordersIn)
			{
				orderMap.put(order.getSerialNo(), order);
			}
			loadBooksAndSubTotal(ordersIn);
			calculateTotalQty();
			resetItems();
		}

		// Adding Existing orders to the Map
		if (orderSet != null)
		{
			for (Order order : orderSet)
			{
				orderMap.put(order.getSerialNo(), order);
			}
		}

		bookMap = bookMapIn;

		List<String> items = new ArrayList<>(bookMap.keySet());
		TextFields.bindAutoCompletion(bookName, items);

		discType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle)
			{
				if (discType.getSelectedToggle() != null)
				{
					calcNetAmount(discAmt.getText());
					loadDiscSymbol();
					calculateTotalQty();
				}
			}
		});
	}

	private void addItems(List<PurchaseDet> bookItems)
	{
		if (addedBooks.getItems() == null)
		{
			addedBooks.setItems(FXCollections.observableList(new ArrayList<>()));
		}
		ObservableList<PurchaseDet> itemsIn = addedBooks.getItems();
		itemsIn.addAll(bookItems);
		addedBooks.getSelectionModel().clearSelection();
	}

	@FXML
	void addBookData(ActionEvent event)
	{
		String bookStr = this.bookName.getText();
		PurchaseDet itemIn = new PurchaseDet(index.incrementAndGet(),
				Integer.parseInt(this.quantity.getText()),
				Double.parseDouble(this.price.getText()),
				this.bookMap.get(bookStr));

		this.addedBooks.getItems().add(itemIn);
		LOGGER.debug("Added Item: " + itemIn);

		loadSubTotal();
		String discAmtStr = StringUtils.defaultString(discAmt.getText());
		calcNetAmount(discAmtStr);
		clearBookFields();
		this.bookName.requestFocus();
	}

	private void loadBooksAndSubTotal(Set<Order> ordersIn)
	{
		if (ordersIn != null)
		{
			List<PurchaseDet> bookItems = ordersIn.stream()
					.map(Order::getOrderItem)
					.flatMap(List::stream)
					.map(oi -> new PurchaseDet(null,
							null,
							index.incrementAndGet(),
							oi))
					.collect(toList());
			addItems(bookItems);
		}
		loadSubTotal();
		calcNetAmount(discAmt.getText());
		clearBookFields();
		this.bookName.requestFocus();
	}

	private void loadSubTotal()
	{
		double subTotalVal = 0;

		ObservableList<PurchaseDet> orderListIn = addedBooks.getItems();
		subTotalVal += orderListIn.stream().collect(summingDblCollector);
		subTotal.setText(getStringVal(subTotalVal));
	}

	private void resetItems()
	{
		items.removeAll(orderList.getItems());
	}

	@FXML
	private void updateNetAmt()
	{
		String discAmtStr = StringUtils.defaultString(discAmt.getText());		
		if (!StringUtils.isEmpty(discAmtStr))
		{
			calcNetAmount(discAmtStr);
		}
	}

	@FXML
	void addOrderNum(ActionEvent e)
	{
		String orderText = orderNum.getText();
		orderList.getItems().add(orderText);
		orderNum.clear();

		HashSet<Order> ordersIn = new HashSet<Order>();
//		ObservableList<String> orderListIn = orderList.getItems();
//		for (String string : orderListIn)
//		{
			ordersIn.add(orderMap.get(orderText));
//		}
		loadBooksAndSubTotal(ordersIn);
		resetItems();
	}

	private void calcNetAmount(String discAmtStr)
	{
		String netTotalStr = netAmt.getText();
		Double netTotalDbl = StringUtils.isEmpty(netTotalStr) ? 0.0 : Double.parseDouble(netTotalStr);

		String subTotalStr = this.subTotal.getText();
		double discVal = 0;
		if (subTotalStr != null)
		{
			double subTotalDbl = Double.parseDouble(subTotalStr);
			double discAmtDbl = StringUtils.isEmpty(discAmtStr) ? 0 : Double.parseDouble(discAmtStr);

			if (subTotalDbl > 0)
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
		this.netAmt.setText(getStringVal(netTotalDbl));	
		this.discCalc.setText(getStringVal(discVal));
		this.calculateTotalQty();
	}

	private void calculateTotalQty()
	{
		ObservableList<PurchaseDet> orderItems = addedBooks.getItems();
		int bookQty = 0;
		bookQty += orderItems.stream().collect(Collectors.summingInt(PurchaseDet::getQty));
		this.totalCount.setText(getStringVal(bookQty));
	}

	@FXML
	void saveData(ActionEvent event)
	{
		try
		{
			if (!this.validateData())
			{
				return;
			}
			Purchase purchaseIn = this.purchase;
			if (purchaseIn == null)
			{
				purchaseIn = new Purchase();
				purchaseIn.setPurchaseDate(LocalDate.now());
				purchaseIn.setFinancialYear(calcFinYear(purchaseIn.getTxnDate()));
				purchaseIn.setSerialNo(this.schoolService.fetchNextPurchaseSerialNum(purchaseIn.getFinancialYear()));
				purchaseIn.setPublisher(this.publisher);
				purchaseIn.setInvoiceNo(this.invoiceNum.getText());
			}
			PurchaseTransaction salesTxn = purchaseIn.getSalesTxn();
			if (salesTxn == null)
			{
				salesTxn = new PurchaseTransaction();
				salesTxn.setPublisher(this.publisher);
			}
			preparePurchase(purchaseIn);
			prepareTransaction(salesTxn);

			schoolService.savePurchase(purchaseIn, this.addedBooks.getItems(), salesTxn);

			((Stage) cancelBtn.getScene().getWindow()).close();
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			e.printStackTrace();
		}
	}

	private boolean validateData()
	{
		boolean valid = true;
		StringBuilder errorMsg = new StringBuilder();
		if (StringUtils.isBlank(this.invoiceNum.getText()))
		{
			errorMsg.append("Please provide an Invoice number");
			errorMsg.append(NEW_LINE);
			valid = false;
		}
		if (this.invoiceDate.getValue() == null)
		{
			errorMsg.append("Please provide a date");
			valid = false;
		}
		if(!valid)
		{
			showErrorAlert("Error in Saving Order", "Please correct the following errors", errorMsg.toString());
		}
		return valid;
	}
	
	private void preparePurchase(Purchase sale)
	{
		sale.setDiscAmt(getDoubleVal(this.discAmt));
		if (percentRad.isSelected())
		{
			sale.setDiscType(true);
		}
		else if (rupeeRad.isSelected())
		{
			sale.setDiscType(false);
		}
		sale.setSubTotal(getDoubleVal(this.subTotal));
		sale.setDespatchedTo(this.despatchedTo.getText());
		sale.setDocsThrough(this.docsThrough.getText());
		sale.setDespatchPer(this.despatchedPer.getText());
		sale.setGrNum(this.grNum.getText());
		sale.setPackages(getIntegerVal(this.packageCount));
		sale.setDeletedAmt(getDoubleVal(this.netAmt));
	}

	private void prepareTransaction(PurchaseTransaction txn)
	{
		txn.setAmount(getDoubleVal(this.netAmt));
		txn.setNote(this.note.getText());
		txn.setTxnDate(this.invoiceDate.getValue());
	}

	@FXML
	void cancelOperation(ActionEvent event)
	{
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void removeOperation(ActionEvent event)
	{
		String orderNumSel = this.orderList.getSelectionModel().getSelectedItem();
		if (orderNumSel != null)
		{
			this.orderList.getItems().remove(orderNumSel);
			Order removedOrder = orderMap.get(orderNumSel);
			this.addedBooks.getItems().removeAll(removedOrder.getOrderItem());
		}

		loadBooksAndSubTotal(null);
	}

	@FXML
	void removeOrderItem(ActionEvent event)
	{
		ObservableList<PurchaseDet> orderNumSel = this.addedBooks.getSelectionModel().getSelectedItems();
		if (orderNumSel != null)
		{
			this.addedBooks.getItems().removeAll(orderNumSel);
		}

		loadBooksAndSubTotal(null);
		reBalanceOrders();
	}
	
	private void reBalanceOrders()
	{
		ObservableList<PurchaseDet> orderItems = this.addedBooks.getItems();
		Set<String> orderNumSet = orderItems.stream()
				.filter(pi -> pi.getOrderItem() != null)
				.map(pi -> pi.getOrderItem().getOrder())
				.filter(pi -> pi.getSerialNo() != null)
				.map(Order::getSerialNo)
				.collect(Collectors.toSet());
		this.orderList.getItems().retainAll(orderNumSet);
	}
	
	private void loadMessage(String msg)
	{
		message.setText(msg);
		message.setVisible(true);
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


	private void clearBookFields()
	{
		this.bookName.clear();
		this.quantity.clear();
		this.price.clear();
		this.addedBooks.getSelectionModel().clearSelection();
	}
}

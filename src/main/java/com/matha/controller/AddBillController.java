package com.matha.controller;

import com.matha.domain.*;
import com.matha.service.SchoolService;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.matha.util.UtilConstants.*;
import static com.matha.util.Utils.*;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddBillController
{

	private static final Logger LOGGER = LogManager.getLogger("AddBillController");

	@Autowired
	private SchoolService schoolService;

	@FXML
	private TextArea schoolDetails;

	@FXML
	private DatePicker billDate;

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
	private Label discTypeInd;

	@FXML
	private TextField calculatedDisc;
	
	@FXML
	private TextField otherCharges;
	
	@FXML
	private TextField netAmt;

	@FXML
	private Button cancelBtn;

	@FXML
	private TextField orderNum;

	@FXML
	private TextField despatchPer;

	@FXML
	private TextField docsThru;

	@FXML
	private TextField invoiceNum;

	@FXML
	private TextField grNum;

	@FXML
	private TextField packageCnt;

	@FXML
	private ListView<String> orderList;

	@FXML
	private TableView<SalesDet> addedBooks;

	@FXML
	private TableColumn<SalesDet, String> slNoCol;

	@FXML
	private TableColumn<SalesDet, String> bkNameCol;

	@FXML
	private TableColumn<SalesDet, String> priceColumn;

	@FXML
	private TableColumn<SalesDet, String> totalColumn;
	
	@FXML
	private TableColumn<SalesDet, String> qtyColumn;

	@FXML
	private TextField bookText;

	@FXML
	private TextField bookCnt;

	@FXML
	private TextField bookPrice;

	private School school;
	private Sales selectedSale;
//	private ObservableSet<Order> orders;
	private Map<String, Book> bookMap;

	private Map<String, Order> orderMap = new HashMap<>();
	private Collector<Order, ?, Map<String, Order>> orderMapCollector = Collectors.toMap(o -> o.getSerialNo(), o -> o);
	private Collector<SalesDet, ?, Double> summingDblCollector = Collectors.summingDouble(SalesDet::getTotalSold);

	@Value("${listOfDespatchers}")
	private String[] despatcherArray;

	private boolean dirty;

	void initData(ObservableList<Order> ordersIn, School schoolIn, Sales sale, Map<String, Book> bookMapIn)
	{
		StopWatch stopwatch = new StopWatch("Edit Bill");
		stopwatch.start("Prepare Edit");
		this.school = schoolIn;
		this.selectedSale = sale;
		this.bookMap = bookMapIn;
		this.prepareEditData(sale);

		printResetStopWatch(stopwatch, "Add defaults");
		this.loadAddDefaults();
		List<Order> allOrders = schoolService.fetchOrderForSchool(schoolIn);
		this.orderMap = allOrders.stream().collect(orderMapCollector);

		printResetStopWatch(stopwatch, "Auto Completions");
		List<String> items = new ArrayList<>(this.orderMap.keySet());
		TextFields.bindAutoCompletion(this.orderNum, items);
		
		LOGGER.debug("despatcherArray: " + despatcherArray);
		TextFields.bindAutoCompletion(this.despatchPer, Arrays.asList(despatcherArray));

		if (ordersIn != null)
		{
			Set<String> orderStrings = ordersIn.stream().map(o -> o.getSerialNo()).collect(toSet());
			this.orderList.setItems(FXCollections.observableList(new ArrayList<>(orderStrings)));

			// To avoid different objects of the same type getting loaded.
			for (Order order : ordersIn)
			{
				orderMap.put(order.getSerialNo(), order);
			}
		}
		printResetStopWatch(stopwatch, "Load books and sub total");
		loadNewBooksAndSubTotal(ordersIn);
		loadDiscSymbol(percentRad, rupeeRad, discTypeInd);

		this.slNoCol.prefWidthProperty().bind(this.addedBooks.widthProperty().multiply(0.1));
		this.bkNameCol.prefWidthProperty().bind(this.addedBooks.widthProperty().multiply(0.5));
		this.qtyColumn.prefWidthProperty().bind(this.addedBooks.widthProperty().multiply(0.12));
		this.priceColumn.prefWidthProperty().bind(this.addedBooks.widthProperty().multiply(0.12));
		this.totalColumn.prefWidthProperty().bind(this.addedBooks.widthProperty().multiply(0.16));
		this.bookText.prefWidthProperty().bind(this.addedBooks.widthProperty().multiply(0.42));
		this.bookCnt.prefWidthProperty().bind(this.addedBooks.widthProperty().multiply(0.1));

		this.dirty = false;
		ObservableList<SalesDet> fxObs = this.addedBooks.getItems();
		if(fxObs != null)
		{
			LOGGER.info("Adding Listener");
			ListChangeListener<SalesDet> lcL= c -> {
				LOGGER.info("Setting Dirty");
				dirty = true;
			};
			fxObs.addListener(lcL);
		}
		printResetStopWatch(stopwatch, null);
	}

	private void loadAddDefaults()
	{
		this.billDate.setConverter(DATE_CONV);
		if(this.billDate.getValue() == null)
		{
			this.billDate.setValue(LocalDate.now());
		}
		this.schoolDetails.setText(this.school.addressText());
		this.addedBooks.getSelectionModel().cellSelectionEnabledProperty().set(true);
		this.priceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		this.priceColumn.setCellValueFactory(fetchSalesPrcColFactory());
		this.priceColumn.setOnEditCommit(fetchSalesPrcEventHandler());
		
		this.qtyColumn.setCellFactory(TextFieldTableCell.forTableColumn());		
		this.qtyColumn.setCellValueFactory(fetchSalesCntFactory());
		this.qtyColumn.setOnEditCommit(fetchSalesQtyEventHandler());
		
		this.discType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle)
			{
				if (discType.getSelectedToggle() != null)
				{
					calcNetAmount(discAmt.getText(), otherCharges.getText());
					loadDiscSymbol(percentRad, rupeeRad, discTypeInd);
				}
			}
		});

		this.totalColumn.setCellValueFactory(cellData -> 
			Bindings.format("%.2f", cellData.getValue().getTotalSold())
		);
		
		if(isBlank(this.invoiceNum.getText()))
		{
			Integer newInvNum = schoolService.fetchNextSalesInvoiceNum(calcFinYear(LocalDate.now()));
			LOGGER.info("newInvNum " + newInvNum);
			this.invoiceNum.setText(getStringVal(newInvNum));
		}
		
		List<String> items = new ArrayList<>(bookMap.keySet());
		AutoCompletionBinding<String> bookNameBinding = TextFields.bindAutoCompletion(bookText, items);
		bookNameBinding.prefWidthProperty().bind(this.bookText.widthProperty().multiply(1.4));

		this.discAmt.textProperty().addListener((observable, oldValue, newValue) -> {		    
		    updateNetAmtAfter();
		});

		this.otherCharges.textProperty().addListener((observable, oldValue, newValue) -> {		    
		    updateNetAmtAfter();
		});
	}

	private void prepareEditData(Sales sale)
	{
		if (sale != null)
		{
			if(sale.getSaleItems() != null)
			{
				List<SalesDet> addedBookList = sale.getSaleItems().stream().sorted(comparing(sd -> sd.getSlNum())).collect(toList());
				ObservableList<SalesDet> fxObs = FXCollections.observableArrayList(addedBookList);
				this.addedBooks.setItems(fxObs);

				Set<String> orderListIn = sale.getSaleItems().stream()
						.filter(sa -> sa.getOrderItem() != null)
						.map(sa -> sa.getOrderItem().getOrder())
						.filter(o -> o != null)
						.map(o -> o.getSerialNo())
						.collect(toSet());
				this.orderList.setItems(FXCollections.observableList(new ArrayList<>(orderListIn)));
			}
			this.billDate.setValue(sale.getInvoiceDate());
			this.subTotal.setText(getStringVal(sale.getSubTotal()));
			this.discAmt.setText(getStringVal(sale.getDiscAmt()));
			this.netAmt.setText(getStringVal(sale.getNetAmount()));
			this.despatchPer.setText(sale.getDespatch());
			this.docsThru.setText(sale.getDocsThru());
			this.grNum.setText(sale.getGrNum());
			this.invoiceNum.setText(getStringVal(sale.getSerialNo()));
			this.packageCnt.setText(sale.getPackages());
			this.otherCharges.setText(getStringVal(sale.getOtherAmount()));

			if (sale.getDiscType() == null || !sale.getDiscType())
			{
				this.rupeeRad.setSelected(true);
				this.discTypeInd.setText(RUPEE_SIGN);
			}
		}
		else
		{
			this.discAmt.setText(USUAL_DISC_TXT);
		}
	}

	private void loadNewBooksAndSubTotal(List<Order> orders)
	{
		if (addedBooks.getItems() == null)
		{
			addedBooks.setItems(FXCollections.observableList(new ArrayList<>()));
		}

		if (orders != null)
		{
			List<OrderItem> newItems = orders.stream()
					.map(Order::getOrderItem)
					.flatMap(List::stream)
					.sorted(comparing(OrderItem::getSerialNum))
					.collect(toList());
			newItems.removeAll(addedBooks.getItems().stream().map(sd -> sd.getOrderItem()).collect(toList()));

			int idx = addedBooks.getItems().size();
			for (OrderItem orderItem : newItems)
			{
				SalesDet salesDet = new SalesDet(null, null, ++idx,orderItem);
				addedBooks.getItems().add(salesDet);
			}
		}
		else
		{
			reArrangeItems(addedBooks.getItems());
		}
		loadSubTotal();
		calcNetAmount(discAmt.getText(), otherCharges.getText());
	}

	private void reArrangeItems(ObservableList<SalesDet> items)
	{
		int idx = 0;
		for (SalesDet orderItem : items)
		{
			orderItem.setSlNum(++idx);
		}
	}

	private void loadSubTotal()
	{
		double subTotalVal = 0;

		ObservableList<SalesDet> orderListIn = addedBooks.getItems();
		subTotalVal += orderListIn.stream().collect(summingDblCollector);
		subTotal.setText(getStringVal(subTotalVal));
	}

	@FXML
	private void updateNetAmtAfter()
	{
		String discAmtStr = defaultString(discAmt.getText());
		calcNetAmount(discAmtStr, otherCharges.getText());
	}
	
//	@FXML
//	private void updateNetAmt(KeyEvent ke)
//	{
//		String discAmtStr = StringUtils.defaultString(discAmt.getText());
//		discAmtStr += ke.getCharacter();
//		calcNetAmount(discAmtStr, otherCharges.getText());
//	}
//
//	@FXML
//	private void updateNetAmtOther(KeyEvent ke)
//	{
//		String otherAmtStr = StringUtils.defaultString(otherCharges.getText());
//		otherAmtStr += ke.getCharacter();
//		String discAmtStr = StringUtils.defaultString(discAmt.getText());
//		calcNetAmount(discAmtStr, otherAmtStr);
//	}
	
	@FXML
	void addOrderNum(ActionEvent e)
	{
		Order addedOrder = orderMap.get(orderNum.getText());
		if (addedOrder != null)
		{
			if(!orderList.getItems().contains(orderNum.getText()))
			{
				orderList.getItems().add(orderNum.getText());
			}
			orderNum.clear();

			List<Order> ordersIn = new ArrayList<Order>();
			ordersIn.add(addedOrder);
			loadNewBooksAndSubTotal(ordersIn);
		}
	}

	private void calcNetAmount(String discAmtStr, String otherAmtStr)
	{
		calcNetAmountGen(discAmtStr, this.subTotal, this.percentRad, this.rupeeRad, otherAmtStr, this.netAmt, this.calculatedDisc);
	}

	private boolean validateData()
	{
		boolean valid = true;
		StringBuilder errorMsg = new StringBuilder();
		if(isBlank(this.invoiceNum.getText()))
		{
			errorMsg.append("Please provide an Invoice Number");
			errorMsg.append(NEW_LINE);
			valid = false;
		}
		if (this.billDate.getValue() == null)
		{
			errorMsg.append("Please provide a Bill Date");
			valid = false;
		}

		if(this.addedBooks == null || this.addedBooks.getItems() == null || this.addedBooks.getItems().isEmpty())
		{
			errorMsg.append("Please add some records");
			valid = false;
		}
		else
		{
			valid = isFilled(this.addedBooks.getItems());
			if(!valid)
			{
				errorMsg.append("Please provide quantity and rate for all records");
			}
		}
		
		if(!valid)
		{
			showErrorAlert("Error in Saving Bill", "Please correct the following errors", errorMsg.toString());
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
			SalesTransaction salesTxn = null;
			Sales sale = this.selectedSale;
			if (selectedSale == null)
			{
				sale = new Sales();
				sale.setTxnDate(LocalDate.now());
				sale.setFinancialYear(calcFinYear(sale.getTxnDate()));
				sale.setSchool(school);
				salesTxn = new SalesTransaction();
				salesTxn.setSchool(school);
			}

			if(sale.getSalesTxn() == null)
			{
				salesTxn = new SalesTransaction();
				salesTxn.setSchool(school);
			}
			else
			{
				salesTxn = sale.getSalesTxn();
			}

			if (!isEmpty(discAmt.getText()))
			{
				Double discAmtVal = Double.parseDouble(discAmt.getText());
				sale.setDiscAmt(discAmtVal);
			}

			if (percentRad.isSelected())
			{
				sale.setDiscType(true);
			}
			else if (rupeeRad.isSelected())
			{
				sale.setDiscType(false);
			}

			List<SalesDet> orderItems = new ArrayList<>(this.addedBooks.getItems());

			if (!isEmpty(subTotal.getText()))
			{
				Double subTotalVal = Double.parseDouble(subTotal.getText());
				sale.setSubTotal(subTotalVal);
			}

			Double netAmtVal = getDoubleVal(netAmt);
			salesTxn.setAmount(netAmtVal);
			sale.setDeletedAmt(netAmtVal);

			salesTxn.setNote(SALES_NOTE);
			salesTxn.setTxnDate(billDate.getValue());

			sale.setDespatch(this.despatchPer.getText());
			sale.setDocsThru(this.docsThru.getText());
			sale.setGrNum(this.grNum.getText());
			sale.setSerialNo(getIntegerVal(this.invoiceNum));
			sale.setPackages(this.packageCnt.getText());
			sale.setOtherAmount(getDoubleVal(this.otherCharges));
			sale.setFinancialYear(calcFinYear(salesTxn.getTxnDate()));

			Sales saleOut = schoolService.saveSalesData(sale, orderItems, salesTxn);
			this.selectedSale = saleOut;
			this.dirty = false;

			((Stage) cancelBtn.getScene().getWindow()).close();
		}
		catch (DataIntegrityViolationException e)
		{
			LOGGER.error("Error...", e);
			showErrorAlert("Error in Saving Order", "Please correct the following errors", "Duplicate Entry Found");
		}
		catch (Exception e)
		{
			LOGGER.error("Error...", e);
			showErrorAlert("Error in Saving Order", "Please correct the following errors", e.getMessage());
		}
	}

	@FXML
	void cancelOperation(ActionEvent event)
	{
		this.selectedSale = null;
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void removeOperation(ActionEvent event)
	{
		int orderNumSel = orderList.getSelectionModel().getSelectedIndex();
		orderList.getItems().remove(orderNumSel);

		List<Order> leftOrders = orderList.getItems().stream().map(o -> orderMap.get(o)).collect(toList());
		List<OrderItem> newItems = leftOrders.stream()
				.map(Order::getOrderItem)
				.flatMap(List::stream)
				.sorted(comparing(OrderItem::getOrderId).thenComparing(OrderItem::getSerialNum))
				.collect(toList());

		int idx = addedBooks.getItems().size();
		for (OrderItem orderItem : newItems)
		{
			SalesDet salesDet = new SalesDet(null, null, ++idx,orderItem);
			addedBooks.getItems().add(salesDet);
		}
		loadNewBooksAndSubTotal(null);
	}

	@FXML
	void removeOrderItem(ActionEvent event)
	{
		int sels = addedBooks.getSelectionModel().getSelectedIndex();
		addedBooks.getItems().remove(sels);

		loadNewBooksAndSubTotal(null);
	}

	@FXML
	void moveUp(ActionEvent event)
	{
		moveUpPos(addedBooks);
		reArrangeItems(addedBooks.getItems());
	}

	@FXML
	void moveDown(ActionEvent event)
	{
		moveDownPos(addedBooks);
		reArrangeItems(addedBooks.getItems());
	}

	@FXML
	void addBookData(ActionEvent e)
	{
		Book book = bookMap.get(bookText.getText());
		if(book == null)
		{
			showErrorAlert("Invalid Book", "Invalid Book Entry", bookText.getText() + " does not correspond to a valid Book Entry");
			return;
		}
		int idx = addedBooks.getItems().size();
		Double bookPriceIn = getDoubleVal(bookPrice);
		SalesDet item = new SalesDet(++idx, getIntegerVal(bookCnt), bookPriceIn, book);
		if(bookPriceIn == null)
		{
			item.setRate(item.getBookPrice());
		}
		addedBooks.getItems().add(item);
		bookText.clear();
		bookCnt.clear();
		bookPrice.clear();
		addedBooks.getSelectionModel().clearSelection();
		bookText.requestFocus();
		loadSubTotal();
		calcNetAmount(discAmt.getText(), otherCharges.getText());
	}
	
	public Sales getSelectedSale()
	{
		return selectedSale;
	}

	public boolean isDirty()
	{
		return this.dirty;
	}

	public EventHandler<CellEditEvent<OrderItem, String>> fetchPriceEventHandler()
	{
		return new EventHandler<CellEditEvent<OrderItem, String>>() {
			public void handle(CellEditEvent<OrderItem, String> t)
			{
				OrderItem oItem = ((OrderItem) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				oItem.setBookPrice(Double.parseDouble(t.getNewValue()));
				t.getTableView().refresh();
				loadSubTotal();
				calcNetAmount(discAmt.getText(), otherCharges.getText());
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
		};
	}

	public EventHandler<CellEditEvent<SalesDet, String>> fetchSalesPrcEventHandler()
	{
		return new EventHandler<CellEditEvent<SalesDet, String>>() {
			public void handle(CellEditEvent<SalesDet, String> t)
			{
				SalesDet oItem = ((SalesDet) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				oItem.setRate(Double.parseDouble(t.getNewValue()));
				t.getTableView().refresh();
				loadSubTotal();
				calcNetAmount(discAmt.getText(), otherCharges.getText());
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
		};
	}

//	public EventHandler<CellEditEvent<OrderItem, String>> fetchQtyEventHandler()
//	{
//		return new EventHandler<CellEditEvent<OrderItem, String>>() {
//			public void handle(CellEditEvent<OrderItem, String> t)
//			{
//				OrderItem oItem = ((OrderItem) t.getTableView().getItems().get(t.getTablePosition().getRow()));
//				oItem.setSoldCnt(Integer.parseInt(t.getNewValue()));
//				t.getTableView().refresh();
//				loadSubTotal();
//				calcNetAmount(discAmt.getText(), otherCharges.getText());
//				t.getTableView().getSelectionModel().selectBelowCell();
//
//				int rowId = t.getTableView().getSelectionModel().getSelectedIndex();
//				if (rowId < t.getTableView().getItems().size())
//				{
//					if(rowId > 0)
//					{
//						t.getTableView().scrollTo(rowId - 1);
//					}
//					t.getTableView().edit(rowId, t.getTablePosition().getTableColumn());
//				}
//			}
//		};
//	}

	public EventHandler<CellEditEvent<SalesDet, String>> fetchSalesQtyEventHandler()
	{
		return new EventHandler<CellEditEvent<SalesDet, String>>() {
			public void handle(CellEditEvent<SalesDet, String> t)
			{
				SalesDet oItem = ((SalesDet) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				oItem.setQty(t.getNewValue() == null ? 0 : Integer.parseInt(t.getNewValue()));
				t.getTableView().refresh();
				loadSubTotal();
				calcNetAmount(discAmt.getText(), otherCharges.getText());
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
		};
	}

}

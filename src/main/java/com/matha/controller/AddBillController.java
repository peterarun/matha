package com.matha.controller;

import static com.matha.util.UtilConstants.DATE_CONV;
import static com.matha.util.UtilConstants.NEW_LINE;
import static com.matha.util.UtilConstants.RUPEE_SIGN;
import static com.matha.util.UtilConstants.SALES_NOTE;
import static com.matha.util.Utils.*;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.matha.domain.*;
import javafx.collections.ObservableSet;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	private TableColumn<SalesDet, String> priceColumn;

	@FXML
	private TableColumn<SalesDet, String> totalColumn;
	
	@FXML
	private TableColumn<SalesDet, String> qtyColumn;

	@FXML
	private TextField bookText;

	@FXML
	private TextField bookCnt;

	private School school;
	private Sales selectedSale;
//	private ObservableSet<Order> orders;
	private Map<String, Book> bookMap;
	private AtomicInteger index = new AtomicInteger();

	private Map<String, Order> orderMap = new HashMap<>();
	private Collector<Order, ?, Map<String, Order>> orderMapCollector = Collectors.toMap(o -> o.getSerialNo(), o -> o);
	private Collector<SalesDet, ?, Double> summingDblCollector = Collectors.summingDouble(SalesDet::getTotalSold);

	@Value("${listOfDespatchers}")
	private String[] despatcherArray;

	void initData(ObservableList<Order> ordersIn, School schoolIn, Sales sale, Map<String, Book> bookMapIn)
	{
		this.school = schoolIn;
		this.selectedSale = sale;
		this.bookMap = bookMapIn;
		this.prepareEditData(sale);
		this.loadAddDefaults();

		List<Order> allOrders = schoolService.fetchOrderForSchool(schoolIn);
		this.orderMap = allOrders.stream().collect(orderMapCollector);

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

		loadNewBooksAndSubTotal(ordersIn);
		loadDiscSymbol(percentRad, rupeeRad, discTypeInd);
	}

	private void loadAddDefaults()
	{
		this.billDate.setConverter(DATE_CONV);
		if(this.billDate.getValue() == null)
		{
			this.billDate.setValue(LocalDate.now());
		}
		this.schoolDetails.setText(this.school.fetchDetails());
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
		
		if(StringUtils.isBlank(this.invoiceNum.getText()))
		{
			Integer newInvNum = schoolService.fetchNextSalesInvoiceNum(calcFinYear(LocalDate.now()));
			LOGGER.info("newInvNum " + newInvNum);
			this.invoiceNum.setText(getStringVal(newInvNum));
		}
		
		List<String> items = new ArrayList<>(bookMap.keySet());
		TextFields.bindAutoCompletion(bookText, items);
		
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
				Set<SalesDet> addedBookList = sale.getSaleItems().stream().sorted(comparing(sd -> sd.getSlNum())).collect(toSet());
				this.addedBooks.setItems(FXCollections.observableArrayList(addedBookList));
				this.index.set(sale.getSaleItems().size());

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
					.collect(toList());
			newItems.removeAll(addedBooks.getItems().stream().map(sd -> sd.getOrderItem()).collect(toList()));

			List<SalesDet> bookItems = newItems.stream()
					.map(oi -> new SalesDet(null,
							null,
							index.incrementAndGet(),
							oi))
					.collect(toList());

			addedBooks.getItems().addAll(bookItems);
		}
		loadSubTotal();
		calcNetAmount(discAmt.getText(), otherCharges.getText());
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
		String discAmtStr = StringUtils.defaultString(discAmt.getText());		
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
		if(this.invoiceNum.getText() == null)
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

		if (!StringUtils.isEmpty(discAmt.getText()))
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

		Set<SalesDet> orderItems = new HashSet<>(this.addedBooks.getItems());

		if (!StringUtils.isEmpty(subTotal.getText()))
		{
			Double subTotalVal = Double.parseDouble(subTotal.getText());
			sale.setSubTotal(subTotalVal);
		}

		if (StringUtils.isEmpty(netAmt.getText()))
		{
			salesTxn.setAmount(0.0);
		}
		else
		{
			Double netAmtVal = Double.parseDouble(netAmt.getText());
			salesTxn.setAmount(netAmtVal);
		}
		
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

		((Stage) cancelBtn.getScene().getWindow()).close();
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
		String orderNumSel = orderList.getSelectionModel().getSelectedItem();
		if (orderNumSel != null)
		{
			orderList.getItems().remove(orderNumSel);
			Order removedOrder = orderMap.get(orderNumSel);

			addedBooks.getItems().removeAll(removedOrder.getOrderItem());
		}

		loadNewBooksAndSubTotal(null);
	}

	@FXML
	void removeOrderItem(ActionEvent event)
	{
		ObservableList<SalesDet> orderNumSel = addedBooks.getSelectionModel().getSelectedItems();
		if (orderNumSel != null)
		{
			addedBooks.getItems().removeAll(orderNumSel);
		}

		loadNewBooksAndSubTotal(null);
	}

	@FXML
	void addBookData(ActionEvent e)
	{
		SalesDet item = new SalesDet(index.incrementAndGet(), Integer.parseInt(bookCnt.getText()), null, bookMap.get(bookText.getText()));
		item.setRate(item.getBookPrice());
		addedBooks.getItems().add(item);
		bookText.clear();
		bookCnt.clear();
		addedBooks.getSelectionModel().clearSelection();
		bookText.requestFocus();
	}
	
	public Sales getSelectedSale()
	{
		return selectedSale;
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
				oItem.setQty(Integer.parseInt(t.getNewValue()));
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

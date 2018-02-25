package com.matha.controller;

import static com.matha.util.UtilConstants.DATE_CONV;
import static com.matha.util.UtilConstants.RUPEE_SIGN;
import static com.matha.util.Utils.*;
import static com.matha.util.Utils.fetchPriceColumnFactory;
import static com.matha.util.Utils.getIntegerVal;
import static com.matha.util.Utils.getStringVal;
import static com.matha.util.Utils.loadDiscSymbol;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.matha.domain.Order;
import com.matha.domain.OrderItem;
import com.matha.domain.Sales;
import com.matha.domain.SalesTransaction;
import com.matha.domain.School;
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
import javafx.scene.input.KeyEvent;
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
	private TableView<OrderItem> addedBooks;

	@FXML
	private TableColumn<OrderItem, String> priceColumn;

	@FXML
	private TableColumn<OrderItem, String> totalColumn;

	private School school;
	private Sales selectedSale;
	private ObservableList<Order> orders;

	private Map<String, Order> orderMap = new HashMap<>();
	private Collector<Order, ?, Map<String, Order>> orderMapCollector = Collectors.toMap(o -> o.getSerialNo(), o -> o);
	private Collector<OrderItem, ?, Double> summingDblCollector = Collectors.summingDouble(OrderItem::getTotalSold);

	@Value("${listOfDespatchers}")
	private String[] despatcherArray;

	void initData(ObservableList<Order> ordersIn, School schoolIn, Sales sale)
	{
		this.school = schoolIn;
		this.selectedSale = sale;
		this.orders = ordersIn;
		this.prepareEditData(sale);
		this.loadAddDefaults();

		List<Order> allOrders = schoolService.fetchOrderForSchool(schoolIn);
		this.orderMap = allOrders.stream().collect(orderMapCollector);

		List<String> items = new ArrayList<>(this.orderMap.keySet());
		TextFields.bindAutoCompletion(this.orderNum, items);
		
		LOGGER.debug("despatcherArray: " + despatcherArray);
		TextFields.bindAutoCompletion(this.despatchPer, Arrays.asList(despatcherArray));

		if (this.orders != null)
		{
			List<String> orderStrings = this.orders.stream().map(o -> o.getSerialNo()).collect(Collectors.toList());
			this.orderList.setItems(FXCollections.observableList(orderStrings));
		}
		
		// To avoid different objects of the same type getting loaded.
		for (Order order : this.orders)
		{
			orderMap.put(order.getSerialNo(), order);
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
		this.priceColumn.setCellValueFactory(fetchPriceColumnFactory());
		this.priceColumn.setOnEditCommit(new EventHandler<CellEditEvent<OrderItem, String>>() {
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
		});

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
			Bindings.format("%.2f", cellData.getValue().getTotal())
		);
		
		if(StringUtils.isBlank(this.invoiceNum.getText()))
		{
			Integer newInvNum = schoolService.fetchNextSalesInvoiceNum();
			LOGGER.info("newInvNum " + newInvNum);
			this.invoiceNum.setText(getStringVal(newInvNum));
		}
	}

	private void prepareEditData(Sales sale)
	{
		if (sale != null)
		{
			Set<Order> orderSet = sale.getOrderItems().stream().map(OrderItem::getOrder).collect(Collectors.toSet());
			this.orders = FXCollections.observableList(new ArrayList<Order>(orderSet));
			this.addedBooks.setItems(FXCollections.observableArrayList(sale.getOrderItems()));
			this.billDate.setValue(sale.getInvoiceDate());
			this.subTotal.setText(getStringVal(sale.getSubTotal()));
			this.discAmt.setText(getStringVal(sale.getDiscAmt()));
			this.netAmt.setText(getStringVal(sale.getSalesTxn().getAmount()));
			this.despatchPer.setText(sale.getDespatch());
			this.docsThru.setText(sale.getDocsThru());
			this.grNum.setText(sale.getGrNum());
			this.invoiceNum.setText(getStringVal(sale.getInvoiceNo()));
			this.packageCnt.setText(getStringVal(sale.getPackages()));
			this.otherCharges.setText(getStringVal(sale.getOtherAmount()));

			if (!sale.getDiscType())
			{
				this.rupeeRad.setSelected(true);
				this.discTypeInd.setText(RUPEE_SIGN);
			}
		}
	}

	private void loadNewBooksAndSubTotal(List<Order> orders)
	{
		if (orders != null)
		{
			List<OrderItem> bookItems = orders.stream()
					.map(Order::getOrderItem)
					.flatMap(List::stream)
					.filter(oi -> oi.getSale() == null)
					.collect(Collectors.toList());
			if (addedBooks.getItems() == null)
			{
				addedBooks.setItems(FXCollections.observableList(new ArrayList<>()));
			}
			addedBooks.getItems().addAll(bookItems);
			for (OrderItem orderItem : addedBooks.getItems())
			{
				orderItem.setSoldCnt(orderItem.getCount());
			}
		}
		loadSubTotal();
		calcNetAmount(discAmt.getText(), otherCharges.getText());
	}

	private void loadSubTotal()
	{
		double subTotalVal = 0;

		ObservableList<OrderItem> orderListIn = addedBooks.getItems();
		subTotalVal += orderListIn.stream().collect(summingDblCollector);
		subTotal.setText(getStringVal(subTotalVal));
	}

	@FXML
	private void updateNetAmt(KeyEvent ke)
	{
		String discAmtStr = StringUtils.defaultString(discAmt.getText());
		discAmtStr += ke.getCharacter();
		if (!StringUtils.isEmpty(discAmtStr))
		{
			calcNetAmount(discAmtStr, otherCharges.getText());
		}
	}

	@FXML
	private void updateNetAmtOther(KeyEvent ke)
	{
		String otherAmtStr = StringUtils.defaultString(otherCharges.getText());
		otherAmtStr += ke.getCharacter();
		String discAmtStr = StringUtils.defaultString(discAmt.getText());
		if (!StringUtils.isEmpty(otherAmtStr))
		{
			calcNetAmount(discAmtStr, otherAmtStr);
		}
	}
	
	@FXML
	void addOrderNum(ActionEvent e)
	{
		Order addedOrder = orderMap.get(orderNum.getText());
		if (addedOrder != null)
		{
			orderList.getItems().add(orderNum.getText());
			orderNum.clear();

			List<Order> ordersIn = new ArrayList<Order>();
			ordersIn.add(addedOrder);
			loadNewBooksAndSubTotal(ordersIn);
		}
	}

	private void calcNetAmount(String discAmtStr, String otherAmtStr)
	{
		calcNetAmountGen(discAmtStr, this.subTotal, this.percentRad, this.rupeeRad, otherAmtStr, this.netAmt);
	}

	@FXML
	void saveData(ActionEvent event)
	{
		SalesTransaction salesTxn = null;
		Sales sale = this.selectedSale;
		if (selectedSale == null)
		{
			sale = new Sales();
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

		Set<OrderItem> orderItems = new HashSet<>(this.addedBooks.getItems());

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

		sale.setDespatch(this.despatchPer.getText());
		sale.setDocsThru(this.docsThru.getText());
		sale.setGrNum(this.grNum.getText());
		sale.setInvoiceNo(getIntegerVal(this.invoiceNum));
		sale.setPackages(getIntegerVal(this.packageCnt));
		sale.setOtherAmount(getDoubleVal(this.otherCharges));

		String note = "Sales Bill Note";
		salesTxn.setNote(note);
		salesTxn.setTxnDate(billDate.getValue());

		schoolService.saveSales(sale, orderItems, salesTxn);

		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void cancelOperation(ActionEvent event)
	{
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
		ObservableList<OrderItem> orderNumSel = addedBooks.getSelectionModel().getSelectedItems();
		if (orderNumSel != null)
		{
			addedBooks.getItems().removeAll(orderNumSel);
		}

		loadNewBooksAndSubTotal(null);
	}

}

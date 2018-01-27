package com.matha.controller;

import static com.matha.util.UtilConstants.RUPEE_SIGN;
import static com.matha.util.Utils.fetchPriceColumnFactory;
import static com.matha.util.Utils.getStringVal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.Order;
import com.matha.domain.OrderItem;
import com.matha.domain.Publisher;
import com.matha.domain.Purchase;
import com.matha.domain.PurchaseTransaction;
import com.matha.service.SchoolService;

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
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

@Component
public class AddPurchaseBillController
{

	private static final Logger LOGGER = Logger.getLogger("AddPurchaseBillController");

	@Autowired
	private SchoolService schoolService;

	@FXML
	private TextField publisherDetails;

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
	private TableView<OrderItem> addedBooks;

	@FXML
	private TableColumn<OrderItem, String> priceColumn;

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
	private TextField totalCount;

	@FXML
	private TextField netAmt;

	@FXML
	private Button cancelBtn;

	private Publisher publisher;
	private Purchase purchase;

	private Map<String, Order> orderMap = new HashMap<>();
	private Collector<Order, ?, Map<String, Order>> orderMapCollector = Collectors.toMap(o -> o.getSerialNo(), o -> o);
	private Collector<OrderItem, ?, Double> summingDblCollector = Collectors.summingDouble(OrderItem::getTotal);

	private void loadAddDefaults()
	{
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
				calcNetAmount(discAmt.getText());
			}
		});
		this.publisherDetails.setText(this.publisher.getName());
	}

	private void prepareEditData(Purchase purchaseIn)
	{
		if (purchaseIn != null)
		{
			this.invoiceNum.setText(purchaseIn.getId());
			this.invoiceDate.setValue(purchaseIn.getSalesTxn().getTxnDate());
			this.despatchedTo.setText(purchaseIn.getDespatchedTo());
			this.docsThrough.setText(purchaseIn.getDocsThrough());
			this.despatchedPer.setText(purchaseIn.getDespatchPer());
			this.grNum.setText(purchaseIn.getGrNum());
			this.packageCount.setText(getStringVal(purchaseIn.getPackages()));
			this.subTotal.setText(getStringVal(purchaseIn.getSubTotal()));
			this.discAmt.setText(getStringVal(purchaseIn.getDiscAmt()));
			this.netAmt.setText(getStringVal(purchaseIn.getSalesTxn().getAmount()));

			if (purchaseIn.getOrderItems() != null && !purchaseIn.getOrderItems().isEmpty())
			{
				Set<Order> orderSet = purchaseIn.getOrderItems().stream().map(OrderItem::getOrder)
						.collect(Collectors.toSet());
				List<String> orderIdList = orderSet.stream().map(Order::getId).collect(Collectors.toList());
				this.orderList.setItems(FXCollections.observableList(orderIdList));

				int bookQty = 0;
				List<OrderItem> bookItems = new ArrayList<OrderItem>();

				if (purchaseIn.getOrderItems() != null)
				{
					bookItems.addAll(purchaseIn.getOrderItems());
					bookQty += purchaseIn.getOrderItems().stream().collect(Collectors.summingInt(OrderItem::getCount));
				}
				this.addedBooks.setItems(FXCollections.observableList(bookItems));
				this.totalCount.setText(getStringVal(bookQty));
			}
			if (!purchaseIn.getDiscType())
			{
				this.rupeeRad.setSelected(true);
				this.discTypeInd.setText(RUPEE_SIGN);
			}
		}
	}

	void initData(Set<Order> ordersIn, Publisher publisherIn, Purchase purchaseIn)
	{
		this.publisher = publisherIn;
		this.purchase = purchaseIn;
		this.prepareEditData(purchaseIn);
		this.loadAddDefaults();

		List<Order> allOrders = schoolService.fetchAllOrders(publisherIn);
		this.orderMap = allOrders.stream().collect(orderMapCollector);
		List<String> items = new ArrayList<>(orderMap.keySet());
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
		}

		discType.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle)
			{
				if (discType.getSelectedToggle() != null)
				{
					calcNetAmount(discAmt.getText());
				}
			}
		});

		LOGGER.info("Test");
	}

	private void loadBooksAndSubTotal(Set<Order> ordersIn)
	{
		List<OrderItem> bookItems = new ArrayList<>();
		for (Order orderIn : ordersIn)
		{
			List<OrderItem> orderItems = orderIn.getOrderItem();
			List<OrderItem> filteredItems = orderItems.stream()
					.filter(o -> o.getBook().getPublisher().getId().equals(this.publisher.getId()))
					.collect(Collectors.toList());
			bookItems.addAll(filteredItems);
		}

		ObservableList<OrderItem> bookList = FXCollections.observableArrayList();
		bookList.addAll(bookItems);

		addedBooks.setItems(bookList);
		loadSubTotal();
		calcNetAmount(discAmt.getText());
	}

	private void loadSubTotal()
	{
		double subTotalVal = 0;

		ObservableList<OrderItem> orderListIn = addedBooks.getItems();
		subTotalVal += orderListIn.stream().collect(summingDblCollector);
		subTotal.setText(String.valueOf(subTotalVal));
	}

	@FXML
	private void updateNetAmt(KeyEvent ke)
	{
		String discAmtStr = discAmt.getText();
		discAmtStr += ke.getCharacter();
		if (!StringUtils.isEmpty(discAmtStr))
		{
			calcNetAmount(discAmtStr);
		}
	}

	@FXML
	void addOrderNum(ActionEvent e)
	{
		orderList.getItems().add(orderNum.getText());
		orderNum.clear();

		HashSet<Order> ordersIn = new HashSet<Order>();
		ObservableList<String> orderListIn = orderList.getItems();
		for (String string : orderListIn)
		{
			ordersIn.add(orderMap.get(string));
		}
		loadBooksAndSubTotal(ordersIn);
	}

	private void calcNetAmount(String discAmtStr)
	{
		String netTotalStr = netAmt.getText();
		Double netTotalDbl = StringUtils.isEmpty(netTotalStr) ? 0.0 : Double.parseDouble(netTotalStr);

		String subTotalStr = this.subTotal.getText();
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
						netTotalDbl = subTotalDbl + discAmtDbl;
					}
					else if (percentRad.isSelected())
					{
						netTotalDbl = subTotalDbl + subTotalDbl * discAmtDbl / 100;
					}
				}
				else
				{
					netTotalDbl = subTotalDbl;
				}
				netAmt.setText(String.valueOf(netTotalDbl));
			}
		}
	}

	@FXML
	void saveData(ActionEvent event)
	{
		Purchase sale = this.purchase;
		if (sale == null)
		{
			sale = new Purchase();
			PurchaseTransaction salesTxn = new PurchaseTransaction();
			salesTxn.setPublisher(this.publisher);
			sale.setSalesTxn(salesTxn);
		}
		if (!StringUtils.isEmpty(discAmt.getText()))
		{
			Double discAmtVal = Double.parseDouble(discAmt.getText());
			sale.setDiscAmt(discAmtVal);
			if (percentRad.isSelected())
			{
				sale.setDiscType(true);
			}
			else if (rupeeRad.isSelected())
			{
				sale.setDiscType(false);
			}
		}

		if (!StringUtils.isEmpty(subTotal.getText()))
		{
			Double subTotalVal = Double.parseDouble(subTotal.getText());
			sale.setSubTotal(subTotalVal);
		}

		PurchaseTransaction txn = sale.getSalesTxn();
		if (StringUtils.isEmpty(netAmt.getText()))
		{
			txn.setAmount(0.0);
		}
		else
		{
			Double netAmtVal = Double.parseDouble(netAmt.getText());
			txn.setAmount(netAmtVal);
		}
	
		txn.setNote(this.note.getText());
		txn.setTxnDate(this.invoiceDate.getValue());

		schoolService.savePurchase(sale, this.addedBooks.getItems());

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
		}

		HashSet<Order> ordersIn = new HashSet<Order>();
		ObservableList<String> orderListIn = orderList.getItems();
		for (String string : orderListIn)
		{
			ordersIn.add(orderMap.get(string));
		}
		loadBooksAndSubTotal(ordersIn);
	}

}

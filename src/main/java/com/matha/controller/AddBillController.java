package com.matha.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.Order;
import com.matha.domain.OrderItem;
import com.matha.domain.Sales;
import com.matha.domain.SalesTransaction;
import com.matha.domain.School;
import com.matha.service.SchoolService;

import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

@Component
public class AddBillController
{

	private static final Logger LOGGER = Logger.getLogger("AddBillController");

	@Autowired
	private SchoolService schoolService;

	@FXML
	private RadioButton rupeeRad;

	@FXML
	private RadioButton percentRad;

	@FXML
	private TextField netAmt;

	@FXML
	private ToggleGroup discType;

	@FXML
	private Button cancelBtn;

	@FXML
	private TextField orderNum;

	@FXML
	private TextField subTotal;

	@FXML
	private Label schoolName;

	@FXML
	private DatePicker billDate;

	@FXML
	private TextField discAmt;

	@FXML
	private TableView<OrderItem> addedBooks;
	
	@FXML 
	private TableColumn<OrderItem, String> priceColumn;

	@FXML
	private ListView<String> orderList;

	private School school;

//	private Order order;

	private Map<String, Order> orderMap = new HashMap<>();
	private Collector<Order, ?, Map<String, Order>> orderMapCollector = Collectors.toMap(o -> o.getSerialNo(), o -> o);
	private Collector<OrderItem, ?, Double> summingDblCollector = Collectors.summingDouble(OrderItem::getTotal);

	void initData(ObservableList<Order> orders, School schoolIn)
	{
		addedBooks.getSelectionModel().cellSelectionEnabledProperty().set(true);
		priceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		priceColumn.setCellValueFactory(new Callback<CellDataFeatures<OrderItem, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<OrderItem, String> p) {
		         // p.getValue() returns the Person instance for a particular TableView row
		         return new ReadOnlyStringWrapper(p.getValue().getBookPrice().toString());
		     }
		  });
		 

		priceColumn.setOnEditCommit(
            new EventHandler<CellEditEvent<OrderItem, String>>() {
                public void handle(CellEditEvent<OrderItem, String> t) {
                    ((OrderItem) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setBookPrice(Double.parseDouble(t.getNewValue()));
                }
            }
        );
        
		
		this.school = schoolIn;
		List<Order> allOrders = schoolService.fetchOrderForSchool(schoolIn);

		orderMap = allOrders.stream().collect(orderMapCollector);

		List<String> items = new ArrayList<>(orderMap.keySet());
		TextFields.bindAutoCompletion(orderNum, items);

		List<String> orderStrings = orders.stream().map(o -> o.getSerialNo()).collect(Collectors.toList());
		orderList.setItems(FXCollections.observableList(orderStrings));
		loadBooksAndSubTotal(orders);
		calcNetAmount(discAmt.getText());
		LOGGER.info("Test");
	}
	
	private void loadBooksAndSubTotal(List<Order> ordersIn)
	{
		double subTotalVal = 0;
		List<OrderItem> bookItems = new ArrayList<>();
		for (Order orderIn : ordersIn)
		{
			List<OrderItem> orderItems = orderIn.getOrderItem();
			subTotalVal += orderItems.stream().collect(summingDblCollector);
			
			bookItems.addAll(orderItems.stream().collect(Collectors.toList()));
		}
		addedBooks.setItems(FXCollections.observableList(bookItems));
		subTotal.setText(String.valueOf(subTotalVal));
	}

	@FXML
	private void updateNetAmt(KeyEvent ke)
	{
		String discAmtStr = discAmt.getText();
		discAmtStr += ke.getCharacter();
		calcNetAmount(discAmtStr);
	}

	@FXML
	void addOrderNum(ActionEvent e)
	{

		orderList.getItems().add(orderNum.getText());
		orderNum.clear();
	}

	private void calcNetAmount(String discAmtStr)
	{
		Double netTotalDbl = Double.NaN;
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
						netTotalDbl -= discAmtDbl;
					}
					else if (percentRad.isSelected())
					{
						netTotalDbl = subTotalDbl - subTotalDbl * discAmtDbl / 100;
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

		Sales sale = new Sales();
		if (!StringUtils.isEmpty(discAmt.getText()))
		{
			Double discAmtVal = Double.parseDouble(discAmt.getText());
			sale.setDiscAmt(discAmtVal);
		}
				
		TreeSet<Order> orders = new TreeSet<>(); 
		ObservableList<String> selectedOrders = this.orderList.getItems();
		for (String orderId: selectedOrders)
		{
			if(this.orderMap.containsKey(orderId))
			{
				orders.add(this.orderMap.get(orderId));
			}
		}
		sale.setOrder(orders);
		
		if (!StringUtils.isEmpty(subTotal.getText()))
		{
			Double subTotalVal = Double.parseDouble(subTotal.getText());
			sale.setSubTotal(subTotalVal);
		}

		SalesTransaction txn = new SalesTransaction();
		if (StringUtils.isEmpty(netAmt.getText()))
		{
			txn.setAmount(0.0);
		}
		else 
		{
			Double netAmtVal = Double.parseDouble(netAmt.getText());
			txn.setAmount(netAmtVal);
		}
		String note = "Sales Bill Note";
		txn.setNote(note);		
		txn.setSchool(this.school);
		txn.setTxnDate(billDate.getValue());
		
		sale.setSalesTxn(txn);
		
		schoolService.saveSales(sale);
		
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
	}

}

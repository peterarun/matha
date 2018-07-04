package com.matha.controller;

import static com.matha.util.Converters.getIntegerConverter;
import static com.matha.util.Utils.calcFinYear;
import static com.matha.util.Utils.showConfirmation;
import static com.matha.util.Utils.showErrorAlert;
import static com.matha.util.UtilConstants.*;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.matha.domain.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import com.matha.service.SchoolService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

@Component
public class AddOrderController
{

	private static final Logger LOGGER = LogManager.getLogger("AddOrderController");

	private Map<String, Book> bookMap;
	private School school;

	@FXML
	private TextField orderNum;

	@FXML
	private TextField bookText;

	@FXML
	private TextField bookCount;

	@FXML
	private Label schoolName;

	@FXML
	private DatePicker orderDate;

	@FXML
	private DatePicker despatchDate;

	@FXML
	private TextField desLocation;

	@FXML
	private TableView<OrderItem> addedBooks;

	@FXML
	private TableColumn<OrderItem, Integer> slNumColumn;

	@FXML
	private TableColumn<OrderItem, Integer> qtyCol;

	@FXML
	private Button cancelBtn;

	@Autowired
	private SchoolService srvc;

	private Order order;

	public void initData(School school, Map<String, Book> bookMap, Order selectedOrder)
	{
		LOGGER.debug("Initializing Add Order");
		this.school = school;
		this.bookMap = bookMap;
		this.order = selectedOrder;
		updateFormData(selectedOrder);

		List<String> items = new ArrayList<>(bookMap.keySet());
		TextFields.bindAutoCompletion(bookText, items);

		this.addedBooks.getSelectionModel().cellSelectionEnabledProperty().set(true);
		this.qtyCol.setCellFactory(TextFieldTableCell.forTableColumn(getIntegerConverter()));
		this.qtyCol.setOnEditCommit(new EventHandler<CellEditEvent<OrderItem, Integer>>() {
			public void handle(CellEditEvent<OrderItem, Integer> t)
			{
				OrderItem oItem = ((OrderItem) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				oItem.setCount(t.getNewValue());
				
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
	}

	@FXML
	void addBookData(ActionEvent e)
	{
		int idx = addedBooks.getItems().size() + 1;
		OrderItem item = new OrderItem();
		item.setBook(bookMap.get(bookText.getText()));
		item.setCount(Integer.parseInt(bookCount.getText()));
		item.setSerialNum(idx);
		addedBooks.getItems().add(item);
		bookText.clear();
		bookCount.clear();
		bookText.requestFocus();
	}

	@FXML
	void removeEntry(ActionEvent e)
	{
		int sels = addedBooks.getSelectionModel().getSelectedIndex();
		addedBooks.getItems().remove(sels);
		reArrangeItems(addedBooks.getItems());
	}

	private void reArrangeItems(ObservableList<OrderItem> items)
	{
		int idx = 0;
		for (OrderItem orderItem : items)
		{
			orderItem.setSerialNum(++idx);
		}
	}

	@FXML
	void saveData(ActionEvent e)
	{

		if(!validateData())
		{
			return;
		}

		List<SalesDet> orderSales = new ArrayList<>();
		List<PurchaseDet> orderPurchases = new ArrayList<>();
		Set<SalesTransaction> saleTxns = new HashSet<>();
		Set<PurchaseTransaction> purTxns = new HashSet<>();

		Order item = this.order;
		if (item == null)
		{
			item = new Order();
		}

		List<OrderItem> origOrders = new ArrayList<>();
		if(item.getId() != null)
		{
			Order orderIn = srvc.fetchOrder(item.getId());
			origOrders = orderIn.getOrderItem();
		}

		if (origOrders != null && !origOrders.isEmpty())
		{
			origOrders.removeAll(this.addedBooks.getItems());
			if (!origOrders.isEmpty())
			{
//				orderSales = origOrders.stream().map(oi -> oi.getSalesDet()).flatMap(Set::stream).collect(toSet());
//				orderPurchases = origOrders.stream().map(oi -> oi.getPurchaseDet()).flatMap(Set::stream).collect(toSet());
				orderPurchases = srvc.fetchPurDetForOrderItems(origOrders);
				orderSales = srvc.fetchSalesDetForOrderItems(origOrders);
				if ((orderSales != null && !orderSales.isEmpty()) || (orderPurchases != null && !orderPurchases.isEmpty()))
				{
					if (!showConfirmation("Purchase/Sale Bills available for the order",
							"There are Purchase/Sale already created for the items getting removed. Are you sure you want to remove?",
							"Click Ok to Delete"))
					{
						return;
					}
				}
			}
		}

		List<OrderItem> items = addedBooks.getItems();
		item.setOrderItem(items);
		item.setSerialNo(orderNum.getText());
		item.setSchool(school);
		item.setOrderDate(orderDate.getValue());
		item.setDeliveryDate(despatchDate.getValue());
		item.setDesLocation(desLocation.getText());
		item.setFinancialYear(calcFinYear(LocalDate.now()));
		item.setPrefix(item.getSerialNo() + " - " + item.getFinancialYear());

		srvc.updateOrderData(item, items, origOrders);

		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void cancelOperation(ActionEvent e)
	{
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	public void updateFormData(Order selectedOrder)
	{
		if (selectedOrder == null)
		{
			return;
		}
		this.orderNum.setText(selectedOrder.getSerialNo());
		this.orderDate.setValue(selectedOrder.getOrderDate());
		this.desLocation.setText(selectedOrder.getDesLocation());
		this.despatchDate.setValue(selectedOrder.getDeliveryDate());
		List<OrderItem> items = selectedOrder.getOrderItem().stream().sorted(comparing(OrderItem::getSerialNum)).collect(toList());
		this.addedBooks.setItems(FXCollections.observableList(items));
	}

	private boolean validateData()
	{
		boolean valid = true;
		StringBuilder errorMsg = new StringBuilder();
		if (StringUtils.isBlank(this.orderNum.getText()))
		{
			errorMsg.append("Please provide an Order Number");
			errorMsg.append(NEW_LINE);
			valid = false;
		}
		if (this.orderDate.getValue() == null)
		{
			errorMsg.append("Please provide an Order Date");
			valid = false;
		}

		if(!valid)
		{
			showErrorAlert("Error in Saving Order", "Please correct the following errors", errorMsg.toString());
		}
		return valid;
	}
}

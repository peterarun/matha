package com.matha.controller;

import static com.matha.util.Converters.getIntegerConverter;
import static com.matha.util.Utils.showErrorAlert;
import static com.matha.util.UtilConstants.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.Book;
import com.matha.domain.Order;
import com.matha.domain.OrderItem;
import com.matha.domain.School;
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
		OrderItem item = new OrderItem();
		item.setBook(bookMap.get(bookText.getText()));
		item.setCount(Integer.parseInt(bookCount.getText()));
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
	}

	@FXML
	void saveData(ActionEvent e)
	{

		if(!validateData())
		{
			return;
		}
		Order item = this.order;
		List<OrderItem> items = addedBooks.getItems();
		if (item == null)
		{
			item = new Order();
		}

		item.setOrderItem(items);
		item.setSerialNo(orderNum.getText());
		item.setSchool(school);
		item.setOrderDate(orderDate.getValue());
		item.setDeliveryDate(despatchDate.getValue());
		item.setDesLocation(desLocation.getText());

		srvc.updateOrderData(item, items);

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
		this.addedBooks.setItems(FXCollections.observableList(selectedOrder.getOrderItem()));
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

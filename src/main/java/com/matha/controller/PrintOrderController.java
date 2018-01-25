package com.matha.controller;

import org.springframework.stereotype.Component;

import com.matha.domain.Order;
import com.matha.domain.OrderItem;
import com.matha.domain.Publisher;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

@Component
public class PrintOrderController {

	private String topLabel = "MATHA AGENCIES\r\n" + 
			"(Mayur Publications)\r\n" + 
			"Pareeckal Buildings, Vazhakulam P.O, Muvattupuzha - 686 670\r\n" + 
			"Ph: 0485 2260225 Mobile: 8281461689, 9495157528\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"SALES ORDER";
	
	@FXML
	private Label header;
	
    @FXML
    private TextField schoolPhone;

    @FXML
    private TextArea schoolAddress;

    @FXML
    private TextField schoolName;

    @FXML
    private TextField orderDate;

    @FXML
    private TableView<OrderItem> orderItems;

    @FXML
    private TextField serialNo;
    
    @FXML
    private TableColumn<OrderItem, Integer> slNo;

	public void initData(Order order, Publisher pub) {
		
		header.setText(topLabel);
		schoolName.setText(order.getSchoolName());
		schoolAddress.setText(order.getSchool().addressText());
		schoolPhone.setText(order.getSchool().getPhone1());
		serialNo.setText(order.getSerialNo());
		orderDate.setText(order.getOrderDate().toString());
		orderItems.setItems(FXCollections.observableList(order.getOrderItem()));
		
		
		slNo.setCellFactory(col -> {
		  TableCell<OrderItem, Integer> indexCell = new TableCell<>();
		  ReadOnlyObjectProperty<TableRow> rowProperty = indexCell.tableRowProperty();
		  ObjectBinding<String> rowBinding = Bindings.createObjectBinding(() -> {
		    TableRow<OrderItem> row = rowProperty.get();
		    if (row != null) { // can be null during CSS processing
		      int rowIndex = row.getIndex();
		      if (rowIndex < row.getTableView().getItems().size()) {
		        return Integer.toString(rowIndex);
		      }
		    }
		    return null;
		  }, rowProperty);
		  indexCell.textProperty().bind(rowBinding);
		  return indexCell;
		});
		
	}		
	
}


package com.matha.controller;

import static com.matha.util.UtilConstants.DATE_CONV;
import static com.matha.util.UtilConstants.RUPEE_SIGN;
import static com.matha.util.Utils.*;
import static com.matha.util.Utils.loadDiscSymbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.matha.domain.Order;
import com.matha.domain.OrderItem;
import com.matha.domain.Sales;
import com.matha.domain.School;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

@Component
public class ViewBillController
{

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
	private TableView<OrderItem> addedBooks;

	private School school;
	private ObservableList<Order> orders;

	@Value("${listOfDespatchers}")
	private String[] despatcherArray;

	void initData(School schoolIn, Sales sale)
	{
		this.school = schoolIn;
		this.prepareEditData(sale);
		this.loadAddDefaults();
	
		if (this.orders != null)
		{
			List<String> orderStrings = this.orders.stream().map(o -> o.getSerialNo()).collect(Collectors.toList());
			this.orderList.setItems(FXCollections.observableList(orderStrings));
		}

		loadDiscSymbol(percentRad, rupeeRad, discTypeInd);
	}

	private void loadAddDefaults()
	{
		this.billDate.setConverter(DATE_CONV);
		this.schoolDetails.setText(this.school.fetchDetails());
	}

	private void prepareEditData(Sales sale)
	{
		if (sale != null)
		{
			Set<Order> orderSet = sale.getOrderItems().stream().map(OrderItem::getOrder).filter(o -> o != null).collect(Collectors.toSet());
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
			this.packageCnt.setText(sale.getPackages());
			this.otherCharges.setText(getStringVal(sale.getOtherAmount()));

			if (!sale.getDiscType())
			{
				this.rupeeRad.setSelected(true);
				this.discTypeInd.setText(RUPEE_SIGN);
			}
			
//			double discAmt = calculateDisc(sale.getSubTotal(), sale.getDiscAmt(), !sale.getDiscType(), sale.getDiscType());
			calculatedDisc.setText(getStringVal(sale.getCalcDisc()));

		}
	}
}

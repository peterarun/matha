package com.matha.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matha.domain.OrderItem;
import com.matha.domain.Publisher;
import com.matha.domain.Purchase;
import com.matha.service.SchoolService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

@Component
public class CreatePurchaseController {

	private static final String[] discTypes = { "Rupees", "Percentage" };

	@Autowired
	private SchoolService srvc;

	@FXML
	private Button cancelBtn;

	@FXML
	private TextField purchaseNum;

	@FXML
	private TextField orderNumSt;

	@FXML
	private TextField grNum;

	@FXML
	private TextField subTotal;

	@FXML
	private TextField invoiceNum;

	@FXML
	private DatePicker deliveryDate;

	@FXML
	private TextArea notes;

	@FXML
	private DatePicker purchaseDate;

	@FXML
	private TableView<OrderItem> addedOrders;

	@FXML
	private ChoiceBox<String> discType;

	@FXML
	private TextField discQty;

	@FXML
	private TextField netAmount;

	private Publisher publisher;

	@FXML
	private TextField paidAmount;

	private Purchase purchase;

	public void init(Set<OrderItem> purchaseData, Publisher pub) {
		List<OrderItem> orderList = new ArrayList<>(purchaseData);
		addedOrders.setItems(FXCollections.observableList(orderList));
		this.publisher = pub;
		this.purchaseDate.setValue(LocalDate.now());
		this.discType.setItems(FXCollections.observableArrayList(discTypes));
		this.discType.getSelectionModel().selectFirst();
	}

	@FXML
	void removeEntry(ActionEvent event) {
		addedOrders.getItems().remove(this.addedOrders.getSelectionModel().getSelectedItem());
	}

	@FXML
	void addBookData(ActionEvent event) {

	}

	@FXML
	void saveData(ActionEvent event) {

		Purchase pur = this.purchase;
		if (pur == null) {
			pur = new Purchase();
		}
		pur.setPurchaseDate(this.purchaseDate.getValue());
//		pur.setGrNo(grNum.getText());
//		pur.setNotes(notes.getText());
//		pur.setPublisher(this.publisher);
//		pur.setDiscount(
//				StringUtils.isEmpty(this.discQty.getText()) ? null : Double.parseDouble(this.discQty.getText()));
//		pur.setDiscountType(this.discType.getSelectionModel().getSelectedItem());
//		pur.setDeliveryDate(this.deliveryDate.getValue());
//		pur.setInvoiceNo(this.invoiceNum.getText());
//		pur.setPaidAmount(
//				StringUtils.isEmpty(this.paidAmount.getText()) ? null : Double.parseDouble(this.paidAmount.getText()));
		pur.setSubTotal(
				StringUtils.isEmpty(this.subTotal.getText()) ? null : Double.parseDouble(this.subTotal.getText()));

		List<OrderItem> orderItems = this.addedOrders.getItems();
//		srvc.savePurchase(pur, orderItems);

		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void cancelOperation(ActionEvent event) {
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void updateNetAmount(ActionEvent e) {
		Double value = null;
		String subTotStr = this.subTotal.getText();
		if (subTotStr != null) {
			double subTotVal = Double.parseDouble(subTotStr);
			String discQtyStr = this.discQty.getText();
			String discType = this.discType.getSelectionModel().getSelectedItem();
			if (discQtyStr != null && !discQtyStr.isEmpty()) {
				double discQty = Double.parseDouble(discQtyStr);
				value = discTypes[0].equals(discType) ? subTotVal - discQty : subTotVal - subTotVal * discQty / 100;
			}
			netAmount.setText(String.valueOf(value));
		}
	}

	@FXML
	void updatedDiscType(ActionEvent e) {
		String discQtyStr = this.discQty.getText();
		String subTotStr = this.subTotal.getText();
		calculateNetAmount(subTotStr, discQtyStr);
	}

	@FXML
	void updatedDiscQty(KeyEvent e) {
		String discQtyStr = this.discQty.getText() + e.getCharacter();
		String subTotStr = this.subTotal.getText();
		calculateNetAmount(subTotStr, discQtyStr);
	}

	@FXML
	void updatedubTot(KeyEvent e) {
		String discQtyStr = this.discQty.getText();
		String subTotStr = this.subTotal.getText() + e.getCharacter();
		calculateNetAmount(subTotStr, discQtyStr);
	}

	private void calculateNetAmount(String subTotStr, String discQtyStr) {
		Double value = null;
		if (subTotStr != null && !subTotStr.isEmpty()) {
			double subTotVal = Double.parseDouble(subTotStr);
			String discType = this.discType.getSelectionModel().getSelectedItem();
			if (discQtyStr != null && !discQtyStr.isEmpty()) {
				double discQty = Double.parseDouble(discQtyStr);
				value = discTypes[0].equals(discType) ? subTotVal - discQty : subTotVal - subTotVal * discQty / 100;
			} else {
				value = subTotVal;
			}
			netAmount.setText(String.valueOf(value));
		}
	}

	public void initEdit(Purchase purchase) {

		this.purchase = purchase;
//		List<OrderItem> orderList = new ArrayList<>(purchase.getOrderItem());
//		addedOrders.setItems(FXCollections.observableList(orderList));
//		this.publisher = purchase.getPublisher();
//		this.purchaseDate.setValue(purchase.getPurchaseDate());
//		this.discType.setItems(FXCollections.observableArrayList(discTypes));
//		this.subTotal.setText(purchase.getSubTotal() == null ? "" : String.valueOf(purchase.getSubTotal()));
//		this.grNum.setText(purchase.getGrNo());
//		this.invoiceNum.setText(purchase.getInvoiceNo());
//		this.notes.setText(purchase.getNotes());
//		this.purchaseNum.setText(purchase.getId() == null ? "" : String.valueOf(purchase.getId()));
//		this.deliveryDate.setValue(purchase.getDeliveryDate());
//		this.discQty.setText(purchase.getDiscount() == null ? "" : String.valueOf(purchase.getDiscount()));
//		this.discType.getSelectionModel().select(purchase.getDiscountType());

	}
}

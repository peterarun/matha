package com.matha.controller;

import org.springframework.stereotype.Component;

import com.matha.domain.OrderItem;
import com.matha.domain.Publisher;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

@Component
public class PrintOrderController {

	@FXML
	private TextField bookLabel;

	@FXML
	private TextArea address;

	@FXML
	private TextField bookName;

	public void initData(OrderItem orderItem, Publisher pub) {
		
		bookName.setText(orderItem.getBookName());
		bookLabel.setText(orderItem.getOrderId());
		address.setText(pub.getAddress());
		
	}

}

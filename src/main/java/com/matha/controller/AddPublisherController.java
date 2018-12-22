package com.matha.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.matha.domain.Publisher;
import com.matha.service.SchoolService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddPublisherController {

	private static final Logger LOGGER = Logger.getLogger("AddPublisherController");

	private Publisher publisher;
	
    @FXML
    private TextField name;

    @FXML
    private TextField address1;

    @FXML
    private TextField address2;

    @FXML
    private TextField address3;

    @FXML
    private TextField phone1;

    @FXML
    private TextField phone2;

    @FXML
    private TextField pin;

    @FXML
    private TextField email;
    
    @FXML
    private TextField gstIn;    

    @FXML
    private Button cancelBtn;

	@Autowired
	private SchoolService srvc;
	
	@FXML
	void saveData(ActionEvent e) {

		if(this.publisher == null)
		{
			publisher = new Publisher();
		}
		
		publisher.setName(name.getText());
		publisher.setAddress1(address1.getText());
		publisher.setAddress2(address2.getText());
		publisher.setAddress3(address3.getText());
		publisher.setPhone1(phone1.getText());
		publisher.setPhone2(phone2.getText());
		publisher.setGstIn(gstIn.getText());
		publisher.setPin(pin.getText());
		publisher.setEmail(email.getText());
		srvc.savePublisher(publisher);

		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void cancelOperation(ActionEvent e) {
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	public void initData(Publisher pub)
	{
		 this.publisher = pub;
		 
		 name.setText(publisher.getName());
		 address1.setText(publisher.getAddress1());
		 address2.setText(publisher.getAddress2());
		 address3.setText(publisher.getAddress3());
		 phone1.setText(publisher.getPhone1());
		 phone2.setText(publisher.getPhone2());
		 gstIn.setText(publisher.getGstIn());
		 pin.setText(publisher.getPin());
		 email.setText(publisher.getEmail());		
	}

}

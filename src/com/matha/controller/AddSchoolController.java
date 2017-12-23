package com.matha.controller;

import com.matha.Main;
import com.matha.domain.School;
import com.matha.service.SchoolService;
import com.matha.util.SchoolConverter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class AddSchoolController {

//	@Autowired
	private SchoolService schoolService;

	@FXML
	private TextField name;

	@FXML
	private TextField address1;

	@FXML
	private TextField address2;

	@FXML
	private TextField city;

	@FXML
	private TextField pin;

	@FXML
	private ChoiceBox<String> district;

	@FXML
	private TextField phone;

	@FXML
	private TextField email;

	@FXML
	void handleSave(ActionEvent event) {

		schoolService = Main.getContext().getBean(SchoolService.class);
		School school = SchoolConverter.createSchoolObj(name, address1, address2, city, pin, district, phone, email);
		schoolService.insertSchool(school);
	}

}

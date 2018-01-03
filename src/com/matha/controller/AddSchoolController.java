package com.matha.controller;

import java.io.IOException;
import java.util.List;

import com.matha.Main;
import com.matha.domain.School;
import com.matha.domain.State;
import com.matha.service.SchoolService;
import com.matha.util.SchoolConverter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class AddSchoolController {

	// @Autowired
	// private SchoolService schoolService;

	private final String schoolFxmlFile = "../view/Schools.fxml";
	private final FXMLLoader schoolLoader = new FXMLLoader(getClass().getResource(schoolFxmlFile));
	Parent root;

	@FXML
	private TextField name;

	@FXML
	private TextField address1;

	@FXML
	private TextField address2;

	@FXML
	private TextField address3;

	@FXML
	private TextField city;

	@FXML
	private TextField pin;

	@FXML
	private ChoiceBox<State> states;

	@FXML
	private ChoiceBox<String> districts;

	@FXML
	private TextField phone1;

	@FXML
	private TextField phone2;

	@FXML
	private TextField principal;
	
	@FXML
	private TextField email;

	@FXML
	protected void initialize() {
		SchoolService srvc = Main.getContext().getBean(SchoolService.class);
		List<State> stateList = srvc.fetchAllStates();
		ObservableList<State> stateObsList = FXCollections.observableList(stateList);
		states.setItems(stateObsList);
	}

	@FXML
	void changedState(ActionEvent evt) {
		List<String> distList = states.getSelectionModel().getSelectedItem().getDistricts();
		districts.setItems(FXCollections.observableList(distList));
	}

	@FXML
	void handleSave(ActionEvent event) throws IOException {

		SchoolService schoolService = Main.getContext().getBean(SchoolService.class);
		School school = SchoolConverter.createSchoolObj(name, address1, address2, address3, city, pin, states, districts, phone1, phone2, principal, email);
		schoolService.insertSchool(school);

		root = schoolLoader.load();
		Main.loadScene(new Scene(root));
	}

	@FXML
	void handleCancel(ActionEvent event) throws IOException {

		root = schoolLoader.load();
		Main.loadScene(new Scene(root));
	}
}

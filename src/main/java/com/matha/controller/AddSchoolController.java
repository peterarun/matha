package com.matha.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.matha.domain.District;
import com.matha.domain.School;
import com.matha.domain.State;
import com.matha.service.SchoolService;
import com.matha.util.Converters;
import com.matha.util.SchoolConverter;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddSchoolController {

	@Autowired
	private SchoolService schoolService;

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
	private ChoiceBox<District> districts;

	@FXML
	private TextField phone1;

	@FXML
	private TextField phone2;

	@FXML
	private TextField principal;

	@FXML
	private TextField email;

	@FXML
	private Button cancelBtn;

	private School school;

	@FXML
	protected void initialize()
	{
		this.school = null;
		List<State> stateList = schoolService.fetchAllStates();
		states.setConverter(Converters.getStateConverter());
		states.setItems(FXCollections.observableList(stateList));
		
		districts.setConverter(Converters.getDistrictConverter());
	}

	@FXML
	void changedState(ActionEvent evt) {
		Set<District> districtSet = this.states.getSelectionModel().getSelectedItem().getDistricts();		
		List<District> distList = new ArrayList<>(districtSet);
		districts.setItems(FXCollections.observableList(distList));
	}

	@FXML
	void handleSave(ActionEvent event) throws IOException {

		School schoolIn = SchoolConverter.createSchoolObj(name, address1, address2, address3, city, pin, states,
				districts, phone1, phone2, principal, email);
		if (this.school != null) {
			schoolIn.setId(this.school.getId());
		}
		schoolService.saveSchool(schoolIn);
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	@FXML
	void handleCancel(ActionEvent event) throws IOException {
		((Stage) cancelBtn.getScene().getWindow()).close();
	}

	public void initEdit(School selectedItem) {
		this.school = selectedItem;
		this.name.setText(selectedItem.getName());
		this.address1.setText(selectedItem.getAddress1());
		this.address2.setText(selectedItem.getAddress2());
		this.address3.setText(selectedItem.getAddress3());
		this.city.setText(selectedItem.getCity());
		this.states.getSelectionModel().select(selectedItem.getState());
		if (selectedItem.getState() != null) {					
			this.districts.getSelectionModel().select(selectedItem.getDistrict());
		}
		this.phone1.setText(selectedItem.getPhone1());
		this.phone2.setText(selectedItem.getPhone2());
		this.principal.setText(selectedItem.getPrincipal());
		this.email.setText(selectedItem.getEmail());

	}
}

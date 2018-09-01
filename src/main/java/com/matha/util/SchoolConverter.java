package com.matha.util;

import com.matha.domain.Book;
import com.matha.domain.BookCategory;
import com.matha.domain.District;
import com.matha.domain.Publisher;
import com.matha.domain.School;
import com.matha.domain.State;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class SchoolConverter {

	public static School createSchoolObj(TextField name, TextField address1, TextField address2, TextField address3,
			TextField city, TextField pin, ChoiceBox<State> states, ChoiceBox<District> districts, TextField phone1,
			TextField phone2, TextField principal, TextField email) {
		School school = new School();
		school.setName(name.getText());
		school.setAddress1(address1.getText());
		school.setAddress2(address2.getText());
		school.setAddress3(address3.getText());
		school.setCity(city.getText());
		school.setPin(pin.getText());
		school.setDistrict(districts.getSelectionModel().getSelectedItem());
		school.setState(states.getSelectionModel().getSelectedItem());
		school.setPhone1(phone1.getText());
		school.setPhone2(phone2.getText());
		school.setPrincipal(principal.getText());
		school.setEmail(email.getSelectedText());

		return school;
	}
}


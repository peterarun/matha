package com.matha.util;

import com.matha.domain.School;
import com.matha.domain.State;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class SchoolConverter {

	public static School createSchoolObj(TextField name, TextField address1, TextField address2, TextField address3,
			TextField city, TextField pin, ChoiceBox<State> state, ChoiceBox<String> district, TextField phone1,
			TextField phone2, TextField principal, TextField email) {
		School school = new School(null, name.getText(), address1.getText(), address2.getText(), address3.getText(),
				city.getText(), pin.getText(), state.getValue().getName(), district.getValue(), phone1.getText(),
				phone2.getText(), principal.getText(), email.getText());
		return school;
	}

}

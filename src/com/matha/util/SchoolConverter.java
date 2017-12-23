package com.matha.util;

import com.matha.domain.School;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class SchoolConverter {

	public static School createSchoolObj(TextField name, TextField address1, TextField address2, TextField city, TextField pin,
			ChoiceBox<String> district, TextField phone, TextField email) {
		School school = new School(null, name.getText(), address1.getText(), address2.getText(), city.getText(),
				district.getValue(), pin.getText(), phone.getText(), email.getText());
		return school;
	}

}

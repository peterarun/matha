package com.matha.util;

import javafx.scene.control.TextField;

public class Utils {

	public static Double getDoubleVal(TextField txt) {
		Double dblVal = null;
		String txtStr = txt.getText();
		if (txtStr != null) {
			dblVal = Double.parseDouble(txtStr);
		}
		return dblVal;
	}
}

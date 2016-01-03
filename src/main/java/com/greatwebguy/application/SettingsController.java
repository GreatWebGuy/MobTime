package com.greatwebguy.application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputControl;

public class SettingsController implements Initializable {
	@FXML // fx:id="timeSlider"
	private Slider timeSlider;

	@FXML // fx:id="timeInput"
	private TextInputControl timeInput;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert timeSlider != null : "fx:id=\"timeSlider\" was not injected: check your FXML file 'application.fxml'.";
		assert timeInput != null : "fx:id=\"timeInput\" was not injected: check your FXML file 'application.fxml'.";

		timeInput.setText(String.valueOf(Settings.instance().getStartTime()));
		timeSlider.setValue(Settings.instance().getStartTime());

		timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				int value = Math.round(newValue.intValue());
				timeInput.setText(value + "");
				Settings.instance().setStartTime(value);
			}
		});

	}

}

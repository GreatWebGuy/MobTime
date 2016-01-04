package com.greatwebguy.application;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class SettingsController implements Initializable {
	@FXML // fx:id="timeSlider"
	private Slider timeSlider;

	@FXML // fx:id="timeInput"
	private TextField timeInput;

	@FXML // fx:id="userInput"
	private TextField userInput;

	@FXML // fx:id="addUser"
	private Button addUser;

	@FXML // fx:id="userList"
	private ListView<People> userList;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert timeSlider != null : "fx:id=\"timeSlider\" was not injected: check your FXML file 'application.fxml'.";
		assert timeInput != null : "fx:id=\"timeInput\" was not injected: check your FXML file 'application.fxml'.";

		timeInput.setText(String.valueOf(Settings.instance().getStartTime()));
		timeSlider.setValue(Settings.instance().getStartTime());
		timeSlider.requestFocus();
		timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				int value = Math.round(newValue.intValue());
				timeInput.setText(value + "");
				Settings.instance().setStartTime(value);
			}
		});
		
		userList.setItems(Settings.instance().users);
		
		addUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(StringUtils.isNotBlank(userInput.getText())) {
            		Settings.instance().users.add(new People(userInput.getText()));
            	}
            }
		});
	}

}

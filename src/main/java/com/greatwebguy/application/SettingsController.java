package com.greatwebguy.application;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsController implements Initializable {
	@FXML // fx:id="settingsModal"
	private VBox settingsModal;
	
	@FXML // fx:id="timeSlider"
	private Slider timeSlider;

	@FXML // fx:id="timeInput"
	private Label timeSettings;

	@FXML // fx:id="userInput"
	private TextField userInput;

	@FXML // fx:id="addUser"
	private Button addUser;

	@FXML // fx:id="removeUser"
	private Button removeUser;

	@FXML // fx:id="nextUser"
	private Button nextUser;

	@FXML // fx:id="userList"
	private ListView<People> userList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		assert settingsModal != null : "fx:id=\"settingsModal\" was not injected: check your FXML file 'application.fxml'.";
		assert timeSlider != null : "fx:id=\"timeSlider\" was not injected: check your FXML file 'application.fxml'.";
		assert timeSettings != null : "fx:id=\"timeInput\" was not injected: check your FXML file 'application.fxml'.";
		assert addUser != null : "fx:id=\"addUser\" was not injected: check your FXML file 'application.fxml'.";
		assert removeUser != null : "fx:id=\"removeUser\" was not injected: check your FXML file 'application.fxml'.";
		assert nextUser != null : "fx:id=\"nextUser\" was not injected: check your FXML file 'application.fxml'.";

		timeSettings.setText(Settings.instance().getStartTime() + "");
		timeSlider.setValue(Settings.instance().getStartTime());
		Platform.runLater(new Runnable() {
		     @Override
		     public void run() {
		    	 userInput.requestFocus();
		     }
		});
		
		
		timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				int value = Math.round(newValue.intValue());
				timeSettings.setText(value + "");
				Settings.instance().setStartTime(value);
			}
		});

		userList.setItems(Settings.instance().users);

		userInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					addUser();
				}
			}
		});
		
		settingsModal.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE) {
					closeWindow();
				}
				
			}

			
		});

		addUser.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addUser();
			}
		});

		removeUser.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int selectedIdx = userList.getSelectionModel().getSelectedIndex();
				if (selectedIdx != -1) {
					userList.getItems().remove(selectedIdx);
					if(Settings.instance().users.size() == 0) {
						Settings.instance().setCurrentUser(-1);
					} else if(Settings.instance().getCurrentUser() == selectedIdx) {
						Settings.instance().incrementCurrentUser();
					}
					
				}
			}
		});

		nextUser.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int selectedIdx = userList.getSelectionModel().getSelectedIndex();
				if (selectedIdx != -1) {
					Settings.instance().setCurrentUser(selectedIdx);
				}
				closeWindow();
			}
		});

	}

	private void addUser() {
		if (StringUtils.isNotBlank(userInput.getText())) {
			ObservableList<People> users = Settings.instance().users;
			if(users.size() == 0) {
				Settings.instance().setCurrentUser(0);
			}
			users.add(new People(userInput.getText()));
			userInput.clear();
			Settings.instance().displayUserMessage();
		}
	}

	private void closeWindow() {
		Stage window = (Stage) settingsModal.getScene().getWindow();
		window.close();
	}
}

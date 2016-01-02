package com.greatwebguy.application;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TimeController implements Initializable {
	private Timeline timeline;
	private int STARTTIME = 5;
	private LocalTime time = LocalTime.of(0, STARTTIME, 0);
	private IntegerProperty timeInSeconds = new SimpleIntegerProperty(STARTTIME * 60);
	private StringProperty timeMinutes = new SimpleStringProperty("Start");
    @FXML //  fx:id="startButton"
    private Button startButton; // Value injected by FXMLLoader
    
    @FXML
    private Button stopButton;
    
    @FXML
    private Button settingsButton;
    
    @FXML // fx:id="timerLabel"
    private Label timerLabel;
    

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'application.fxml'.";
        assert stopButton != null : "fx:id=\"stopButton\" was not injected: check your FXML file 'application.fxml'.";
        assert settingsButton != null : "fx:id=\"settingsButton\" was not injected: check your FXML file 'application.fxml'.";
        assert timerLabel != null : "fx:id=\"timerLabel\" was not injected: check your FXML file 'application.fxml'.";
        // initialize your logic here: all @FXML variables will have been injected
        timerLabel.textProperty().bind(timeMinutes);
        	
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(timeline == null || timeline.getStatus().equals(Status.STOPPED)) {
					timeMinutes.set(time.format(DateTimeFormatter.ofPattern("mm:ss")));
					timeline = new Timeline();
					timeline.setCycleCount(timeInSeconds.get() + 1);
					timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
						// KeyFrame event handler
						public void handle(ActionEvent event) {
							time = time.minusSeconds(1);
							timeMinutes.set(time.format(DateTimeFormatter.ofPattern("mm:ss")));
							if ("00:00".equals(time.format(DateTimeFormatter.ofPattern("mm:ss")))) {
								timeline.stop();
							}
						}
					}));
					timeline.playFromStart();
            	} else {
            		switch (timeline.getStatus()) {
					case RUNNING:
						timeline.pause();
						break;
					case PAUSED:
						timeline.play();
						break;
					default:
						break;
					}
            	}
            }
        });
        
        //Stop and reset the timer
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(timeline != null) {
            		timeline.stop();
            	}
            	timeMinutes.set("Start");
            	time = LocalTime.of(0, STARTTIME, 0);
            	timeInSeconds = new SimpleIntegerProperty(STARTTIME * 60);
            }
        });
    }
}

package com.greatwebguy.application;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TimeController implements Initializable {
	private Timeline timeline;
	
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
        
        timerLabel.textProperty().bind(timeMinutes);
        	
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(timeline == null || timeline.getStatus().equals(Status.STOPPED)) {
            		resetStartState();
					timeMinutes.set(Settings.instance().getTime().format(DateTimeFormatter.ofPattern("mm:ss")));
					timeline = new Timeline();
					timeline.setCycleCount(Settings.instance().getTimeInSeconds());
					timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
						// KeyFrame event handler
						public void handle(ActionEvent event) {
							Settings.instance().setTime(Settings.instance().getTime().minusSeconds(1));
							timeMinutes.set(Settings.instance().getTime().format(DateTimeFormatter.ofPattern("mm:ss")));
							if ("00:00".equals(Settings.instance().getTime().format(DateTimeFormatter.ofPattern("mm:ss")))) {
								showRotate();
							}
						}

					}));
					timeline.playFromStart();
					startButton.setStyle("-fx-background-color:#71B284");
					hideWindow();
					
            	} else {
            		switch (timeline.getStatus()) {
					case RUNNING:
						timeline.pause();
						break;
					case PAUSED:
						timeline.play();
						hideWindow();
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
            	resetStartState();
            }
        });
    }
    
    private void showRotate() {
    	if(timeline != null) {
    		timeline.stop();
    	}
    	timeMinutes.set("Rotate");
    	startButton.setStyle("-fx-background-color:#FF0000");
    	URL resource = getClass().getResource("door-bell.wav");
    	Media doorBell = new Media(resource.toString());
    	MediaPlayer mediaPlayer = new MediaPlayer(doorBell);
    	mediaPlayer.play();
    	Stage window = (Stage)timerLabel.getScene().getWindow();
        window.toFront();
        window.requestFocus();
    	
    }

    private void resetStartState() {
    	if(timeline != null) {
    		timeline.stop();
    	}
    	timeMinutes.set("Start");
    	Settings.instance().initializeTime();
    	startButton.setStyle("-fx-background-color:#333333");
    }
    
	private void hideWindow() {
		Timeline hide = new Timeline(1, new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				Stage window = (Stage) timerLabel.getScene().getWindow();
				window.toBack();
			}
		}));
		hide.playFromStart();
	}
}

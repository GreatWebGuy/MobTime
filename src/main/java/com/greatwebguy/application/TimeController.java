package com.greatwebguy.application;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

public class TimeController implements Initializable {
	private Timeline timeline;
	private Timeline nag;

	public static StringProperty timeMinutes = new SimpleStringProperty("Start");
	public static StringProperty paneColor = new SimpleStringProperty("-fx-background-color:#333333");

	@FXML // fx:id="startButton"
	private Button startButton; // Value injected by FXMLLoader

	@FXML
	private Button stopButton;

	@FXML
	private Button pauseButton;

	@FXML
	private Button skipButton;

	@FXML
	private Button settingsButton;

	@FXML // fx:id="muteButton"
	private Button muteButton;

	@FXML // fx:id="timerLabel"
	private Label timerLabel;

	@FXML // fx:id="turnLabel"
	private Label turnLabel;

	@FXML // fx:id="turnLabel"
	private Label nextTurnLabel;

	@FXML
	private AnchorPane bottomPane;

	@Override // This method is called by the FXMLLoader when initialization is
	// complete
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'application.fxml'.";
		assert stopButton != null : "fx:id=\"stopButton\" was not injected: check your FXML file 'application.fxml'.";
		assert pauseButton != null : "fx:id=\"pauseButton\" was not injected: check your FXML file 'application.fxml'.";
		assert skipButton != null : "fx:id=\"skipButton\" was not injected: check your FXML file 'application.fxml'.";
		assert settingsButton != null
				: "fx:id=\"settingsButton\" was not injected: check your FXML file 'application.fxml'.";
		assert muteButton != null : "fx:id=\"muteButton\" was not injected: check your FXML file 'application.fxml'.";
		assert timerLabel != null : "fx:id=\"timerLabel\" was not injected: check your FXML file 'application.fxml'.";
		assert turnLabel != null : "fx:id=\"turnLabel\" was not injected: check your FXML file 'application.fxml'.";
		assert nextTurnLabel != null : "fx:id=\"nextTurnLabel\" was not injected: check your FXML file 'application.fxml'.";
		assert bottomPane != null : "fx:id=\"bottomPane\" was not injected: check your FXML file 'application.fxml'.";
		Settings.instance().loadUsers();
		Settings.instance().loadTime();
		Settings.instance().loadMuteStatus();

		timerLabel.textProperty().bind(timeMinutes);
		turnLabel.textProperty().bind(Settings.instance().userMessage);
		nextTurnLabel.textProperty().bind(Settings.instance().nextUserMessage);
		bottomPane.styleProperty().bind(paneColor);
		Settings.instance().updateUserDisplay();

		startButton.setOnAction(event -> startButtonPressed());
		stopButton.setOnAction(event -> resetTimer());
		pauseButton.setOnAction(event -> pauseButtonPressed());
		skipButton.setOnAction(event -> skipButtonPressed());
		settingsButton.setOnAction(event -> settingsButtonPressed(((Node) event.getSource()).getScene().getWindow()));
		muteButton.setOnAction(event -> muteButtonPressed());
	}

	private void settingsButtonPressed(Window owner) {
		try {
			Stage stage = new Stage();
			Parent root = FXMLLoader.load(SettingsController.class.getResource("settings-modal.fxml"));
			stage.setScene(new Scene(root));
			stage.setTitle("Settings");
			stage.setResizable(false);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(owner);
			stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					Settings.instance().storeUsers();
					Settings.instance().storeTime();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void skipButtonPressed() {
		Settings.instance().incrementCurrentUser();
		resetTimer();
	}

	private void pauseButtonPressed() {
		if ((timeline == null || timeline.getStatus().equals(Status.STOPPED))
				&& (nag == null || nag.getStatus().equals(Status.STOPPED))) {
			startTimer();
		} else {
			switch (timeline.getStatus()) {
				case PAUSED:
					continueTimer();
					break;
				default:
					pauseTimer();
					break;
			}
		}
	}

	private void startButtonPressed() {
		if (timeline == null || timeline.getStatus().equals(Status.STOPPED)) {
			startTimer();
		} else {
			switch (timeline.getStatus()) {
				case PAUSED:
					continueTimer();
					break;
				default:
					break;
			}
		}
	}

	private void muteButtonPressed() {
		if (Settings.instance().isMuted()) {
			Settings.instance().setMuted(false);
			FontIcon icon = new FontIcon("fas-bell-slash");
			icon.setIconColor(Color.WHITE);
			muteButton.setGraphic(icon);
			muteButton.setText("Mute");
			Settings.instance().storeMuteStatus();
		} else {
			Settings.instance().setMuted(true);
			FontIcon icon = new FontIcon("fas-bell");
			icon.setIconColor(Color.WHITE);
			muteButton.setGraphic(icon);
			muteButton.setText("Unmute");
			Settings.instance().storeMuteStatus();
		}
	}

	private void showRotate() {
		URL doorBellWav = getClass().getResource("door-bell.wav");
		AudioClip doorBellSound = new AudioClip(doorBellWav.toString());
		if (!Settings.instance().isMuted()) {
			doorBellSound.play();
		}
		if (timeline != null) {
			timeline.stop();
		}
		Settings.instance().incrementCurrentUser();
		timeMinutes.set("Rotate");
		paneColor.set("-fx-background-color:#FF0000");
		showMainWindow();
		nag = new Timeline();
		nag.setCycleCount(Timeline.INDEFINITE);
		nag.getKeyFrames().add(new KeyFrame(Duration.seconds(15), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (timeMinutes.getValue().equals("Rotate")) {
					showMainWindow();
					if (!Settings.instance().isMuted()) {
						doorBellSound.play();
					}
				} else {
					nag.stop();
				}
			}
		}));
		nag.playFromStart();

	}

	private void resetTimer() {
		if (timeline != null) {
			timeline.stop();
		}
		FontIcon icon = new FontIcon("fas-play");
		icon.setIconColor(Color.WHITE);
		pauseButton.setGraphic(icon);
		timeMinutes.set("Start");
		Settings.instance().initializeTime();
		paneColor.set("-fx-background-color:#333333");

	}

	private void showMainWindow() {
		Stage window = (Stage) timerLabel.getScene().getWindow();
		window.toFront();
		window.requestFocus();
	}

	private void continueTimer() {
		timeline.play();
		paneColor.set("-fx-background-color:#71B284");
		FontIcon icon = new FontIcon("fas-pause");
		icon.setIconColor(Color.WHITE);
		pauseButton.setGraphic(icon);
		hideWindow();
	}

	private void hideWindow() {
		Timeline hide = new Timeline(1, new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (timeline != null && timeline.getStatus().equals(Status.RUNNING)) {
					Stage window = (Stage) timerLabel.getScene().getWindow();
					window.toBack();
				}
			}
		}));
		hide.playFromStart();
	}

	private void startTimer() {
		resetTimer();
		timeMinutes.set(Settings.instance().getTime().format(DateTimeFormatter.ofPattern("mm:ss")));
		timeline = new Timeline();
		timeline.setCycleCount(Settings.instance().getTimeInSeconds());
		// KeyFrame event handler
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> handleTimeDecrement()));
		timeline.playFromStart();
		Settings.instance().updateUserDisplay();
		Settings.instance().runUserScript();
		paneColor.set("-fx-background-color:#71B284");
		FontIcon icon = new FontIcon("fas-pause");
		icon.setIconColor(Color.WHITE);
		pauseButton.setGraphic(icon);
		hideWindow();
	}

	private void handleTimeDecrement() {
		Settings.instance().setTime(Settings.instance().getTime().minusSeconds(1));
		timeMinutes.set(Settings.instance().getTime().format(DateTimeFormatter.ofPattern("mm:ss")));
		if ("00:00".equals(Settings.instance().getTime().format(DateTimeFormatter.ofPattern("mm:ss")))) {
			showRotate();
		}
	}

	private void pauseTimer() {
		boolean paused = false;
		if (timeline != null && timeline.getStatus().equals(Status.RUNNING)) {
			timeline.pause();
			paused = true;
		}
		if (nag != null && nag.getStatus().equals(Status.RUNNING)) {
			nag.stop();
			paused = true;
		}
		if (paused) {
			paneColor.set("-fx-background-color:#FFBF00");
			FontIcon icon = new FontIcon("fas-play");
			icon.setIconColor(Color.WHITE);
			pauseButton.setGraphic(icon);
		}
	}

}

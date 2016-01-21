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
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class TimeController implements Initializable {
	private Timeline timeline;
	private Timeline nag;

	private StringProperty timeMinutes = new SimpleStringProperty("Start");
	private StringProperty paneColor = new SimpleStringProperty("-fx-background-color:#333333");
	private Stage miniTimer;

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

	@FXML // fx:id="timerLabel"
	private Label timerLabel;

	@FXML // fx:id="turnLabel"
	private Label turnLabel;

	@FXML
	private AnchorPane bottomPane;

	@Override // This method is called by the FXMLLoader when initialization is
				// complete
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'application.fxml'.";
		assert stopButton != null : "fx:id=\"stopButton\" was not injected: check your FXML file 'application.fxml'.";
		assert pauseButton != null : "fx:id=\"pauseButton\" was not injected: check your FXML file 'application.fxml'.";
		assert skipButton != null : "fx:id=\"skipButton\" was not injected: check your FXML file 'application.fxml'.";
		assert settingsButton != null : "fx:id=\"settingsButton\" was not injected: check your FXML file 'application.fxml'.";
		assert timerLabel != null : "fx:id=\"timerLabel\" was not injected: check your FXML file 'application.fxml'.";
		assert turnLabel != null : "fx:id=\"turnLabel\" was not injected: check your FXML file 'application.fxml'.";
		assert bottomPane != null : "fx:id=\"bottomPane\" was not injected: check your FXML file 'application.fxml'.";

		timerLabel.textProperty().bind(timeMinutes);
		turnLabel.textProperty().bind(Settings.instance().userMessage);
		bottomPane.styleProperty().bind(paneColor);
		Settings.instance().displayUserMessage();

		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (timeline == null || timeline.getStatus().equals(Status.STOPPED)) {
					resetStartState();
					timeMinutes.set(Settings.instance().getTime().format(DateTimeFormatter.ofPattern("mm:ss")));
					timeline = new Timeline();
					timeline.setCycleCount(Settings.instance().getTimeInSeconds());
					timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
						// KeyFrame event handler
						public void handle(ActionEvent event) {
							Settings.instance().setTime(Settings.instance().getTime().minusSeconds(1));
							timeMinutes.set(Settings.instance().getTime().format(DateTimeFormatter.ofPattern("mm:ss")));
							if ("00:00".equals(
									Settings.instance().getTime().format(DateTimeFormatter.ofPattern("mm:ss")))) {
								showRotate();
							}
						}

					}));
					timeline.playFromStart();
					Settings.instance().displayUserMessage();
					paneColor.set("-fx-background-color:#71B284");
					hideWindow();
					openMiniTimer();
				} else {
					switch (timeline.getStatus()) {
					case PAUSED:
						timeline.play();
						paneColor.set("-fx-background-color:#71B284");
						hideWindow();
						break;
					default:
						break;
					}
				}
			}

		});

		// Stop and reset the timer
		stopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				resetStartState();
			}
		});

		pauseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
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
				}
			}
		});

		skipButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Settings.instance().incrementCurrentUser();
				resetStartState();
			}
		});

		settingsButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					Stage stage = new Stage();
					Parent root = FXMLLoader.load(SettingsController.class.getResource("settings-modal.fxml"));
					stage.setScene(new Scene(root));
					stage.setTitle("Settings");
					stage.initModality(Modality.WINDOW_MODAL);
					stage.initOwner(((Node) event.getSource()).getScene().getWindow());
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void showRotate() {
		URL doorBellWav = getClass().getResource("door-bell.wav");
		AudioClip doorBellSound = new AudioClip(doorBellWav.toString());
		doorBellSound.play();
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
					doorBellSound.play();
				} else {
					nag.stop();
				}
			}
		}));
		nag.playFromStart();

	}

	private void resetStartState() {
		if (timeline != null) {
			timeline.stop();
		}
		timeMinutes.set("Start");
		Settings.instance().initializeTime();
		paneColor.set("-fx-background-color:#333333");

	}

	private void showMainWindow() {
		Stage window = (Stage) timerLabel.getScene().getWindow();
		window.toFront();
		window.requestFocus();
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

	private void openMiniTimer() {
		if (miniTimer == null) {
			int height = 35;
			int width = 50;
			Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
			miniTimer = new Stage();
			miniTimer.initStyle(StageStyle.TRANSPARENT);
			miniTimer.setX(screenBounds.getMinX() + screenBounds.getWidth() - width);
			miniTimer.setY(screenBounds.getMinY() + screenBounds.getHeight() - height);
			Label turn = new Label();
			turn.setPrefWidth(width);
			turn.setPrefHeight(height - 25);
			turn.setTextAlignment(TextAlignment.CENTER);
			turn.setAlignment(Pos.CENTER);
			turn.setStyle("-fx-background-color: #000000; -fx-text-fill: white; -fx-font-size: 10px;");
			turn.textProperty().bind(Settings.instance().userName);
			Label timer = new Label();
			timer.setPrefWidth(width);
			timer.setPrefHeight(height - 10);
			timer.textProperty().bind(timeMinutes);
			timer.setTextAlignment(TextAlignment.CENTER);
			timer.setAlignment(Pos.CENTER);
			timer.setTextFill(Paint.valueOf("white"));
			timer.styleProperty().bind(paneColor);
			VBox box = new VBox();
			box.setAlignment(Pos.CENTER);
			box.getChildren().add(turn);
			box.getChildren().add(timer);
			box.setCenterShape(true);
			final Scene scene = new Scene(box, width, height);
			scene.setFill(Color.TRANSPARENT);
			miniTimer.setScene(scene);
			miniTimer.setAlwaysOnTop(true);
			miniTimer.show();

			box.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					showMainWindow();
				}
			});
		}
	}
}

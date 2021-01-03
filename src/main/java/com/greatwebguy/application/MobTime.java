package com.greatwebguy.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileLock;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class MobTime extends Application {
	private Stage miniTimer;
	private Stage mainStage;
	
	@Override
	public void start(Stage stage) {	
		try {
			mainStage = stage;
			Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
			stage.setTitle("MobTime");
			stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			openMiniTimer();
			
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		        @Override
		        public void handle(final WindowEvent event) {
		        	miniTimer.close();
		        }
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	private void openMiniTimer() {
		if (miniTimer == null) {
			int height = 40;
			int width = 50;
			Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
			miniTimer = new Stage();
			miniTimer.initStyle(StageStyle.TRANSPARENT);
			miniTimer.setX(screenBounds.getMinX() + screenBounds.getWidth() - width);
			miniTimer.setY(screenBounds.getMinY() + screenBounds.getHeight() + 2 - height);
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
			timer.textProperty().bind(TimeController.timeMinutes);
			timer.setTextAlignment(TextAlignment.CENTER);
			timer.setAlignment(Pos.CENTER);
			timer.setTextFill(Paint.valueOf("white"));
			timer.styleProperty().bind(TimeController.paneColor);
			Label nextTurn = new Label();
			nextTurn.setPrefWidth(width);
			nextTurn.setPrefHeight(5);
			nextTurn.setTextAlignment(TextAlignment.CENTER);
			nextTurn.setAlignment(Pos.CENTER);
			nextTurn.setStyle("-fx-background-color: #000000; -fx-text-fill: white; -fx-font-size: 9px;");
			nextTurn.textProperty().bind(Settings.instance().nextUserMessage);
			VBox box = new VBox();
			box.setAlignment(Pos.CENTER);
			box.getChildren().add(turn);
			box.getChildren().add(timer);
			box.getChildren().add(nextTurn);
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
	
	public void showMainWindow() {
		Stage window = (Stage) mainStage.getScene().getWindow();
		window.toFront();
		window.requestFocus();
	}

	private static boolean lockInstance() {
		try {
			String homeDir = System.getProperty("user.home");
			String lockFile = homeDir + "/.mobtime-lock";
			final File file = new File(lockFile);
			final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
			final FileLock fileLock = randomAccessFile.getChannel().tryLock();
			if (fileLock != null) {
				Runtime.getRuntime().addShutdownHook(new Thread() {
					public void run() {
						try {
							fileLock.release();
							randomAccessFile.close();
							file.delete();
						} catch (Exception e) {
							System.out.println("Unable to remove lock file: " + lockFile + e);
						}
					}
					});
				return true;
			}
		} catch (Exception e) {
			System.out.println("Unable to create lock file" + e);
		}
		return false;
	}
	
}

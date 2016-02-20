package com.greatwebguy.application;

import java.io.InputStream;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MobTime extends Application {

	@Override
	public void start(Stage stage) {
		try {
			InputStream is = getClass().getResourceAsStream("fontawesome-webfont.ttf");
			GlyphFont fa = new FontAwesome(is);
			GlyphFontRegistry.register(fa);
			Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
			stage.setTitle("MobTime");
			stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

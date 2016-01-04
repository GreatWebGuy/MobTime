package com.greatwebguy.application;

import java.time.LocalTime;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Settings {
	private static Settings settings = new Settings();
	private int DEFAULT_START = 7;
	private int startTime = DEFAULT_START;
	private LocalTime time;
	protected ObservableList<People> users = FXCollections.observableArrayList();
	
	
	private Settings() {
		//
	}
	
	public static Settings instance() {
		return settings;
	}
	
	public void initializeTime() {
		time = LocalTime.of(0, startTime, 0);
	}
	
	public void setStartTime(int minutes) {
		startTime = minutes;
	}
	
	public int getStartTime() {
		return startTime;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}
	
	public int getTimeInSeconds() {
		return new SimpleIntegerProperty(startTime * 60).get() + 1;
	}
	
}

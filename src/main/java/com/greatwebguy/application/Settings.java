package com.greatwebguy.application;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;

public class Settings {
	private static Settings settings = new Settings();
	private int DEFAULT_START = 7;
	private int startTime = DEFAULT_START;
	private LocalTime time;
	private List<People> people = new ArrayList<People>();
	
	
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

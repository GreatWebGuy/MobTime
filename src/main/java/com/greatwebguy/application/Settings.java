package com.greatwebguy.application;

import java.time.LocalTime;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Settings {
	private static Settings settings = new Settings();
	private int DEFAULT_START = 7;
	private int startTime = DEFAULT_START;
	private LocalTime time;
	private int currentUser = 0;
	
	protected ObservableList<People> users = FXCollections.observableArrayList();
	protected StringProperty userMessage = new SimpleStringProperty("");
	
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
	
	public void setCurrentUser(int index) {
		currentUser = index;
		displayUserMessage();
	}
	
	public int getCurrentUser() {
		return users.size() > 0 && currentUser <= users.size()?currentUser:-1;
	}
	
	public void incrementCurrentUser() {
		int potentialUser = currentUser+1;
		currentUser = potentialUser >= users.size()?0:potentialUser;
		displayUserMessage();
	}
	
    public void displayUserMessage() {
    	int index = getCurrentUser();
    	if(index > -1) {
    		String name = users.get(index).getName();
    		userMessage = new SimpleStringProperty("Mobber " + name +"'s Turn");
    	} else {
    		userMessage = new SimpleStringProperty("");
    	}
	}
	
}

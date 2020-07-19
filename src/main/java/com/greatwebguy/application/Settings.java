package com.greatwebguy.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.stream.Collectors;


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
	protected StringProperty nextUserMessage = new SimpleStringProperty("");
	protected StringProperty userName = new SimpleStringProperty("MobTime");
	
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
		updateUserDisplay();
	}
	
	public int getCurrentUser() {
		return users.size() > 0 ? currentUser:-1;
	}
	
	public int getNextUser() {
		int potentialUser = getCurrentUser()+1;
		if(potentialUser == 0) {
			return -1;
		}
		return potentialUser >= users.size()?0:potentialUser;
	}
	
	public void incrementCurrentUser() {
		int nextUser = getNextUser();
		if(nextUser > -1) {
			currentUser = nextUser;
		}
		updateUserDisplay();
	}

	public void updateUserDisplay() {
		displayUserMessage();
		displayNextUserMessage();
	}

	private People getUser(int index) {
		if (users.isEmpty()) {
			return null;
		}
		return users.get((index) % users.size());
	}

	private void displayNextUserMessage() {
		People user = getUser(getCurrentUser() + 1);
		if (user == null) {
			nextUserMessage.set("");
			return;
		}
		if (isBreak(user)) {
			user = getUser(getCurrentUser() + 2);
		}
		nextUserMessage.set(">> " + user.getName());
	}

	private boolean isBreak(People user) {
		return "break".equalsIgnoreCase(user.getName());
	}

	private void displayUserMessage() {
		People user = getUser(getCurrentUser());
		if (user == null) {
			userMessage.set("");
			userName.set("MobTime");
			return;
		}
		if (isBreak(user)) {
			userMessage.set("Break");
			userName.set("Break");
		} else {
			String name = user.getName();
			userMessage.set(name +"'s Turn");
			userName.set(name);
		}
	}
    
	public void storeUsers() {
		String path = getUserStoragePath();
		try (PrintWriter out = new PrintWriter(path)) {
			out.println(Settings.instance().users.stream().map(People::getName).collect(Collectors.joining(",")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void loadUsers() {
		String path = getUserStoragePath();
		try (BufferedReader brTest = new BufferedReader(new FileReader(path))) {
			String users = brTest.readLine();
			String[] people = users.split(",");
			for (String person : people) {
				if (!person.isBlank()) {
					Settings.instance().users.add(new People(person));
				}
			}
		} catch (Exception e) {
			System.out.println("No existing users found");
		}
	}
	
	public void storeTime() {
		String path = getTimeStoragePath();
		try (PrintWriter out = new PrintWriter(path)) {
			out.println(Settings.instance().getStartTime());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	public void loadTime() {
		String path = getTimeStoragePath();
		try (BufferedReader brTime = new BufferedReader(new FileReader(path))) {
			String time = brTime.readLine();
			if(!time.isBlank()) {
				Settings.instance().setStartTime(Integer.parseInt(time));
			}
		} catch (Exception e) {
			System.out.println("No existing time");
		}		
	}

	private String getUserStoragePath() {
		String home = System.getProperty("user.home");
		String path = home + File.separator + ".mobtime";
		return path;
	}
	
	private String getTimeStoragePath() {
		String home = System.getProperty("user.home");
		String path = home + File.separator + ".mobtime-time";
		return path;
	}
	
}

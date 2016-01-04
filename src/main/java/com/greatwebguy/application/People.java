package com.greatwebguy.application;

public class People {
	private String name;
	private boolean enabled = true;
	
	public People(String name) {
		this.name = name;
	}
	
	public void setEnabled(boolean value) {
		this.enabled = value;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public String toString() {
		return getName();
	}
}

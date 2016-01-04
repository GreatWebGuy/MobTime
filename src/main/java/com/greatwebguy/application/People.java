package com.greatwebguy.application;

public class People {
	private String name;
	
	public People(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
}

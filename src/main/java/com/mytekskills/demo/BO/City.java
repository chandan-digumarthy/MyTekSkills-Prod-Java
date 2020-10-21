package com.mytekskills.demo.BO;

public class City {
	
	public City(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public City(String name) {
		this.name = name;
	}
	
	public City() {
	}

	private String name;
	private int value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "City [name=" + name + ", value=" + value + "]";
	}
	
}

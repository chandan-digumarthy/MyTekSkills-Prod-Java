package com.mytekskills.demo.BO;

import java.util.List;

public class State {
	
	public State(String name, List<City> children) {
		this.name = name;
		this.children = children;
	}
	
	public State(String name) {
		this.name = name;
	}
	
	public State() {

	}

	
	private String name;
	private String type;
	private List<City> children;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<City> getChildren() {
		return children;
	}
	public void setChildren(List<City> children) {
		this.children = children;
	}
	
	public String getType() {
		this.type = "State";
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "State [name=" + name + ", children=" + children + "]";
	}
	
}

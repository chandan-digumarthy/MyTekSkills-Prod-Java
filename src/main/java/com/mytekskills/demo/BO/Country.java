package com.mytekskills.demo.BO;

import java.util.List;

public class Country {

	public Country(String name, List<State> children) {
		this.name = name;
		this.children = children;
	}
	
	public Country(String name) {
		this.name = name;
	}
	
	public Country() {

	}
	
	private String name;
	private List<State> children;
	private String type;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<State> getChildren() {
		return children;
	}
	public void setChildren(List<State> children) {
		this.children = children;
	}
	
	public String getType() {
		this.type = "Country";
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Country [name=" + name + ", children=" + children + "]";
	}
	
}

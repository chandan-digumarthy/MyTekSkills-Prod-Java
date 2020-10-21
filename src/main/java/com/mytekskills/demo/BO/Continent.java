package com.mytekskills.demo.BO;

import java.util.List;

public class Continent {
	
	public Continent(String name, List<Country> children) {
		this.name = name;
		this.children = children;
	}
	
	public Continent(String name) {
		this.name = name;
	}
	
	public Continent() {

	}
	
	private String name;
	private List<Country> children;
	private String type;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
		
	public List<Country> getChildren() {
		return children;
	}
	public void setChildren(List<Country> children) {
		this.children = children;
	}

	public String getType() {
		this.type = "Continent";
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Continent [name=" + name + ", children=" + children + "]";
	}
	
	
}

package com.mytekskills.demo.BO;

import java.util.List;

public class World {

	private String name;
	private List<Continent> children;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Continent> getChildren() {
		return children;
	}
	public void setChildren(List<Continent> children) {
		this.children = children;
	}
	
	@Override
	public String toString() {
		return "World [name=" + name + ", children=" + children + "]";
	}
	
}

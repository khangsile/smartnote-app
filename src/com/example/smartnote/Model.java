package com.example.smartnote;

public class Model {
	
	private String name;
	private boolean selected;
	private long count;
	
	public Model(String name, long count) {
		this.name = name;
		this.count = count;
		selected = false;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setCount(long count) {
		this.count = count;
	}
	
	public long getCount() {
		return count;
	}

}

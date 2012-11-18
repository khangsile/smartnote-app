package com.example.smartnote;

public class CardModel {
	
	private String title;
	private String definition;
	
	public CardModel(String title, String definition) {
		this.title = title;
		this.definition = definition;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDef() {
		return definition;
	}

}

package com.example.smartnote;

public class CardModel {
	
	private String title;
	private String definition;
	private int id, stack;
	
	public CardModel(String title, String definition, int id, int stack) {
		this.title = title;
		this.definition = definition;
		this.id = id;
		this.stack = stack;
	}
	
	public CardModel(CardModel card) {
		this.title = card.getTitle();
		this.definition = card.getDef();
		this.id = card.getId();
		this.stack = card.getStack();
	}
	
	public void copy (CardModel card) {
		this.title = card.getTitle();
		this.definition = card.getDef();
		this.id = card.getId();
		this.stack = card.getStack();
	}

	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDef() {
		return definition;
	}
	
	public void setDef(String definition) {
		this.definition = definition;
	}
	
	public int getId() {
		return id;
	}
	
	public int getStack() {
		return stack;
	}

}

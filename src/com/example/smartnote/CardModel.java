package com.example.smartnote;

public class CardModel {
	
	private String title;
	private String definition;
	private int id, stack, hits, attempts;
	private boolean selected;
	
	public CardModel(String title, String definition, int id, int stack, int hits, int attempts) {
		this.title = title;
		this.definition = definition;
		this.id = id;
		this.stack = stack;
		this.hits = hits;
		this.attempts = attempts;
		this.selected = false;
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
	
	public int getHits() {
		return hits;
	}
	
	public int getAttempts() {
		return attempts;
	}
	
	public String getDifficulty() {
		if (attempts > 0) {
			int difficulty = 11-(10*hits)/attempts;
			return difficulty+"";
		} else {
			return "N/A";
		}
	}
	
	public void correct() {
		hits++;
		attempts++;
	}
	
	public void wrong() {
		attempts++;
	}

	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}

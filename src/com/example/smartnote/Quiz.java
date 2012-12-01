package com.example.smartnote;

public class Quiz {
	
	private int correct, attempts;
	
	public Quiz(int correct, int attempts) {
		this.correct = correct;
		this.attempts = attempts;
	}
	
	public int getCorrect() {
		return correct;
	}
	
	public int getAtts() {
		return attempts;
	}

}

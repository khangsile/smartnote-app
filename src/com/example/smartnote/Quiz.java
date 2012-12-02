package com.example.smartnote;

public class Quiz {
	
	private int correct, attempts, grade;
	
	public Quiz(int correct, int attempts) {
		this.correct = correct;
		this.attempts = attempts;
		
		grade = (correct*100) / (attempts);
	}
	
	public int getCorrect() {
		return correct;
	}
	
	public int getAtts() {
		return attempts;
	}
	
	public int getGrade() {
		return grade;
	}

}

package com.example.smartnote;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;

public class MemHistory extends ListActivity {
	
	private String stack;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		Bundle extras = getIntent().getExtras();
		stack = extras.getString("stack");
		setTitle(stack+ " Quizzes");
		
		setListAdapter(new QuizArrayAdapter(this, getQuizzes()));	
	}
	
	public List<Quiz> getQuizzes() {
		
		List<Quiz> list = new ArrayList<Quiz>();
		
		SmartDBAdapter db = new SmartDBAdapter(this);
		db.open();
		
		try {
			list = db.getMemQuiz(stack);
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.close();
		
		return list;
	}

}


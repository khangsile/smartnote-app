package com.example.smartnote;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityGroup;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class QuizHistory extends ActivityGroup {
	
	private String stack;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quizhistorytab);
		
		Bundle extras = getIntent().getExtras();
		stack = extras.getString("stack");
		setTitle(stack+ " Quiz History");
		
		TabHost th = (TabHost) findViewById(R.id.quizHistoryTabhost);
		th.setup(getLocalActivityManager());
		Intent intent;
		
		TabSpec tabSpec = th.newTabSpec("mcQuiz");
		intent = new Intent().setClass(this, McHistory.class);
		intent.putExtra("stack", stack);
		tabSpec.setIndicator("Multiple Choice").setContent(intent);
		th.addTab(tabSpec);
		
		tabSpec = th.newTabSpec("memQuiz");
		intent = new Intent().setClass(this, MemHistory.class);
		intent.putExtra("stack", stack);
		tabSpec.setIndicator("Memory").setContent(intent);
		th.addTab(tabSpec);
	}
	
	
}

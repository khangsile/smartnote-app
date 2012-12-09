package com.example.smartnote;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class SmartSearchView extends ActivityGroup {
	
	private String query;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quizhistorytab);
		
		Bundle extras = getIntent().getExtras();
		query = extras.getString("query");
		setTitle("Search Results");
		
		TabHost th = (TabHost) findViewById(R.id.quizHistoryTabhost);
		th.setup(getLocalActivityManager());
		Intent intent;
		
		TabSpec tabSpec = th.newTabSpec("cardRes");
		intent = new Intent().setClass(this, CardSearch.class);
		intent.putExtra("query", query);
		tabSpec.setIndicator("Card Results").setContent(intent);
		th.addTab(tabSpec);
		
		tabSpec = th.newTabSpec("stackRes");
		intent = new Intent().setClass(this, StackSearch.class);
		intent.putExtra("query", query);
		tabSpec.setIndicator("Stack Results").setContent(intent);
		th.addTab(tabSpec);
	}
	
	
}

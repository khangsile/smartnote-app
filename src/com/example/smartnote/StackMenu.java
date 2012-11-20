package com.example.smartnote;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class StackMenu extends ListActivity {
	
	SmartDBAdapter db;
	
	private String stack, definition, title;
	
	InteractiveArrayAdapter adapter;
	
	ListView listview;
		
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stackmenu);
		
		Bundle extras = getIntent().getExtras();
		
		stack = extras.getString("stack");
		definition = extras.getString("definition");
		title = extras.getString("title");
				
		Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
		
		TextView header = (TextView) findViewById(R.id.stackheader);
		header.setTypeface(chinacat);
				
		adapter = new InteractiveArrayAdapter(this, getMenuItems());
		
		setListAdapter(adapter);
		listview = getListView();
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

						
	}
	
	private List<Model> getMenuItems() {
		
		db = new SmartDBAdapter(this);
		db.open();
		
		List<Model> list = new ArrayList<Model>();
		list = db.getStacks();
		
		db.close();
		return list;
	}
	
	public void cancel(View view) {
		Intent intent = new Intent(this, CardCreator.class);
		intent.putExtra("title", title);
		intent.putExtra("definition", definition);
		intent.putExtra("stack", stack);
		
		startActivity(intent);
	}
	
	public void addStacks(View view) {
	
		List<Model> models = adapter.list;
				
		for (int i=0; i < models.size(); i++) {
			if (models.get(i).isSelected()==true)
				stack = stack + ";" + models.get(i).getName();
		}
		
		Intent intent = new Intent(this, CardCreator.class);
		intent.putExtra("title", title);
		intent.putExtra("definition", definition);
		intent.putExtra("stack", stack);
		
		startActivity(intent);
		
	}
}

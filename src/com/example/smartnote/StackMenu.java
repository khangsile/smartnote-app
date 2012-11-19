package com.example.smartnote;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class StackMenu extends ListActivity {
	
	SmartDBAdapter db;
	
	private static final String STACK_NAME = "stackName";
	private String stack, definition, title;

	private int stackNameIndex;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stackmenu);
		
		Bundle extras = getIntent().getExtras();
		
		stack = extras.getString("stack");
		definition = extras.getString("definition");
		title = extras.getString("title");
		
		db = new SmartDBAdapter(this);
		db.open();
		
		Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
		
		TextView header = (TextView) findViewById(R.id.stackheader);
		header.setTypeface(chinacat);
				
		ArrayAdapter<Model> adapter = new InteractiveArrayAdapter(getMenuItems(), this);
							
		setListAdapter(adapter);
				
		db.close();
		
	}
	
	private List<Model> getMenuItems() {
		Cursor cursor = db.getStacks();
		stackNameIndex = cursor.getColumnIndex(STACK_NAME);
		cursor.moveToFirst();
		
		List<Model> list = new ArrayList<Model>();
		
		while(!cursor.isAfterLast()) {
			String name = cursor.getString(stackNameIndex);
			list.add(get(name));
			
			cursor.moveToNext();
		}
		list.get(0).setSelected(true);
		return list;
	}
	
	private Model get(String s) {
		Model model = new Model(s);
		
		return model;
	}
	
	public void cancel(View view) {
		Intent intent = new Intent(this, CardCreator.class);
		intent.putExtra("title", title);
		intent.putExtra("definition", definition);
		intent.putExtra("stack", stack);
		
		startActivity(intent);
	}
}

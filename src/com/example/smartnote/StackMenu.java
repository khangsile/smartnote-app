package com.example.smartnote;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class StackMenu extends ListActivity {
	
	SmartDBAdapter db;
	
	private static final String STACK_NAME = "stackName";
	
	
	private int stackNameIndex;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db = new SmartDBAdapter(this);
		db.open();
				
		ArrayAdapter<Model> adapter = new InteractiveArrayAdapter(getMenuItems(), this);
		
		setListAdapter(adapter);
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
		
		return list;
	}
	
	private Model get(String s) {
		Model model = new Model(s);
		
		return model;
	}
}

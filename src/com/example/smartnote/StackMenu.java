package com.example.smartnote;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class StackMenu extends ListActivity {
	
	String menuItems[];
	SmartDBAdapter db;
	
	private static final String STACK_NAME = "stackName";
	
	private int stackNameIndex;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db = new SmartDBAdapter(this);
		db.open();
		
		getMenuItems();
		
		setListAdapter(new ArrayAdapter<String>(StackMenu.this, android.R.layout.simple_list_item_1, menuItems));
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Toast.makeText(getApplicationContext(), menuItems[position], Toast.LENGTH_SHORT).show();
	}
	
	private void getMenuItems() {
		Cursor cursor = db.getStacks();
		int numStacks = cursor.getCount();
		menuItems = new String[numStacks];
		stackNameIndex = cursor.getColumnIndex(STACK_NAME);
		cursor.moveToFirst();
		
		for(int i = 0; !cursor.isAfterLast(); i++) {
			menuItems[i] = cursor.getString(stackNameIndex);
			cursor.moveToNext();
		}
	}
}

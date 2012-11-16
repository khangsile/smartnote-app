package com.example.smartnote;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class StackMenu extends ListActivity {
	
	String menuItems[] = {"WTF", "WOW", "WWW", "WMD"};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(StackMenu.this, android.R.layout.simple_list_item_1, menuItems));
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Toast.makeText(getApplicationContext(), menuItems[position], Toast.LENGTH_SHORT).show();
	}
}

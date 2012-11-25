package com.example.smartnote;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StacksGallery extends ListActivity {
				
		
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			        android.R.layout.simple_list_item_1, getMenuItems());
		
		setListAdapter(adapter);
						
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    String item = (String) getListAdapter().getItem(position);
	    Intent intent = new Intent(this, ModeChooser.class);
	    intent.putExtra("stack", item);
	    startActivity(intent);
	  }

	
	private List<String> getMenuItems() {
		SmartDBAdapter db = new SmartDBAdapter(this);
		db.open();

		List<Model> list = new ArrayList<Model>();
		list = db.getStacks();
		
		List<String> stringList = new ArrayList<String>();
		for (Model model:list) {
			stringList.add(model.getName());
		}
		
		db.close();
		return stringList;
	}
	
}

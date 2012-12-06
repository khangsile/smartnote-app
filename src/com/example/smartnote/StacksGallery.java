package com.example.smartnote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

public class StacksGallery extends ListActivity {
				
		
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Your Stacks");
		setContentView(R.layout.stacksgallery);
			
		StacksGallArrayAdapter adapter = new StacksGallArrayAdapter(this, getMenuItems());
		
		setListAdapter(adapter);
						
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    String item = ((Model) getListAdapter().getItem(position)).getName();
	    v.setBackgroundColor(Color.LTGRAY);
	    Intent intent = new Intent(this, ModeChooser.class);
	    intent.putExtra("stack", item);
	    startActivity(intent);
	  }

	
	private List<Model> getMenuItems() {
		SmartDBAdapter db = new SmartDBAdapter(this);
		db.open();

		List<Model> list = new ArrayList<Model>();
		list = db.getStacks();
		
		
		db.close();
		
		Collections.sort(list, new CustomComparator());
		return list;
	}
	
	public class CustomComparator implements Comparator<Model> {

		@SuppressLint("DefaultLocale")
		@Override
		public int compare(Model lhs, Model rhs) {
			// TODO Auto-generated method stub
			return lhs.getName().toLowerCase()
					.compareTo(rhs.getName().toLowerCase());
		}
		
	}

	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	        Intent intent = new Intent(this, SmartNoteActivity.class);
	        startActivity(intent);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
			
}

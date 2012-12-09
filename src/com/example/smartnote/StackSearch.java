package com.example.smartnote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.actionbarsherlock.app.SherlockListActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class StackSearch extends SherlockListActivity {
				
	private String query = "";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stacksgallery);
			
		query = getIntent().getStringExtra("query");
		
		List<Model> list = new ArrayList<Model>(getMenuItems());
		StacksGallArrayAdapter adapter = new StacksGallArrayAdapter(this, list);
		
		TextView header = (TextView)findViewById(R.id.stacks);
		header.setText(list.size() + " Stacks");
		
    	Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
    	header.setTypeface(chinacat);
    	
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
		list = db.searchStacks(query);
		
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
	    	finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
			
}

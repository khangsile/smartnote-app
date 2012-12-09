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

public class CardSearch extends SherlockListActivity {
				
	private String query = "";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stacksgallery);
			
		query = getIntent().getStringExtra("query");
		
		List<CardModel> list = new ArrayList<CardModel>(getMenuItems());
		CardSearchAdapter adapter = new CardSearchAdapter(this, list);
		
		TextView header = (TextView)findViewById(R.id.stacks);
		header.setText(list.size() + " Cards");
		
    	Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
    	header.setTypeface(chinacat);		
		setListAdapter(adapter);
						
	}
		
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    int stackID = ((CardModel) getListAdapter().getItem(position)).getStack();
	    
	    SmartDBAdapter db = new SmartDBAdapter(this);
	    db.open();
	    
	    String stack = "";
	    try {
	    	stack = db.getStackName(stackID);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
	    db.close();
	    
	    CardModel card = new CardModel((CardModel) getListAdapter().getItem(position));
	    
	    v.setBackgroundColor(Color.LTGRAY);
	    Intent intent = new Intent(this, Card.class);
	    intent.putExtra("stack", stack);
	    intent.putExtra("card", card);
	    startActivity(intent);
	  }

	
	private List<CardModel> getMenuItems() {
		SmartDBAdapter db = new SmartDBAdapter(this);
		db.open();

		List<CardModel> list = new ArrayList<CardModel>();
		list = db.searchCards(query);
		
		db.close();
		
		Collections.sort(list, new CustomComparator());
		return list;
	}
	
	public class CustomComparator implements Comparator<CardModel> {

		@SuppressLint("DefaultLocale")
		@Override
		public int compare(CardModel lhs, CardModel rhs) {
			// TODO Auto-generated method stub
			return lhs.getTitle().toLowerCase()
					.compareTo(rhs.getTitle().toLowerCase());
		}
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
			
}


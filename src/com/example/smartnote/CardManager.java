package com.example.smartnote;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CardManager extends ListActivity {

	SmartDBAdapter db;
	
	private String stack;
	
	CardArrayAdapter adapter;
	
	ListView listview;
		
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardmanager);
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			try {
				stack = extras.getString("stack");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			finish();
		}
		
		Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
		
		Button bCopy = (Button) findViewById(R.id.copy);	
		bCopy.setTypeface(chinacat);
		Button bDelete = (Button) findViewById(R.id.delete);
		bDelete.setTypeface(chinacat);
		
		@SuppressWarnings("unchecked")
		final List<CardModel> data = (List<CardModel>) getLastNonConfigurationInstance();
	    if (data != null) {
	        adapter = new CardArrayAdapter(this, data);
	    } else {
				adapter = new CardArrayAdapter(this, getMenuItems());
	    }
	    	    
		setListAdapter(adapter);
		listview = getListView();
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	public Object onRetainNonConfigurationInstance() {
	    final List<CardModel> data = adapter.getList();
	    return data;
	}
	
	private List<CardModel> getMenuItems() {
		
		db = new SmartDBAdapter(this);
		db.open();
		
		try {
			List<CardModel> list = new ArrayList<CardModel>();
			list = db.getItems(stack);
		
			db.close();
			return list; 
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return null;
	}
	
	public void cancel(View view) {		
		setResult(RESULT_CANCELED);
		finish();
	}
	
		
	public void delete(View view) {
		SmartDBAdapter db = new SmartDBAdapter(this);
		db.open();
		
		List<CardModel> cards = adapter.getList();
		for (int i=0; i<cards.size(); i++) {
			if (cards.get(i).isSelected()) {
				try {
				db.deleteCard(cards.get(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		Intent intent = new Intent(this, CardManager.class);
		intent.putExtra("stack", stack);
		startActivity(intent);
	}
	
	public void copy(View view) {
		
		Intent intent = new Intent(this, StackMenu.class);
		intent.putExtra("stack", stack);
		
		startActivityForResult(intent, 1);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode,resultCode,data);
		if (resultCode == RESULT_OK && requestCode == 1) {
			if (data != null) {
				String extraStacks = data.getStringExtra("stack");
				String[] stacks = splitStacks(extraStacks);
				
				copyCards(stacks);
			}
		}
	}
	
	private void copyCards(String[] stacks) {
		List<CardModel> cards = adapter.getList();
		
		db = new SmartDBAdapter(this);
		db.open();
		
		for (CardModel card: cards) {
			
			if (card.isSelected()) {
				String title = card.getTitle();
				String definition = card.getDef();
				
				for(int i=0; i < stacks.length; i++) {
					if (!stacks[i].equals("")) {
						try {
						if (!db.matchCard(title, definition, stacks[i]))
							db.insertCard(title, definition, stacks[i]);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}	
			}
		}
		
		db.close();
	}
	
	private String[] splitStacks(String s) {
		String[] stacks = s.split(";");
		for(int i=0; i<stacks.length; i++) {
			stacks[i] = stacks[i].trim();
		}
		return stacks;
	}

	
	public void onBackPressed() {
		Intent intent = new Intent(this, ModeChooser.class);
		intent.putExtra("stack", stack);
		startActivity(intent);
	}
}

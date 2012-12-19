package com.example.smartnote;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CardManager extends SherlockListActivity {

	SmartDBAdapter db;
	
	private String stack;
	private static final int HOME = 1;
	
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
		
				
		List<CardModel> list = new ArrayList<CardModel>();
		
		try {
			Deck deck = new Deck(stack, this);
			deck.alphabetize();
			list = deck.getCards();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return list;
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
		
		db.close();
		
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
	
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0, HOME, 0, "Home")
    	.setIcon(R.drawable.ic_menu_home)
    	.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
		searchView.setQueryHint("Search");
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), SmartSearchView.class);
				intent.putExtra("query", query);
				
				startActivity(intent);
				
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		
		menu.add("Search")
        .setIcon(R.drawable.ic_menu_search)
        .setActionView(searchView)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		
    	SubMenu subMenu1 = menu.addSubMenu("Navigation");
        subMenu1.add("Stacks Gallery")
        	.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					// TODO Auto-generated method stub
					Intent sgIntent = new Intent(getApplicationContext(), StacksGallery.class);
					startActivity(sgIntent);
					return true;
				}
        		
        	});
        subMenu1.add("New Card")
        	.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        		public boolean onMenuItemClick(MenuItem item) {
					Intent ccIntent = new Intent(getApplicationContext(), CardCreator.class);
					startActivity(ccIntent);
        			return true;	
        		}
        	});
        subMenu1.add("Download Stack")
        	.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        		public boolean onMenuItemClick(MenuItem item) {
        			//Intent dlIntent = new Intent(getApplicationContext(), )
        			Toast.makeText(getApplicationContext(), "Feature in next version?", 250).show();
        			return true;
        		}
        	});
        		    	
    	MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setIcon(R.drawable.abs__ic_menu_moreoverflow_normal_holo_dark);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    	
	    return true;
	  }
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case HOME:
			Intent home = new Intent(getApplicationContext(), SmartNoteActivity.class);
			startActivity(home);
			break;
		default:
			break;
		}
		
		return true;
	}

	
}

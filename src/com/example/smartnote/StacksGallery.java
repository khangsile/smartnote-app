package com.example.smartnote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StacksGallery extends SherlockListActivity {
				
	private static final int HOME = 1;	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Your Stacks");
		setContentView(R.layout.stacksgallery);
			
		
		List<Model> list = new ArrayList<Model>(getMenuItems());
		StacksGallArrayAdapter adapter = new StacksGallArrayAdapter(this, list);
		
		TextView header = (TextView)findViewById(R.id.stacks);
		header.setText(list.size() + " Stacks");
		
    	Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
    	header.setTypeface(chinacat);
		
		setListAdapter(adapter);
						
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

package com.example.smartnote;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class SmartNoteActivity extends SherlockActivity {

	private static final int HOME = 1;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
                
    	Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
        
        Button stacks = (Button)findViewById(R.id.stacks);
        stacks.setTypeface(chinacat);
        Button create = (Button)findViewById(R.id.createNew);
        create.setTypeface(chinacat);
        
    }
    
    /** Sends the user to the list of their stacks */
    public void toStacks(View view) {
    	Intent intent = new Intent(this, StacksGallery.class);
    	startActivity(intent);
    }
    
    public void toCreate(View view) {
    	
    	Intent intent = new Intent(this, CardCreator.class);
    	startActivity(intent);
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	Intent intent = new Intent(Intent.ACTION_MAIN);
	        intent.addCategory(Intent.CATEGORY_HOME);
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        startActivity(intent);
	        return true;
	    }
    return super.onKeyDown(keyCode, event);
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
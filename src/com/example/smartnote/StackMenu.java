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

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class StackMenu extends SherlockListActivity {
	
	SmartDBAdapter db;
	
	private String stack;
	private static final int HOME = 1;
	
	InteractiveArrayAdapter adapter;
	
	ListView listview;
		
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stackmenu);
		setTitle("Your Stacks");
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			try {
				stack = extras.getString("stack");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			stack = "";
		}
		
		Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
		
		Button bAddStacks = (Button) findViewById(R.id.addStacks);	
		bAddStacks.setTypeface(chinacat);
		Button bCancel = (Button) findViewById(R.id.cancel);
		bCancel.setTypeface(chinacat);
		
		@SuppressWarnings({ "unchecked", "deprecation" })
		final List<Model> data = (List<Model>) getLastNonConfigurationInstance();
	    if (data != null) {
	        adapter = new InteractiveArrayAdapter(this, data);
	    } else {
				adapter = new InteractiveArrayAdapter(this, getMenuItems());
	    }
	    
	    modifyStack();
	    
		setListAdapter(adapter);
		listview = getListView();
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	public Object onRetainNonConfigurationInstance() {
	    final List<Model> data = adapter.getList();
	    return data;
	}
	
	private List<Model> getMenuItems() {
		
		db = new SmartDBAdapter(this);
		db.open();
		
		List<Model> list = new ArrayList<Model>();
		list = db.getStacks();
		
		db.close();
		return list;
	}
	
	public void cancel(View view) {		
		setResult(RESULT_CANCELED);
		finish();
	}
	
	public void addStacks(View view) {
	
		List<Model> models = adapter.getList();
				
		for (int i=0; i < models.size(); i++) {
			if (models.get(i).isSelected()==true) {
				if (stack.equals("")) 
					stack = models.get(i).getName();
				else
					stack = stack + ";" + models.get(i).getName();
			}
		}
		
		Intent intent = getIntent();
		intent.putExtra("stack", stack);
		
		setResult(RESULT_OK,intent);
		finish();
	}
	
	public void modifyStack() {
		String[] stackNames = stack.split(";");
		for (String stackName: stackNames) 
			stackName.trim();
		
		List<Model> models = adapter.getList();
		for (int i = 0; i < stackNames.length; i++) {
			for (int j = 0; j < models.size(); j++) {
				if (stackNames[i].equals(models.get(j).getName())) {
					models.get(j).setSelected(true);
					stackNames[i] = "";
				}
			}
		}
		
		stack = "";
		
		for (int i=0; i < stackNames.length; i++) {
			if (!stackNames[i].equals(""))
				stack = stack + ";" + stackNames[i];
		}
		
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

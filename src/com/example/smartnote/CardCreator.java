package com.example.smartnote;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.example.smartnote.R;

public class CardCreator extends SherlockActivity {
	
	private SmartDBAdapter db;
		
	private static final int HOME = 1;
	
	private String definition = " ", title = " ", stack = " ";
	private EditText sInput, tInput, defInput;
		
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creator);
		
		db = new SmartDBAdapter(this);
		db.open();
		
		Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
		
		TextView titleTxt = (TextView)findViewById(R.id.title_header);
		titleTxt.setTypeface(chinacat);
		TextView defTxt = (TextView)findViewById(R.id.definition_header);
		defTxt.setTypeface(chinacat);
		TextView stackTxt = (TextView)findViewById(R.id.stack_header);
		stackTxt.setTypeface(chinacat);
		
		Button cardCreator = (Button)findViewById(R.id.createCard);
		cardCreator.setTypeface(chinacat);
		Button stackChooser = (Button)findViewById(R.id.stackButton);
		stackChooser.setTypeface(chinacat);
		
		
		sInput = (EditText)findViewById(R.id.newStack);
		sInput.setTypeface(chinacat);
		tInput = (EditText)findViewById(R.id.title);
		tInput.setTypeface(chinacat);
		defInput = (EditText)findViewById(R.id.definition);
		defInput.setTypeface(chinacat);
		
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

	public void onDestroy() {
		super.onDestroy();
		db.close();
	}
	
	public void createCard(View view) {
				
		getText();
						
		if (title.isEmpty() || definition.isEmpty() || stack.isEmpty())
			Toast.makeText(getApplicationContext(), "Your card contains unwritten side(s)", Toast.LENGTH_SHORT).show();
		else {
			String[] stacks = splitStacks(stack);
			insertStacks(stacks);
			
			long insTester[] = new long[stacks.length];
			for(int i=0; i < stacks.length; i++) {
				if (stacks[i].equals("")) {
				}else if (db.matchCard(title, definition, stacks[i])) {
					insTester[i] = -1;
				} else 
					insTester[i] = db.insertCard(title, definition, stacks[i]);
			}
			
			for (int i = 0; i < insTester.length; i++) {
				if (insTester[i] == -1 && !stacks[i].equals("")) 
					Toast.makeText(getApplicationContext(), "Not inserted in " + stacks[i],
							500).show();
			}
			
			Intent intent = new Intent(this, SmartNoteActivity.class);
			startActivity(intent);
		} 
	}
		
	public void insertStacks(String[] stacks) {
		for (String sName:stacks) {
			if (!sName.equals("") && !db.matchStack(sName)) {
				db.insertStack(sName);
			}
		}
	}
	private String[] splitStacks(String s) {
		String[] stacks = s.split(";");
		for(int i=0; i<stacks.length; i++) {
			stacks[i] = stacks[i].trim();
		}
		return stacks;
	}
	
	public void toStacks(View view) {
		
		getText();
		
		Intent intent = new Intent(this, StackMenu.class);
		intent.putExtra("stack", stack);
		
		startActivityForResult(intent, 1);
	}

	protected void onStop() {
		super.onStop();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	        Intent intent = new Intent(this, SmartNoteActivity.class);
	        startActivity(intent);
	        return true;
	    }
	    if ((keyCode == KeyEvent.KEYCODE_SEARCH)) {
	    	getText();
	    	
	    	Intent intent = new Intent(this, StackMenu.class);
			intent.putExtra("stack", stack);
			
	    	startActivityForResult(intent, 1);
	    	return true;
	    }
	    
	    return super.onKeyDown(keyCode, event); 

	}
	
	private void getText() {
		EditText editText = (EditText)findViewById(R.id.title);
		title = editText.getText().toString();
		title = title.trim();
		editText = (EditText)findViewById(R.id.definition);
		definition = editText.getText().toString();
		definition = definition.trim();
		editText = (EditText)findViewById(R.id.newStack);
		stack = editText.getText().toString();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode,resultCode,data);
		if (resultCode == RESULT_OK && requestCode == 1) {
			if (data != null) {
				sInput.setText(data.getStringExtra("stack"));
			}
		}
	}
}

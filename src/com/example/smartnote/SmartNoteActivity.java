package com.example.smartnote;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class SmartNoteActivity extends SherlockActivity {
    	
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
    	
    	menu.add("Home")
    	.setIcon(R.drawable.ic_menu_home)
    	.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    	
    	SubMenu subMenu1 = menu.addSubMenu("Navigation");
        subMenu1.add("Stacks Gallery");
        subMenu1.add("New Card");
        subMenu1.add("Download Stack");
        
        MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setIcon(R.drawable.ic_menu_moreoverflow);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
    	menu.add("Search")
        .setIcon(R.drawable.ic_menu_search)
        .setActionView(R.layout.collapsable_edit_text)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
    	
    	return true;
    	
    }
        

    
}
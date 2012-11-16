package com.example.smartnote;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SmartNoteActivity extends Activity {
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
    	Intent intent = new Intent(this, Card.class);
    	startActivity(intent);
    }
    
    public void toCreate(View view) {
    	
    	Intent intent = new Intent(this, CardCreator.class);
    	startActivity(intent);
    }
}
package com.example.smartnote;

import android.app.Activity;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.smartnote.R;

public class CardCreator extends Activity {
	
	SmartDBAdapter db;
	
	static final String TITLE = "mytitles";
	static final String DEFINITION = "mydefinitions";
	String definition, title;
	
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
		
		Button cardCreator = (Button)findViewById(R.id.createCard);
		cardCreator.setTypeface(chinacat);
		
		EditText tInput = (EditText)findViewById(R.id.title);
		tInput.setTypeface(chinacat);
		EditText defInput = (EditText)findViewById(R.id.definition);
		defInput.setTypeface(chinacat);
			
	}
	
	public void onDestroy() {
		super.onDestroy();
		db.close();
	}
	
	public void createCard(View view) {
		
		getText();
		
		if (title.isEmpty() || definition.isEmpty())
			Toast.makeText(getApplicationContext(), "Your card contains unwritten side(s)", Toast.LENGTH_SHORT).show();
		else {
			if(db.matchCard(title, definition)) {
				Toast.makeText(getApplicationContext(), "Already in!", Toast.LENGTH_SHORT).show();
			}
			else {
				
				int tdefault = 1;
				long id = db.insertCard(tdefault,title, definition);
				if (id != -1) {
					
					Toast.makeText(getApplicationContext(), "inserted!", Toast.LENGTH_SHORT).show();
					
					Intent intent = new Intent(this, SmartNoteActivity.class);
					startActivity(intent);
				}	
				else
					Toast.makeText(getApplicationContext(), "not inserted!", Toast.LENGTH_SHORT).show();
			}
			
		}
	}

	public void onSaveInstanceState(Bundle savedInstanceState) {
		getText();
			
		savedInstanceState.putString(TITLE, title);
		savedInstanceState.putString(DEFINITION, definition);
		super.onSaveInstanceState(savedInstanceState);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		title = savedInstanceState.getString(TITLE);
		definition = savedInstanceState.getString(DEFINITION);
		
		EditText editText = (EditText)findViewById(R.id.title);
		editText.setText(title);
		
		editText = (EditText)findViewById(R.id.definition);
		editText.setText(definition);
				
	}

	protected void onStop() {
		super.onStop();
	}
	
	private void getText() {
		EditText editText = (EditText)findViewById(R.id.title);
		title = editText.getText().toString();
		editText = (EditText)findViewById(R.id.definition);
		definition = editText.getText().toString();
	}
}

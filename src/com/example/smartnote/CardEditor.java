package com.example.smartnote;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

public class CardEditor extends SherlockActivity {
		
	private CardModel card;
	private String stack;
	private EditText etTitle, etDef;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_editor);
	
		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			try {
				card = (CardModel)extras.getParcelable("Card");
				stack = extras.getString("stack");
				initialize();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			finish();
		}
		
	}
	
	public void initialize() {
		etTitle = (EditText)findViewById(R.id.title);
		etDef = (EditText)findViewById(R.id.definition);
		
		etTitle.setText(card.getTitle());
		etDef.setText(card.getDef());	
		
		toChinacat();
	}
	
	public void toChinacat() {
		
		Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
		
		TextView tvTitle = (TextView)findViewById(R.id.title_header);
		TextView tvDef = (TextView)findViewById(R.id.def_header);
		
		Button cancel = (Button)findViewById(R.id.cancel);
		Button submit = (Button)findViewById(R.id.edit);
		
		cancel.setTypeface(chinacat);
		submit.setTypeface(chinacat);
		
		tvTitle.setTypeface(chinacat);
		tvDef.setTypeface(chinacat);
		
		etTitle.setTypeface(chinacat);
		etDef.setTypeface(chinacat);
	}
	
	public void getText() {
		card.setTitle(etTitle.getText().toString().trim());
		card.setDef(etDef.getText().toString().trim());
	}
	
	public void cancel(View view) {
		finish();
	}
	
	public void edit(View view) {
		
		getText();
		
		SmartDBAdapter db = new SmartDBAdapter(this);
		db.open();
		
		db.updateCard(card);
	
		db.close();
		
		Intent i = new Intent();
		i.putExtra("Card", card);
		
		setResult(RESULT_OK, i);
		finish();		
	}
	
	public void add(View view) {
		
		getText();
		
		SmartDBAdapter db = new SmartDBAdapter(this);
		db.open();
		
		if (db.matchCard(card.getTitle(), card.getDef(), card.getStack())) {
			Toast.makeText(this, "Already In!", 250).show();
		} else {
			db.insertCard(card.getTitle(), card.getDef(), card.getStack());
			
			Intent intent = new Intent(this, Card.class);
			intent.putExtra("stack", stack);
			
			startActivity(intent);
		}
	}

}

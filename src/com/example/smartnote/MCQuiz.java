package com.example.smartnote;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MCQuiz extends Activity {
	
	private String answer, stack;
	private TextView question, indexTxt;
	private RadioButton[] choices;
	private int correct=0, atts=0;
	
	private List<CardModel> answers;
	
	private Deck deck;
	
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mcquiz);
		
		initialize();
		deck.shuffle();
		
		if (deck.getSize() <= 4) {
			Toast.makeText(getApplicationContext(), "Sorry! This feature requires \nmore than 4 cards in your deck.",500).show();
			finish();
		} else {
			toChinaCat();
			
			displayCard();
			
			getChoices();
		}
	}
		
	public void initialize() {
		indexTxt = (TextView)findViewById(R.id.index);
		
		Bundle extras = getIntent().getExtras();
		stack = extras.getString("stack");
		
		deck = new Deck(stack, this);
					
		question = (TextView) findViewById(R.id.question);
		
		choices = new RadioButton[4];
		choices[0] = (RadioButton) findViewById(R.id.a);
		choices[1] = (RadioButton) findViewById(R.id.b);
		choices[2] = (RadioButton) findViewById(R.id.c);
		choices[3] = (RadioButton) findViewById(R.id.d);
		
		answers = new ArrayList<CardModel>();
	}
	
	private void toChinaCat() {
		Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
		for (int i=0; i<choices.length;i++)
			choices[i].setTypeface(chinacat);
		question.setTypeface(chinacat);
		indexTxt.setTypeface(chinacat);
			
	}
	
	private void update(boolean isCorrect) {
			if (isCorrect) {
	    		deck.getCard().correct();
	    		correct++;
	    		atts++;
	    	} else {
	    		deck.getCard().wrong();
	    		atts++;
	    	}
	    	SmartDBAdapter db = new SmartDBAdapter(this);
	    	db.open();
	    	
	    	db.updateCard(deck.getCard());
	    	
	    	db.close();
	    }
	
	private void displayCard() {
		
		try {		
			CardModel card = deck.getCard();
			question.setText(card.getDef());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			indexTxt.setText(deck.getIndex()+"/"+(deck.getSize()-1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/*Gets the choices by randomly selecting four cards from the list*/
	private void getChoices() {		
		answers = deck.getChoices();
		
		for (int i=0; i < answers.size(); i++)  {
			choices[i].setText(answers.get(i).getTitle());
		}
	}
	
	public void onRadioButtonClicked(View view) {
			int selectedid = view.getId();
	        answer = ((RadioButton)findViewById(selectedid)).getText().toString();
	}
	
	public void mcAnswer(View view) {
		
		try {
			if (answer.equals(deck.getCard().getTitle())) {
				Toast.makeText(getApplicationContext(), "Correct!", 500).show();
				update(true);
			} else {
				Toast.makeText(getApplicationContext(), "Answer is " + deck.getCard().getTitle(), 500).show();
				update(false);
			} } catch (Exception e) {
				e.printStackTrace();
		}
		
		
		if (!deck.changeCard(true)) {
			SmartDBAdapter db = new SmartDBAdapter(this);
			db.open();
				
			db.insertMCTest(correct, atts, stack);
				
			finish();
		}
		
		displayCard();
		getChoices();
		
		RadioGroup rgButton = (RadioGroup)findViewById(R.id.rGroup);
		rgButton.clearCheck(); 
	}
	
	public Object onRetainNonConfigurationInstance() {
	    return deck;
	}
	
	public void onSaveInstanceState(Bundle savedInstanceState) {	
		super.onSaveInstanceState(savedInstanceState);
	}
	
	@SuppressWarnings("deprecation")
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		try {
			deck = (Deck) getLastNonConfigurationInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		displayCard();									
	}
	
	public void onBackPressed() {
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Closing Activity")
        .setMessage("Are you sure you want to close this activity? " +
        		"The current statistics for this quiz will be lost!")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();    
        }

        })
    	.setNegativeButton("No", null)
    	.show();
	}

}

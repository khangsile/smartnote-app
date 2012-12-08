package com.example.smartnote;

import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class MemQuiz extends Activity implements OnInitListener {
			
	private int MY_DATA_CHECK_CODE = 0;
	
	private TextView defTxt, indexTxt;
	private EditText etAnswer;
	private Button speak, answer;
	private TextToSpeech myTTS;
	private int correct=0, atts=0;
	private String stack;
	
	private Deck deck;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memquiz);
		
		initialize();
		toChinaCat();
		
		displayCard();
	}
	
	public Object onRetainNonConfigurationInstance() {
	    return deck;
	}
	
	private void initialize() {
		
		indexTxt = (TextView)findViewById(R.id.index);
		
		Bundle extras = getIntent().getExtras();
		stack = extras.getString("stack");
		
		deck = new Deck(stack, this);
		deck.shuffle();
		
		defTxt = (TextView)findViewById(R.id.defTxt);
		
		etAnswer = (EditText)findViewById(R.id.answerHeader);
		
		speak = (Button)findViewById(R.id.speak);	
		answer = (Button)findViewById(R.id.answer);
				
		Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
	}
		
	private void toChinaCat() {
		
		Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
		
		indexTxt.setTypeface(chinacat);
		etAnswer.setTypeface(chinacat);
		defTxt.setTypeface(chinacat);
		speak.setTypeface(chinacat);
		answer.setTypeface(chinacat);
		
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
			defTxt.setText(card.getDef());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			indexTxt.setText(deck.getIndex()+"/"+(deck.getSize()-1));
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
		
	@SuppressLint("DefaultLocale")
	public void answer(View view) {
		String userAnswer = etAnswer.getText().toString();
		String rAnswer = deck.getCard().getTitle();
		
		userAnswer = userAnswer.trim();
		userAnswer = userAnswer.toLowerCase();
		rAnswer = rAnswer.trim();
		String modAnswer = rAnswer.toLowerCase();
		
		
			if (userAnswer.equals(modAnswer)) {
				Toast.makeText(getApplicationContext(), "Correct!", 500).show();
				update(true);
			} else {
				Toast.makeText(getApplicationContext(), "Answer is " + rAnswer, 500).show();
				update(false);
			}
			
		if (!deck.changeCard(true)) {
			SmartDBAdapter db = new SmartDBAdapter(this);
			db.open();
					
			db.insertMemTest(correct, atts, stack);
					
			finish();
		}
		
		etAnswer.setText("");
		displayCard();
	}
		
	public void talk(View view) {
		
		String def = deck.getCard().getDef();
		
		readCard(def);
		
	}
	
	private void readCard(String phrase) {
		myTTS.speak(phrase, TextToSpeech.QUEUE_FLUSH, null);
	}
	
	public void onSaveInstanceState(Bundle savedInstanceState) {	
		super.onSaveInstanceState(savedInstanceState);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		try {
			deck = (Deck) getLastNonConfigurationInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		displayCard();									
	}
	
	public void onDestroy() {
		super.onDestroy();
	}
		
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == MY_DATA_CHECK_CODE) {
	    	if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
	                //the user has the necessary data - create the TTS
	        myTTS = new TextToSpeech(this, this);
	        } else {
	                    //no data - install it now
	        	Intent installTTSIntent = new Intent();
	            installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
	            startActivity(installTTSIntent);
	        }
	    }
    }
	
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {
			if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE) 
				myTTS.setLanguage(Locale.US);
		} else {
			Toast.makeText(getApplicationContext(), "Sorry...Text To Speech Error", Toast.LENGTH_SHORT).show();
		}
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

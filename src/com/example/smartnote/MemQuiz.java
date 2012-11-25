package com.example.smartnote;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
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
	
	private TextView defTxt;
	private EditText etAnswer;
	private Button speak, answer;
	private TextToSpeech myTTS;
	
	private List<CardModel> cardList;
	private Queue<CardModel> cardQueue;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memquiz);
		
		initialize();
		toChinaCat();
		
		cardQueue = (Queue<CardModel>) getLastNonConfigurationInstance();
	    if (cardQueue == null) 
	    	shuffle();

		getCard();
	}
	
	public Object onRetainNonConfigurationInstance() {
	    return cardQueue;
	}
	
	private void initialize() {
		
		Bundle extras = getIntent().getExtras();
		String stack = extras.getString("stack");
		
		SmartDBAdapter db = new SmartDBAdapter(this);
		db.open();
			
		cardList = new ArrayList<CardModel>();
		cardList = db.getItems(stack);
		
		db.close();
		
		defTxt = (TextView)findViewById(R.id.defTxt);
		
		etAnswer = (EditText)findViewById(R.id.answerHeader);
		
		speak = (Button)findViewById(R.id.speak);	
		answer = (Button)findViewById(R.id.answer);
				
		Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
	}
	
	private void shuffle() {
		
		cardQueue = new LinkedList<CardModel>();
		
		for(int i = cardList.size(); i > 1; i--) {
			Random numberGen = new Random();
			int shuffler = numberGen.nextInt(i);
			cardQueue.add(cardList.remove(shuffler));
		}		
		cardQueue.add(cardList.get(0));
	}
	
	private void toChinaCat() {
		
		Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
		
		etAnswer.setTypeface(chinacat);
		defTxt.setTypeface(chinacat);
		speak.setTypeface(chinacat);
		answer.setTypeface(chinacat);
		
	}

	private void getCard() {
		
		String definition = cardQueue.peek().getDef();
		
		defTxt.setText(definition);
	}
		
	@SuppressLint("DefaultLocale")
	public void answer(View view) {
		String userAnswer = etAnswer.getText().toString();
		String rAnswer = cardQueue.element().getTitle();
		
		userAnswer = userAnswer.trim();
		userAnswer = userAnswer.toLowerCase();
		rAnswer = rAnswer.trim();
		String modAnswer = rAnswer.toLowerCase();
		
		if (cardQueue.size() > 1) {
			if (userAnswer.equals(modAnswer)) {
				Toast.makeText(getApplicationContext(), "Correct!", 500).show();
				cardQueue.remove();
			} else {
				Toast.makeText(getApplicationContext(), "Answer is " + rAnswer, 500).show();
				cardQueue.offer(cardQueue.remove());
				} 
			} else {
				finish();
			}
		etAnswer.setText("");
		getCard();
	}
		
	public void talk(View view) {
		
		String def = cardQueue.peek().getDef();
		
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

}

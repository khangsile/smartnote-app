package com.example.smartnote;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class Card extends Activity implements OnInitListener {
	
	SmartDBAdapter db;
	
	public static final String KEY_DEFS = "definition"; 
	public static final String KEY_TITLES = "title";
	private static final String CURSOR_POS = "cursorPos";
	private static final String FLIP_TAB = "flipperTab";
	
	private int defIndex, titleIndex, MY_DATA_CHECK_CODE = 0;
	private Cursor cursor;
	
	private TextView defTxt, titleTxt;
	private Button flipHandler, forward, backward, speak;
	private ViewFlipper flipper;
	private TextToSpeech myTTS;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card);
		
		initialize();
		toChinaCat();
		
		
	}
	
		
	private void initialize() {
		
		db = new SmartDBAdapter(this);
		db.open();
				
		cursor = db.getItems();
		
		defIndex = cursor.getColumnIndex(KEY_DEFS);
		titleIndex = cursor.getColumnIndex(KEY_TITLES);
				
		cursor.moveToFirst();
				
		titleTxt = (TextView)findViewById(R.id.titleTxt);
		defTxt = (TextView)findViewById(R.id.defTxt);
		
		speak = (Button)findViewById(R.id.speak);		
	    forward = (Button)findViewById(R.id.forward);
		backward = (Button)findViewById(R.id.backward);
		flipHandler = (Button)findViewById(R.id.flip);
		
		flipper = (ViewFlipper)findViewById(R.id.flipper);
		
		Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

		
		getCard();
		
	}
	
	private void getCard() {
		
		String definition = cursor.getString(defIndex);
		String title = cursor.getString(titleIndex);
		
		titleTxt.setText(title);
		defTxt.setText(definition);

	}
	
	private void toChinaCat() {
		
		Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
		
		titleTxt.setTypeface(chinacat);
		defTxt.setTypeface(chinacat);
		forward.setTypeface(chinacat);
		backward.setTypeface(chinacat);
		flipHandler.setTypeface(chinacat);
		speak.setTypeface(chinacat);
		
	}
	
	public void moveForward(View view) {
		if (!cursor.isLast()) {
			cursor.moveToNext();
		} else {
			Toast.makeText(getApplicationContext(), "End of the Deck", Toast.LENGTH_SHORT).show();
		}
		
		getCard();

	}
	
	public void moveBackward(View view) {
		if (!cursor.isFirst()) {
			cursor.moveToPrevious();
		} else {
			Toast.makeText(getApplicationContext(), "Beginning of the Deck", Toast.LENGTH_SHORT).show();
		}
		
		getCard();
		
	}
	
	public void flip(View view) {
		
		flipper.showNext();
	}
	
	public void talk(View view) {
		
		int flipperView = flipper.getDisplayedChild();
		int flipperTitle = flipper.indexOfChild(titleTxt);
		
		String title = cursor.getString(titleIndex);
		String def = cursor.getString(defIndex);
		
		if (flipperTitle == flipperView)
			readCard(title);
		else
			readCard(def);
		
	}
	
	private void readCard(String phrase) {
		
		myTTS.speak(phrase, TextToSpeech.QUEUE_FLUSH, null);
	}
	
	public void onSaveInstanceState(Bundle savedInstanceState) {
		
		savedInstanceState.putInt(FLIP_TAB, flipper.getDisplayedChild());
		savedInstanceState.putInt(CURSOR_POS, cursor.getPosition());
		
		super.onSaveInstanceState(savedInstanceState);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		int cursorPosition = savedInstanceState.getInt(CURSOR_POS);
		int flipperPos = savedInstanceState.getInt(FLIP_TAB);
		
		cursor.moveToPosition(cursorPosition);
		
		String title = cursor.getString(titleIndex);
		String def = cursor.getString(defIndex);
		
		titleTxt.setText(title);
		defTxt.setText(def);
		
		flipper.setDisplayedChild(flipperPos);
		
	}
	
	public void onDestroy() {
		super.onDestroy();
		db.close();
	}
	
		
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
	        if (requestCode == MY_DATA_CHECK_CODE) {
	            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
	                //the user has the necessary data - create the TTS
	            myTTS = new TextToSpeech(this, this);
	            }
	            else {
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
			Toast.makeText(getApplicationContext(), "Sorry...Text To Speech Error", Toast.LENGTH_SHORT);
		}
	}

}



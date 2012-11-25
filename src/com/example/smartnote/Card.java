package com.example.smartnote;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class Card extends Activity implements OnInitListener, OnGestureListener {
	private GestureDetector gestureDetector;
		
	private static final String FLIP_TAB = "flipperTab";
	private static final String LIST_POS = "listPos";
	
	private int MY_DATA_CHECK_CODE = 0;
	
	private TextView defTxt, titleTxt;
	private Button flipHandler, forward, backward, speak;
	private ViewFlipper flipper;
	private TextToSpeech myTTS;
	
	private List<CardModel> cardList;
	private int cardListIndex = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card);
		
		initialize();
		toChinaCat();
		
		gestureDetector = new GestureDetector(this);
		
		getCard();
	}
	
		
	private void initialize() {
		
		Bundle extras = getIntent().getExtras();
		String stack = extras.getString("stack");
		
		SmartDBAdapter db = new SmartDBAdapter(this);
		db.open();
				
		cardList = db.getItems(stack);
		
		db.close();
		
		titleTxt = (TextView)findViewById(R.id.titleTxt);
		defTxt = (TextView)findViewById(R.id.defTxt);
		
		speak = (Button)findViewById(R.id.speak);		
	    forward = (Button)findViewById(R.id.forward);
		backward = (Button)findViewById(R.id.backward);
		flipHandler = (Button)findViewById(R.id.flip);
		
		flipper = (ViewFlipper)findViewById(R.id.cardFlipper);
		
		Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
	}
	
	private void getCard() {
		
		String definition = cardList.get(cardListIndex).getDef();
		String title = cardList.get(cardListIndex).getTitle();
		
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
	
	public void changeCard(boolean forward) {
		if (forward) {
			if (cardListIndex != cardList.size()-1) 
				cardListIndex++;
			else 
				Toast.makeText(getApplicationContext(), "End of the Deck", Toast.LENGTH_SHORT).show();
		} else {
			if (cardListIndex != 0) 
				cardListIndex--;
			else 
				Toast.makeText(getApplicationContext(), "Beginning of the Deck", Toast.LENGTH_SHORT).show();
		}
		
		getCard();
	}
	
	public void moveForward(View view) {
		changeCard(true);
	}
	
	public void moveBackward(View view) {
		changeCard(false);
	}
	
	public void flip(View view) {
		flipper.showNext();
	}
	
	public void talk(View view) {
		int flipperView = flipper.getDisplayedChild();
		int flipperTitle = flipper.indexOfChild(titleTxt);
		
		String title = cardList.get(cardListIndex).getTitle();
		String def = cardList.get(cardListIndex).getDef();
		
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
		savedInstanceState.putInt(LIST_POS, cardListIndex);
		
		super.onSaveInstanceState(savedInstanceState);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int flipperPos = savedInstanceState.getInt(FLIP_TAB);
		cardListIndex = savedInstanceState.getInt(LIST_POS);
								
		flipper.setDisplayedChild(flipperPos);
		
		getCard();
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


	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onFling(MotionEvent start, MotionEvent finish, float velocityX,
			float velocityY) {
		
		float xBndUpr = start.getRawX() + 75;
		float xBndLwr = start.getRawX() - 75;
		float yBndUpr = start.getRawY() + 100;
		float yBndLwr = start.getRawY() - 100;
		
		if (finish.getRawY() > yBndLwr && finish.getRawY() < yBndUpr) {
			if (finish.getRawX() > xBndUpr)
				 changeCard(false);
			 else if (finish.getRawX() < xBndLwr)
				 changeCard(true);
		} else if (finish.getRawY() < start.getRawY() + 150)
			 flipper.showNext();
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		flipper.showNext();
		// TODO Auto-generated method stub
		return true;
	}

	public boolean onTouchEvent(MotionEvent me) {
		return gestureDetector.onTouchEvent(me);
		}
}



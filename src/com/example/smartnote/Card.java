package com.example.smartnote;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class Card extends SherlockActivity implements OnInitListener, OnGestureListener {
	private GestureDetector gestureDetector;
			
	private static final String FLIP_TAB = "flipperTab";
	private static final String LIST_POS = "listPos";
	private String stack;
	
	private int MY_TTS_DATA_CHECK_CODE = 0;
	private int MY_STACK_DATA_CHECK_CODE = 1;
	
	private static final int EDIT = 1;
	private static final int DELETE = 2;
	private static final int COPY = 3;
	private static final int SHUFFLE = 4;
	private static final int ALPHABETIZE = 5;
	
	private TextView defTxt, titleTxt;
	private ViewFlipper flipper;
	private TextToSpeech myTTS;
	
	private List<CardModel> cardList;
	private int cardListIndex = 0;
	
	ActionMode mMode;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.card);
        
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		initialize();
		toChinaCat();
		
		gestureDetector = new GestureDetector(this);
		
		getCard();
	}
	
	@Override
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
	
    private final class AnActionModeOfEpicProportions implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        	
        	menu.add(0, EDIT, 0, "Edit")
        		.setIcon(R.drawable.ic_menu_edit)
        		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            
            menu.add(0, DELETE, 0, "Delete")
            	.setIcon(R.drawable.ic_menu_delete)
            	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            
            menu.add(0, COPY, 0, "Copy")
            	.setIcon(R.drawable.ic_menu_copy_light)
            	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

                        
            menu.add(0, SHUFFLE, 0, "Shuffle")
            	.setIcon(R.drawable.ic_menu_shuffle)
            	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            
            menu.add(0, ALPHABETIZE, 0, "Alphabetize")
            	.setIcon(R.drawable.ic_menu_mark)
            	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            
            return true;
        }

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			// TODO Auto-generated method stub
			switch (item.getItemId()) {
			case EDIT:
				break;
			case DELETE:
				delete();
				break;
			case COPY:
				copy();
				break;
			case SHUFFLE:
				shuffle();
				break;
			case ALPHABETIZE:
				alphabetize();
				break;
			default:
				break;
			}
			            mode.finish();
            return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// TODO Auto-generated method stub
			
		}
    }
    	
    private void initialize() {
		
		Bundle extras = getIntent().getExtras();
		stack = extras.getString("stack");
		
		SmartDBAdapter db = new SmartDBAdapter(this);
		db.open();
				
		cardList = db.getItems(stack);
		
		db.close();
		
		titleTxt = (TextView)findViewById(R.id.titleTxt);
		defTxt = (TextView)findViewById(R.id.defTxt);
				
		flipper = (ViewFlipper)findViewById(R.id.cardFlipper);
		
		Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_TTS_DATA_CHECK_CODE);
	}
	
	private void getCard() {
		
		try {
			String definition = cardList.get(cardListIndex).getDef();
			String title = cardList.get(cardListIndex).getTitle();
		
			titleTxt.setText(title);
			defTxt.setText(definition);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void toChinaCat() {
		
		Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
		
		titleTxt.setTypeface(chinacat);
		defTxt.setTypeface(chinacat);
	}
	
	/*Instead of creating two long functions, created a switch card function. 
	 * True to go forward, false to go backwards. Has bounds checking also.
	 */
	public boolean changeCard(boolean forward) {
		if (forward) {
			if (cardListIndex < cardList.size()-1) {
				cardListIndex++;
			}
			else {
				Toast.makeText(getApplicationContext(), "End of the Deck", Toast.LENGTH_SHORT).show();
				return false;
			}
		} else {
			if (cardListIndex > 0) {
				cardListIndex--;
			}
			else {
				Toast.makeText(getApplicationContext(), "Beginning of the Deck", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		
		getCard();
		return true;
	}
	
	public void moveForward(View view) {
		changeCard(true);
	}
	
	public void moveBackward(View view) {
		changeCard(false);
	}
	
	/*For TTS*/
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
	
	/*Basic shuffle which generates two random numbers that signify deck positions and switches them.
	 * Takes O(n) time because the shuffle lasts 4n long in order to make adequate switching
	 * of cards.
	 */
	private void shuffle() {
		Random rand1 = new Random();
		
		for (int i=0; i<cardList.size()*4; i++) {
			int index1 = rand1.nextInt(cardList.size());
			CardModel temp = new CardModel(cardList.get(index1));
			int index2 = rand1.nextInt(cardList.size());
			cardList.get(index1).copy(cardList.get(index2));
			cardList.get(index2).copy(temp);
		}
		cardListIndex = 0;
		getCard();
	}
	
	/*Deletes the current card. If this is the last card in the stack, then the 
	 * stack is also deleted (because it is empty after this deletion).
	 */
	private void delete() {
		
		SmartDBAdapter db = new SmartDBAdapter(this);
		db.open();
		
		try {
			int success = db.deleteCard(cardList.get(cardListIndex));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		cardList.remove(cardListIndex);
		if (changeCard(true)) {
			getCard();
		} else {
			changeCard(false);
			getCard();
		}
		
		db.close();
		
	}
	
	/*Sorts the cards alphabetically based on the card's titles, and then resets 
	 * the position back to the beginning. 
	 */
	private void alphabetize() {
		Collections.sort(cardList, new CustomComparator());
		cardListIndex = 0;
		
		getCard();
	}
	 /*CustomComparator used to compare cards based on their titles*/
	
	private void copy() {
		Intent intent = new Intent(this, StackMenu.class);
		intent.putExtra("stack", stack);
		
		startActivityForResult(intent, MY_STACK_DATA_CHECK_CODE);
	}
	
	public class CustomComparator implements Comparator<CardModel> {

		@SuppressLint("DefaultLocale")
		@Override
		public int compare(CardModel lhs, CardModel rhs) {
			// TODO Auto-generated method stub
			return lhs.getTitle().toLowerCase()
					.compareTo(rhs.getTitle().toLowerCase());
		}
		
	}
	
	/*Saves the position and view of the screen when the screen changes axis*/
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
		
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
	        if (requestCode == MY_TTS_DATA_CHECK_CODE) {
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
	        if(requestCode == MY_STACK_DATA_CHECK_CODE && resultCode == RESULT_OK) {
	        	SmartDBAdapter db = new SmartDBAdapter(this);
	        	db.open();
	        	
	        	String title = titleTxt.getText().toString();
	        	String definition = defTxt.getText().toString();
	        	String copyStacks = data.getStringExtra("stack");
	        	String[] stacks = splitStacks(copyStacks);
				
				long insTester[] = new long[stacks.length];
				for(int i=0; i < stacks.length; i++) {
					if (stacks[i].equals("")) {
					}else if (db.matchCard(title, definition, stacks[i])) {
						insTester[i] = -1;
					} else 
						insTester[i] = db.insertCard(title, definition, stacks[i]);
				}
				
				for (int i = 1; i < insTester.length; i++) {
					if (insTester[i] == -1 && !stacks[i].equals("")) 
						Toast.makeText(getApplicationContext(), "Not inserted in " + stacks[i],
								500).show();
				}	  
	        }
    }
	
	private String[] splitStacks(String s) {
		String[] stacks = s.split(";");
		for (String string:stacks) {
			string = string.trim();
		}
		return stacks;
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

    /*Used for touch motion. Move left to go forward, right to go backward, up to
     * flip the card. Included buffers in there so that there is room for user error
     * when making a swipe. 
     * (non-Javadoc)
     * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
     */
	@Override
	public boolean onFling(MotionEvent start, MotionEvent finish, float velocityX,
			float velocityY) {
		
		float xBndUpr = start.getRawX() + 75;
		float xBndLwr = start.getRawX() - 75;
		float yBndUpr = start.getRawY() + 125;
		float yBndLwr = start.getRawY() - 125;
		
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
		 mMode = startActionMode(new AnActionModeOfEpicProportions());
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

	/*Flips the card*/
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



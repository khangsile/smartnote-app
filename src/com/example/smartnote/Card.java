package com.example.smartnote;

import java.util.Locale;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class Card extends SherlockActivity implements OnInitListener, OnGestureListener {
	private GestureDetector gestureDetector;
			
	private static final String FLIP_TAB = "flipperTab";
	private static final String VIEW = "view";
	private String stack;
	
	private int MY_TTS_DATA_CHECK_CODE = 0;
	private int MY_STACK_DATA_CHECK_CODE = 1;
	private int MY_EDIT_DATA_CHECK_CODE = 2;
	
	private static final int EDIT = 1;
	private static final int DELETE = 2;
	private static final int COPY = 3;
	private static final int SHUFFLE = 4;
	private static final int ALPHABETIZE = 5;
	private static final int SWITCH_VIEW = 6;
	private static final int MANAGE_CARDS = 7;
	private static final int HOME = 8;
	private boolean SINGLE_VIEW;
	
	private TextView defTxt, titleTxt, indexTxt;
	private ViewFlipper flipper;
	private TextToSpeech myTTS;
	
	private Deck deck;
	
	ActionMode mMode;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.card);
        
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		initialize();
		toChinaCat();
		
		gestureDetector = new GestureDetector(this);
		
		displayCard();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0, HOME, 0, "Home")
    	.setIcon(R.drawable.ic_menu_home)
    	.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
		searchView.setQueryHint("Search");
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				if (deck.findCard(query))
					displayCard();
				else
					Toast.makeText(getApplicationContext(), "Match not found!", 250).show();
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		
		menu.add("Search")
        .setIcon(R.drawable.ic_menu_search)
        .setActionView(searchView)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		
    	SubMenu subMenu1 = menu.addSubMenu("Navigation");
        subMenu1.add("Stacks Gallery")
        	.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					// TODO Auto-generated method stub
					Intent sgIntent = new Intent(getApplicationContext(), StacksGallery.class);
					startActivity(sgIntent);
					return true;
				}
        		
        	});
        subMenu1.add("New Card")
        	.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        		public boolean onMenuItemClick(MenuItem item) {
					Intent ccIntent = new Intent(getApplicationContext(), CardCreator.class);
					startActivity(ccIntent);
        			return true;	
        		}
        	});
        
    	MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setIcon(R.drawable.abs__ic_menu_moreoverflow_normal_holo_dark);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    	
	    return true;
	  }
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case HOME:
			Intent home = new Intent(getApplicationContext(), SmartNoteActivity.class);
			startActivity(home);
			break;
		default:
			break;
		}
		
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
            	.setIcon(R.drawable.ic_menu_share)
            	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

                        
            menu.add(0, SHUFFLE, 0, "Shuffle")
            	.setIcon(R.drawable.ic_menu_shuffle)
            	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            
            menu.add(0, ALPHABETIZE, 0, "Alphabetize")
            	.setIcon(R.drawable.ic_menu_mark)
            	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            
            menu.add(0, SWITCH_VIEW, 0, "Switch View")
            	.setIcon(R.drawable.ic_menu_copy_light)
            	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            
            menu.add(0, MANAGE_CARDS, 0, "Manage Cards")
            	.setIcon(R.drawable.ic_menu_archive)
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
				edit();
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
			case SWITCH_VIEW:
				SINGLE_VIEW = !SINGLE_VIEW;
				displayCard();
				break;
			case MANAGE_CARDS:
				Intent mCards = new Intent(getApplicationContext(), CardManager.class);
				mCards.putExtra("stack", stack);
				startActivity(mCards);
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
    	
    	SINGLE_VIEW = false;
		
		Bundle extras = getIntent().getExtras();
		stack = extras.getString("stack");
				
		deck = new Deck(stack, this);
		
		if (extras.containsKey("card")) {
			CardModel card = (CardModel) extras.getParcelable("card");
			deck.moveTo(card);
		}
		
		titleTxt = (TextView)findViewById(R.id.titleTxt);
		defTxt = (TextView)findViewById(R.id.defTxt);
		indexTxt = (TextView)findViewById(R.id.index);
				
		flipper = (ViewFlipper)findViewById(R.id.cardFlipper);
		
		Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_TTS_DATA_CHECK_CODE);
	}
	
	private void displayCard() {
		
		try {		
			CardModel card = deck.getCard();
			
			if (!SINGLE_VIEW) {
				titleTxt.setText(card.getTitle());
				defTxt.setText(card.getDef());	
			}
			else {
				defTxt.setText(card.getTitle() + " - " + card.getDef());
				titleTxt.setText(card.getTitle());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
		indexTxt.setText(deck.getIndex() +"/"+(deck.getSize()-1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void toChinaCat() {
		
		Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
		
		titleTxt.setTypeface(chinacat);
		defTxt.setTypeface(chinacat);
		indexTxt.setTypeface(chinacat);
	}
	
	public void moveForward(View view) {
		if (deck.changeCard(true))
			displayCard();
		else
			Toast.makeText(this, "End of Stack!", 100).show();
	}
	
	public void moveBackward(View view) {
		if (deck.changeCard(false))
			displayCard();
		else
			Toast.makeText(this, "Beginning of Stack!", 100).show();
	}
	
	/*For TTS*/
	public void talk(View view) {
		int flipperView = flipper.getDisplayedChild();
		int flipperTitle = flipper.indexOfChild(titleTxt);
				
		if (flipperTitle == flipperView) {
			String title = titleTxt.getText().toString();
			readCard(title);
		} else {
			String def = defTxt.getText().toString();
			readCard(def);
		}
	}
	
	private void readCard(String phrase) {
		try {
		myTTS.speak(phrase, TextToSpeech.QUEUE_FLUSH, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*Basic shuffle which generates two random numbers that signify deck positions and switches them.
	 * Takes O(n) time because the shuffle lasts 4n long in order to make adequate switching
	 * of cards.
	 */
	private void shuffle() {
		deck.shuffle();
		displayCard();
	}
	
	/*Deletes the current card. If this is the last card in the stack, then the 
	 * stack is also deleted (because it is empty after this deletion).
	 */
	private void delete() {
		if (!deck.delete(this))
			displayCard();
		else
			finish();
	}
	/*Sorts the cards alphabetically based on the card's titles, and then resets 
	 * the position back to the beginning. 
	 */
	private void alphabetize() {
		try {
			deck.alphabetize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		displayCard();
	}
	
	private void copy() {
		Intent intent = new Intent(this, StackMenu.class);
		intent.putExtra("stack", stack);
		
		startActivityForResult(intent, MY_STACK_DATA_CHECK_CODE);
	}
	
	private void edit() {
		Intent intent = new Intent(this, CardEditor.class);
		intent.putExtra("stack", stack);
		CardModel card = new CardModel(deck.getCard());
		intent.putExtra("Card", card);
		
		startActivityForResult(intent, MY_EDIT_DATA_CHECK_CODE);
	}
		
	/*Saves the position and view of the screen when the screen changes axis*/
	public void onSaveInstanceState(Bundle savedInstanceState) {	
		savedInstanceState.putInt(FLIP_TAB, flipper.getDisplayedChild());	
		savedInstanceState.putBoolean(VIEW, SINGLE_VIEW);
		super.onSaveInstanceState(savedInstanceState);
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		int flipperPos = savedInstanceState.getInt(FLIP_TAB);						
		flipper.setDisplayedChild(flipperPos);
		SINGLE_VIEW = savedInstanceState.getBoolean(VIEW);
		
		try {
			deck = (Deck) getLastNonConfigurationInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		displayCard();
	}
	
	public Object onRetainNonConfigurationInstance() {
	  	return deck;
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
	        	
	        	String title = deck.getCard().getTitle();
	        	String definition = deck.getCard().getDef();
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
	        
	        if(requestCode == MY_EDIT_DATA_CHECK_CODE && resultCode == RESULT_OK) {
	        	CardModel card = data.getParcelableExtra("Card");
	        	
	        	deck.getCard().setTitle(card.getTitle());
	        	deck.getCard().setDef(card.getDef());
	        	
	        	displayCard();    	
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
				 deck.changeCard(false);
			else if (finish.getRawX() < xBndLwr)
				 deck.changeCard(true);
			displayCard();
		} else if (finish.getRawY() < start.getRawY() + 150)
			 flipper.showNext();
		else if (start.getRawY() < finish.getRawY()-150)
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
	
	public void onBackPressed() {
		Intent intent = new Intent(this, ModeChooser.class);
		intent.putExtra("stack", stack);
		startActivity(intent);
	}
	
}



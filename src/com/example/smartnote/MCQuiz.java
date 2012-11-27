package com.example.smartnote;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MCQuiz extends Activity {
	
	private String answer;
	private Queue<CardModel> cardQueue;
	private List<CardModel> cardList, answers;
	private TextView question;
	private RadioButton[] choices;
	
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mcquiz);
		
		initialize();
		
		if (cardList.size() <= 4) {
			Toast.makeText(getApplicationContext(), "Sorry! This feature requires \nmore than 4 cards in your deck.",500).show();
			finish();
		} else {
			toChinaCat();
			
			cardQueue = (Queue<CardModel>) getLastNonConfigurationInstance();
		    if (cardQueue == null) 
		    	cardQueue = shuffle(new ArrayList<CardModel>(cardList));
			
			getCard();
			
			answers = new ArrayList<CardModel>();
			getChoices(new ArrayList<CardModel>(cardList));
		}
	}
	
	public Object onRetainNonConfigurationInstance() {
	    return cardQueue;
	}
	
	public void initialize() {
		Bundle extras = getIntent().getExtras();
		String stack = extras.getString("stack");
		
		SmartDBAdapter db = new SmartDBAdapter(this);
		db.open();
			
		cardList = new ArrayList<CardModel>();
		cardList = db.getItems(stack);
		
		db.close();
			
		question = (TextView) findViewById(R.id.question);
		
		choices = new RadioButton[4];
		choices[0] = (RadioButton) findViewById(R.id.a);
		choices[1] = (RadioButton) findViewById(R.id.b);
		choices[2] = (RadioButton) findViewById(R.id.c);
		choices[3] = (RadioButton) findViewById(R.id.d);
		
		
	}
	
	private void toChinaCat() {
		Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
		choices[0].setTypeface(chinacat);
		choices[1].setTypeface(chinacat);
		choices[2].setTypeface(chinacat);
		choices[3].setTypeface(chinacat);
		question.setTypeface(chinacat);
			
	}
	
	/*Shuffles the cards and outputs an order for the cards to be quizzed in through
	 * the use of a queue. This means that you cannot move backwards in the list
	 * and the queue is fixed. 
	 */
	private Queue<CardModel> shuffle(List<CardModel> cardListCopy) {
		Queue<CardModel> cardQueue = new LinkedList<CardModel>();
		
		for(int i = cardListCopy.size(); i > 1; i--) {
			Random numberGen = new Random();
			int shuffler = numberGen.nextInt(i);
			cardQueue.add(cardListCopy.remove(shuffler));
		}		
		cardQueue.add(cardListCopy.get(0));
		
		return cardQueue;
	}
	
	private void getCard() {
		String definition = cardQueue.peek().getDef();
		question.setText(definition);
	}
	
	/*Gets the choices by randomly selecting four cards from the list*/
	private void getChoices(List<CardModel> cardListCopy) {		
		answers.add(cardQueue.peek());
		
		Random numGen = new Random();
		for (int i=0;i<3;i++) {
			int shuffle = numGen.nextInt(cardListCopy.size());
			if (!cardListCopy.get(shuffle).getTitle().equals(answers.get(0).getTitle()))
				answers.add(cardListCopy.remove(shuffle));
			else
				i--;
		}
		Queue<CardModel> mixAnswers = new LinkedList<CardModel>();
		mixAnswers = shuffle(answers);
		
		for (int i=0; i < 3; i++) {
			choices[i].setText(mixAnswers.remove().getTitle());
		}
		choices[3].setText(mixAnswers.peek().getTitle());
		
		answers.clear();
	}
	
	public void onRadioButtonClicked(View view) {
			int selectedid = view.getId();
	        answer = ((RadioButton)findViewById(selectedid)).getText().toString();
	}
	
	public void mcAnswer(View view) {
		if (cardQueue.size() > 1) {
			if (answer.equals(cardQueue.peek().getTitle())) {
				Toast.makeText(getApplicationContext(), "Correct!", 500).show();
				cardQueue.remove();
			} else {
				Toast.makeText(getApplicationContext(), "Answer is " + 
						cardQueue.peek().getTitle(), 500).show();
				cardQueue.offer(cardQueue.remove());
				} 
			} else {
				finish();
			}
		getCard();
		getChoices(new ArrayList<CardModel>(cardList));
		RadioGroup rgButton = (RadioGroup)findViewById(R.id.rGroup);
		rgButton.clearCheck();
	}

}

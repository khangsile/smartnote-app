package com.example.smartnote;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import android.content.Context;

public class Deck {
	
	private static final int QUIZ_MODE = 1;
	private static final int REVIEW_MODE = 0;
	
	private List<CardModel> cards;
	private Queue<CardModel> cardQueue;
	private int cardListIndex;
	private int mode;
	
	public Deck(String stack, int mode, Context context) {
		
		SmartDBAdapter db = new SmartDBAdapter(context);
		db.open();
		
		cards = new ArrayList<CardModel>();
		cards = db.getItems(stack);
		
		db.close();	
		
		cardListIndex = 0;
		this.mode = mode;
	}
	
	public void shuffle() {
			
			cardQueue = new LinkedList<CardModel>();
			
			for(int i = cards.size(); i > 1; i--) {
				Random numberGen = new Random();
				int shuffler = numberGen.nextInt(i);
				cardQueue.add(cards.remove(shuffler));
			}		
			cardQueue.add(cards.get(0));
	}
	
	public CardModel getCard() {
		
		if (mode == REVIEW_MODE) {
			return cards.get(cardListIndex);
		} else if (mode == QUIZ_MODE) {
			if (!cardQueue.isEmpty()) 
				return cardQueue.peek();
		}
		
		return null;	
	}


}

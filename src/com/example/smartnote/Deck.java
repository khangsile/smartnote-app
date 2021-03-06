package com.example.smartnote;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;

public class Deck {
	
	private List<CardModel> cards;
	private int index;
	
	public Deck(String stack, Context context) {
		
		SmartDBAdapter db = new SmartDBAdapter(context);
		db.open();
		
		cards = new ArrayList<CardModel>();
		try {
			cards = db.getItems(stack);
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.close();	
		
		index = 0;
	}
	
	public List<CardModel> getCards() {
		return cards;
	}
	
	public int getSize() {
		return cards.size();
	}
	
	public int getIndex() {
		return index;
	}
	
	public void shuffle() {
		dShuffle(cards);	
	}
		
	public CardModel getCard() {
		
		return cards.get(index);			
	}
	
	public boolean changeCard(boolean forward) {
		
		if (forward) {
			if (index < cards.size()-1) {
				index++;
				return true;
			}
		} else {
			if (index > 0) {
				index--;
				return true;
			}
		}	
		
		return false;
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
	
	public boolean delete(Context context) {
		SmartDBAdapter db = new SmartDBAdapter(context);
		db.open();
		
		try {
			db.deleteCard(getCard());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (cards.size() <= 1)
			return true;
		cards.remove(index);
		
		try {
			if (changeCard(true)) {}
			else {
				changeCard(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		db.close();
		return false;
	}
	
	public void alphabetize() {
		Collections.sort(cards, new CustomComparator());
		index = 0;		
	}

	public List<CardModel> getChoices() {
		List<CardModel> choices = new ArrayList<CardModel>();
		
		choices.add(cards.get(index));
		Random gen = new Random();
		while (choices.size()<4) {
			int choiceIndex = gen.nextInt(cards.size());
			if (!choices.contains(cards.get(choiceIndex)))
				choices.add(cards.get(choiceIndex));
		}
		
		List<CardModel> result = new ArrayList<CardModel>();
		for (int i=0;i<3;i++) {
			int random = gen.nextInt(choices.size());
			result.add(choices.get(random));
			choices.remove(random);
		}
		result.add(choices.get(0));
		
		return result;	
	}
	
	@SuppressLint("DefaultLocale")
	public boolean findCard(String query) {
		
		if (isNumeric(query)) {
			int newIndex;
			try {
				newIndex = Integer.parseInt(query);
				if (newIndex < getSize() && newIndex >= 0) {
					index = newIndex;
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for (int i=0; i < cards.size(); i++) {
			String title = cards.get(i).getTitle().toLowerCase();
			String def = cards.get(i).getDef().toLowerCase();
			query = query.toLowerCase().trim();
			
			if (title.matches(query)|| def.matches(query)) {
				index = i;
				return true;
			}	
		}
		
		for (int i=0; i < cards.size(); i++) {
			String title = cards.get(i).getTitle().toLowerCase();
			String def = cards.get(i).getDef().toLowerCase();
			query = query.toLowerCase();
			
			if (title.contains(query)|| def.contains(query)) {
				index = i;
				return true;
			}	
		}
		
		return false;	
	}
	
	public void moveTo(CardModel card) {
		int newIndex = cards.indexOf(card);
		index = newIndex;
	}
	
	private void dShuffle(List<CardModel> list) {
		Random generator = new Random();
		for(int i=0; i<4*list.size();i++) {
			int index1 = generator.nextInt(list.size());
			int index2 = generator.nextInt(list.size());
			
			CardModel temp = new CardModel(list.get(index1));
			list.get(index1).copy(list.get(index2));
			list.get(index2).copy(temp);
		}		
	}
	
	public static boolean isNumeric(String str) {
	  NumberFormat formatter = NumberFormat.getInstance();
	  ParsePosition pos = new ParsePosition(0);
	  formatter.parse(str, pos);
	  return str.length() == pos.getIndex();
	}
}

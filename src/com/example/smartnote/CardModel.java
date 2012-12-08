package com.example.smartnote;

import android.os.Parcel;
import android.os.Parcelable;

public class CardModel implements Parcelable {
	
	private String title;
	private String definition;
	private int id, stack, hits, attempts;
	private boolean selected;
	
	public CardModel(String title, String definition, int id, int stack, int hits, int attempts) {
		this.title = title;
		this.definition = definition;
		this.id = id;
		this.stack = stack;
		this.hits = hits;
		this.attempts = attempts;
		this.selected = false;
	}
	
	public CardModel(CardModel card) {
		this.title = card.getTitle();
		this.definition = card.getDef();
		this.id = card.getId();
		this.stack = card.getStack();
		this.selected = false;
	}
	
	public CardModel(Parcel in) {
		readFromParcel(in);
	}
	
	public void copy (CardModel card) {
		this.title = card.getTitle();
		this.definition = card.getDef();
		this.id = card.getId();
		this.stack = card.getStack();
	}

	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDef() {
		return definition;
	}
	
	public void setDef(String definition) {
		this.definition = definition;
	}
	
	public int getId() {
		return id;
	}
	
	public int getStack() {
		return stack;
	}
	
	public int getHits() {
		return hits;
	}
	
	public int getAttempts() {
		return attempts;
	}
	
	public String getDifficulty() {
		if (attempts > 0) {
			int difficulty = 11-(10*hits)/attempts;
			return difficulty+"";
		} else {
			return "";
		}
	}
	
	public void correct() {
		hits++;
		attempts++;
	}
	
	public void wrong() {
		attempts++;
	}

	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean equals (Object o) {
	    CardModel x = (CardModel) o;
	        if (x.title == title && x.definition == definition) return true;
	        return false;
	    }
	
	
    public static final Parcelable.Creator<CardModel> CREATOR = new Parcelable.Creator<CardModel>() {
        public CardModel createFromParcel(Parcel in) {
            return new CardModel(in);
        }

        public CardModel[] newArray(int size) {
            return new CardModel[size];
        }
    };

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(title);
		dest.writeString(definition);
		dest.writeInt(stack);
		dest.writeInt(hits);
		dest.writeInt(attempts);
		dest.writeInt(id);
	}
	
	public void readFromParcel(Parcel in) {
		title = in.readString();
		definition = in.readString();
		stack = in.readInt();
		hits = in.readInt();
		attempts = in.readInt();
		id = in.readInt();
		selected = false;
	}

}

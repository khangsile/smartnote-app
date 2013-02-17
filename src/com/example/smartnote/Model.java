package com.example.smartnote;

import android.os.Parcel;
import android.os.Parcelable;

public class Model implements Parcelable {
	
	private String name;
	private boolean selected;
	private long count;
	private String date;
	
	public Model(String name, long count, String date) {
		this.name = name;
		this.count = count;
		this.date = date;
		selected = false;
	}
	
	public Model(Parcel in) {
		readFromParcelable(in);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setCount(long count) {
		this.count = count;
	}
	
	public long getCount() {
		return count;
	}
	
	public String getDate() {
		return date;
	}
	
	 public static final Parcelable.Creator<Model> CREATOR = new Parcelable.Creator<Model>() {

	      public Model createFromParcel(Parcel source) {
	         return new Model(source);
	      }

	      public Model[] newArray(int size) {
	         return new Model[size];
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
		dest.writeString(name);
		dest.writeLong(count);
		
	}
	
	private void readFromParcelable(Parcel in) {
		name = in.readString();
		selected = false;
		count = in.readLong();
	}

}

package com.example.smartnote;

import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class CardSearchAdapter extends ArrayAdapter<CardModel> implements SectionIndexer {
	
	private Activity context;
	private List<CardModel> list;
	
	public CardSearchAdapter(Activity context, List<CardModel> list) {
		super(context, R.layout.cardsearch, list);
		this.list = list;
		this.context = context;
	}
	
	static class ViewHolder {
	    public TextView title, definition, stack;
	  }

	
	public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    if (rowView == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(R.layout.cardsearch, null);
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.title = (TextView) rowView.findViewById(R.id.title);
	      viewHolder.definition = (TextView) rowView.findViewById(R.id.definition);
	      viewHolder.stack = (TextView) rowView.findViewById(R.id.stack);
	      rowView.setTag(viewHolder);
	    }
	    
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    holder.title.setText(list.get(position).getTitle() + "");
	    holder.definition.setText(list.get(position).getDef() + "");
	    
	    SmartDBAdapter db = new SmartDBAdapter(holder.title.getContext());
	    db.open();
	    
	    String stack = db.getStackName(list.get(position).getStack());
	    
	    db.close();
	    
	    holder.stack.setText(stack);
	    	    	    
	    Typeface chinacat = Typeface.createFromAsset(holder.title.getContext()
	            .getAssets(), "fonts/DroidSans-Bold.ttf");
	    
	    holder.title.setTypeface(chinacat);
	    holder.definition.setTypeface(chinacat);
	    holder.stack.setTypeface(chinacat);
	    
	    return rowView;
	  }


	@Override
	public int getPositionForSection(int section) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

}
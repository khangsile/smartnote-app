package com.example.smartnote;

import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class CardArrayAdapter extends ArrayAdapter<CardModel>{

	private final List<CardModel> list;
	private final Activity context;
	
	public CardArrayAdapter(Activity context, List<CardModel> list) {
		super(context, R.layout.cardlist, list);
		
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView title, correct, atts, diff, definition;
		protected CheckBox check;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
				
		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			view = inflater.inflate(R.layout.cardlist, null);
			
			final ViewHolder viewholder = new ViewHolder();
						
			viewholder.title = (TextView)view.findViewById(R.id.title);
			viewholder.correct = (TextView)view.findViewById(R.id.numCorrect);
			viewholder.atts = (TextView)view.findViewById(R.id.numAtts);
			viewholder.diff = (TextView)view.findViewById(R.id.gradePct);
			viewholder.check = (CheckBox)view.findViewById(R.id.check);
			viewholder.definition = (TextView)view.findViewById(R.id.definition);
			
			viewholder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				 public void onCheckedChanged(CompoundButton buttonView,
			                boolean isChecked) {
			              CardModel element = (CardModel) viewholder.check
			                  .getTag();
			              			  			
			              element.setSelected(buttonView.isChecked());       
				 }
			});
			
			view.setTag(viewholder);
			viewholder.check.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).check.setTag(list.get(position));
		}
		
		ViewHolder holder = (ViewHolder) view.getTag();
	    holder.title.setText(list.get(position).getTitle());
	    holder.correct.setText(list.get(position).getHits() + "");
	    holder.atts.setText(list.get(position).getAttempts() + "");
	    holder.diff.setText(list.get(position).getDifficulty());
	    holder.definition.setText(list.get(position).getDef());
	    holder.check.setChecked(list.get(position).isSelected());
	    
	    Typeface chinacat = Typeface.createFromAsset(holder.title.getContext()
	            .getAssets(), "fonts/DroidSans-Bold.ttf");
	    
	    holder.title.setTypeface(chinacat);
	    holder.diff.setTypeface(chinacat);
	    holder.correct.setTypeface(chinacat);
	    holder.atts.setTypeface(chinacat);
	    holder.definition.setTypeface(chinacat);
	    
	    TextView lCorrect = (TextView) view.findViewById(R.id.correct);
	    TextView lAttempts = (TextView) view.findViewById(R.id.attempts);
	    TextView lGrade = (TextView) view.findViewById(R.id.difficulty);
	    
	    lCorrect.setTypeface(chinacat);
	    lAttempts.setTypeface(chinacat);
	    lGrade.setTypeface(chinacat);
	    
	    
	    return view;
	}
	
	public List<CardModel> getList() {
		return this.list;
	}
}

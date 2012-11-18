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

public class InteractiveArrayAdapter extends ArrayAdapter<Model> {
	
	private final List<Model> list;
	private final Activity context;
	
	public InteractiveArrayAdapter(List<Model> list, Activity context) {
		super(context, R.layout.stackchoice, list);
		
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView text;
		protected CheckBox check;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			view = inflater.inflate(R.layout.stackchoice, null);
			
			final ViewHolder viewholder = new ViewHolder();
			
			viewholder.text = (TextView)view.findViewById(R.id.label);
			viewholder.check = (CheckBox)view.findViewById(R.id.check);
			
			viewholder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				 public void onCheckedChanged(CompoundButton buttonView,
			                boolean isChecked) {
			              Model element = (Model) viewholder.check
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
	    holder.text.setText(list.get(position).getName());
	    holder.check.setChecked(list.get(position).isSelected());
	    
	    Typeface chinacat = Typeface.createFromAsset(holder.text.getContext()
	            .getAssets(), "fonts/DroidSans-Bold.ttf");
	    
	    holder.text.setTypeface(chinacat);

	    return view;
	    
		
	}
}

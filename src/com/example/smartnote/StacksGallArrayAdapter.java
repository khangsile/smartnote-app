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

public class StacksGallArrayAdapter extends ArrayAdapter<Model> implements SectionIndexer {
	
	private Activity context;
	private List<Model> list;
	
	public StacksGallArrayAdapter(Activity context, List<Model> list) {
		super(context, R.layout.stacksgalleryrow, list);
		this.list = list;
		this.context = context;
	}
	
	static class ViewHolder {
	    public TextView title, count;
	  }

	
	public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    if (rowView == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(R.layout.stacksgalleryrow, null);
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.title = (TextView) rowView.findViewById(R.id.stack);
	      viewHolder.count = (TextView) rowView.findViewById(R.id.count);
	      rowView.setTag(viewHolder);
	    }
	    
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    holder.title.setText(list.get(position).getName() + "");
	    holder.count.setText(list.get(position).getCount() + "");
	    	    	    
	    Typeface chinacat = Typeface.createFromAsset(holder.title.getContext()
	            .getAssets(), "fonts/DroidSans-Bold.ttf");
	    
	    holder.title.setTypeface(chinacat);
	    holder.count.setTypeface(chinacat);
	    TextView diff = (TextView)rowView.findViewById(R.id.difficulty);
	    diff.setTypeface(chinacat);
	    
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

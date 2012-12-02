package com.example.smartnote;

import java.util.List;

import com.example.smartnote.InteractiveArrayAdapter.ViewHolder;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class QuizArrayAdapter extends ArrayAdapter<Quiz> {
	
	private Activity context;
	private List<Quiz> quizzes;
	
	public QuizArrayAdapter(Activity context, List<Quiz> quizzes) {
		super(context, R.layout.quizhistory, quizzes);
		this.quizzes = quizzes;
		this.context = context;
	}
	
	static class ViewHolder {
	    public TextView number, correct, grade, attempts;
	  }

	
	public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    if (rowView == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(R.layout.quizhistory, null);
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.correct = (TextView) rowView.findViewById(R.id.numCorrect);
	      viewHolder.number = (TextView) rowView
	          .findViewById(R.id.quizNumber);
	      viewHolder.grade = (TextView) rowView.findViewById(R.id.gradePct);
	      viewHolder.attempts = (TextView) rowView.findViewById(R.id.numAtts);
	      rowView.setTag(viewHolder);
	    }
	    
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    holder.correct.setText(quizzes.get(position).getCorrect() + "");
	    holder.attempts.setText(quizzes.get(position).getAtts() + "");
	    	    
	    holder.grade.setText(quizzes.get(position).getGrade() + "");
	    holder.number.setText(position + "");
	    
	    Typeface chinacat = Typeface.createFromAsset(holder.grade.getContext()
	            .getAssets(), "fonts/DroidSans-Bold.ttf");
	    
	    holder.number.setTypeface(chinacat);
	    holder.grade.setTypeface(chinacat);
	    holder.correct.setTypeface(chinacat);
	    holder.attempts.setTypeface(chinacat);
	    
	    TextView lCorrect = (TextView) rowView.findViewById(R.id.correct);
	    TextView lAttempts = (TextView) rowView.findViewById(R.id.attempts);
	    TextView lGrade = (TextView) rowView.findViewById(R.id.grade);
	    
	    lCorrect.setTypeface(chinacat);
	    lAttempts.setTypeface(chinacat);
	    lGrade.setTypeface(chinacat);


	    return rowView;
	  }

}

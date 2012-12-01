package com.example.smartnote;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class StackMenu extends ListActivity {
	
	SmartDBAdapter db;
	
	private String stack;
	
	InteractiveArrayAdapter adapter;
	
	ListView listview;
		
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stackmenu);
		setTitle("Your Stacks");
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			try {
				stack = extras.getString("stack");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			stack = "";
		}
		
		Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");
		
		Button bAddStacks = (Button) findViewById(R.id.addStacks);	
		bAddStacks.setTypeface(chinacat);
		Button bCancel = (Button) findViewById(R.id.cancel);
		bCancel.setTypeface(chinacat);
		
		@SuppressWarnings("unchecked")
		final List<Model> data = (List<Model>) getLastNonConfigurationInstance();
	    if (data != null) {
	        adapter = new InteractiveArrayAdapter(this, data);
	    } else {
				adapter = new InteractiveArrayAdapter(this, getMenuItems());
	    }
	    
	    modifyStack();
	    
		setListAdapter(adapter);
		listview = getListView();
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	public Object onRetainNonConfigurationInstance() {
	    final List<Model> data = adapter.getList();
	    return data;
	}
	
	private List<Model> getMenuItems() {
		
		db = new SmartDBAdapter(this);
		db.open();
		
		List<Model> list = new ArrayList<Model>();
		list = db.getStacks();
		
		db.close();
		return list;
	}
	
	public void cancel(View view) {		
		setResult(RESULT_CANCELED);
		finish();
	}
	
	public void addStacks(View view) {
	
		List<Model> models = adapter.getList();
				
		for (int i=0; i < models.size(); i++) {
			if (models.get(i).isSelected()==true) {
				if (stack.equals("")) 
					stack = models.get(i).getName();
				else
					stack = stack + ";" + models.get(i).getName();
			}
		}
		
		Intent intent = getIntent();
		intent.putExtra("stack", stack);
		
		setResult(RESULT_OK,intent);
		finish();
	}
	
	public void modifyStack() {
		String[] stackNames = stack.split(";");
		for (String stackName: stackNames) 
			stackName.trim();
		
		List<Model> models = adapter.getList();
		for (int i = 0; i < stackNames.length; i++) {
			for (int j = 0; j < models.size(); j++) {
				if (stackNames[i].equals(models.get(j).getName())) {
					models.get(j).setSelected(true);
					stackNames[i] = "";
				}
			}
		}
		
		stack = "";
		
		for (int i=0; i < stackNames.length; i++) {
			if (!stackNames[i].equals(""))
				stack = stack + ";" + stackNames[i];
		}
		
	}
}

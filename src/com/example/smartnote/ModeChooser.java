package com.example.smartnote;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*Choose your modes in this activity. Allows you to choose between review, multiple
 * choice, memory (fill-in), and also allows you to manage your stack and look
 * at the stats.
 */
public class ModeChooser extends SherlockActivity {
	
	private Button bMemQuiz, bReview, bMcQuiz;
	private TextView tChoose;
	private String stack;
	
	private static final int DELETE = 1;
	private static final int QUIZ = 2;
	private static final int MANAGE = 3;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modechooser);
		
		Bundle extras = getIntent().getExtras();
		stack = extras.getString("stack");
		
		bMemQuiz = (Button)findViewById(R.id.memQuiz);
		bReview = (Button)findViewById(R.id.review);
		bMcQuiz = (Button)findViewById(R.id.mcQuiz);
		tChoose = (TextView)findViewById(R.id.modeChoice);
		
        Typeface chinacat = Typeface.createFromAsset(getAssets(), "fonts/DroidSans-Bold.ttf");

		tChoose.setTypeface(chinacat);
        bMemQuiz.setTypeface(chinacat);
        bReview.setTypeface(chinacat);
        bMcQuiz.setTypeface(chinacat);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0, DELETE, 0, "Delete")
		.setIcon(R.drawable.ic_menu_delete)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		menu.add(0, QUIZ, 0, "History")
		.setIcon(R.drawable.ic_menu_copy_holo_dark)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		menu.add(0, MANAGE, 0, "Manage")
		.setIcon(R.drawable.ic_menu_archive)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		SubMenu subMenu1 = menu.addSubMenu("Navigation");
        subMenu1.add("Stacks Gallery")
        	.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					// TODO Auto-generated method stub
					Intent sgIntent = new Intent(getApplicationContext(), StacksGallery.class);
					startActivity(sgIntent);
					return true;
				}
        		
        	});
        subMenu1.add("New Card")
        	.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        		public boolean onMenuItemClick(MenuItem item) {
					Intent ccIntent = new Intent(getApplicationContext(), CardCreator.class);
					startActivity(ccIntent);
        			return true;	
        		}
        	});
        
        MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setIcon(R.drawable.abs__ic_menu_moreoverflow_normal_holo_dark);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE:
			SmartDBAdapter db = new SmartDBAdapter(this);
			db.open();
			db.deleteStack(stack);
			
			Intent intent = new Intent(this, StacksGallery.class);
			startActivity(intent);
			break;
		case QUIZ:
			Intent qhIntent = new Intent(this, QuizHistory.class);
			qhIntent.putExtra("stack", stack);
			startActivity(qhIntent);
			break;
		case MANAGE:
			Intent cmIntent = new Intent(this, CardManager.class);
			cmIntent.putExtra("stack", stack);
			startActivity(cmIntent);
			break;
		default:
			break;
		}
		
		return true;
	}
	
	public void toReview(View view) {
		Intent intent = new Intent(this, Card.class);
		intent.putExtra("stack", stack);
		
		startActivity(intent);
	}
	
	public void toMemQuiz(View view) {
		Intent intent = new Intent(this, MemQuiz.class);
		intent.putExtra("stack", stack);
		
		startActivity(intent);		
	}
	
	public void toMcQuiz(View view) {
		Intent intent = new Intent(this, MCQuiz.class);
		intent.putExtra("stack", stack);
		
		startActivity(intent);		

	}
	
	public void onBackPressed() {
		Intent intent = new Intent(this, StacksGallery.class);
		startActivity(intent);
	}
	
}

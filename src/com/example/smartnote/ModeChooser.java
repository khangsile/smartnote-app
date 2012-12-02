package com.example.smartnote;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*Choose your modes in this activity. Allows you to choose between review, multiple
 * choice, memory (fill-in), and also allows you to manage your stack and look
 * at the stats.
 */
public class ModeChooser extends Activity {
	
	private Button bMemQuiz, bReview, bMcQuiz;
	private TextView tChoose;
	private String stack;
	
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
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.stackmanager, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.deleteStack:
			
			break;
		case R.id.quizHistory:
			Intent qhIntent = new Intent(this, QuizHistory.class);
			qhIntent.putExtra("stack", stack);
			startActivity(qhIntent);
			break;
		case R.id.manageCards:
			Intent cmIntent = new Intent(this, CardManager.class);
			cmIntent.putExtra("stack", stack);
			startActivity(cmIntent);
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

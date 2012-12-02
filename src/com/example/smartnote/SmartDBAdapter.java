package com.example.smartnote;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SmartDBAdapter {

	public static final String CARD_TABLE = "Cards";
	public static final String TITLE = "title";
	public static final String STACK = "deck";
	public static final String DEFINITION = "definition";
	public static final String STACK_TABLE = "Stacks";
	public static final String STACK_ID = "stackID";
	public static final String STACK_NAME = "stackName";
	public static final String ROW_ID = "_id";
	public static final String HITS = "hits";
	public static final String ATTEMPTS = "attempts";
	public static final String MEM_STATS_TABLE = "MemStats";
	public static final String MC_STATS_TABLE = "McStats";
	
	public static final String DB_NAME = "db_notecard";
	public static final int DB_VER = 8;
	
	private static final String CARD_CREATE = "CREATE TABLE Cards (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"title TEXT NOT NULL, definition TEXT NOT NULL, " + "hits INTEGER NOT NULL, " + 
			"attempts INTEGER NOT NULL, " + "deck INTEGER NOT NULL " + 
			",FOREIGN KEY (deck) REFERENCES " + STACK_TABLE + " (stackID));";
	
	private static final String STACK_CREATE = "Create TABLE " + STACK_TABLE + " (stackID INTEGER PRIMARY KEY AUTOINCREMENT, " +
			STACK_NAME + " TEXT NOT NULL);";
	
	private static final String MC_STATS_CREATE = "CREATE TABLE McStats (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"hits INTEGER NOT NULL, attempts INTEGER NOT NULL, deck INTEGER NOT NULL , FOREIGN KEY " +
			"(deck) REFERENCES " + STACK_TABLE + " (stackID));";
	
	private static final String MEM_STATS_CREATE = "CREATE TABLE MemStats (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"hits INTEGER NOT NULL, attempts INTEGER NOT NULL, deck INTEGER NOT NULL , FOREIGN KEY " +
			"(deck) REFERENCES " + STACK_TABLE + " (stackID));";
	
	private static final String VIEW_CARDS = "viewCards";
	
	private static class SmartDatabaseHelper extends SQLiteOpenHelper {
		
		SmartDatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VER);
		}
		
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(STACK_CREATE);
			db.execSQL(CARD_CREATE);
			db.execSQL(MEM_STATS_CREATE);
			db.execSQL(MC_STATS_CREATE);
			
			db.execSQL("CREATE TRIGGER fk_empstack_stackid " +
				    " BEFORE INSERT "+
				    " ON "+CARD_TABLE+
				    
				    " FOR EACH ROW BEGIN"+
				    " SELECT CASE WHEN ((SELECT "+STACK_ID+" FROM "+STACK_TABLE+
				    " WHERE "+STACK_ID+"=new."+STACK+" ) IS NULL)"+
				    " THEN RAISE (ABORT,'Foreign Key Violation') END;"+
				    "  END;");
			
			db.execSQL("CREATE TRIGGER fk_empstack_stackid_mc " +
				    " BEFORE INSERT "+
				    " ON "+MC_STATS_TABLE+
				    
				    " FOR EACH ROW BEGIN"+
				    " SELECT CASE WHEN ((SELECT "+STACK_ID+" FROM "+STACK_TABLE+
				    " WHERE "+STACK_ID+"=new."+STACK+" ) IS NULL)"+
				    " THEN RAISE (ABORT,'Foreign Key Violation') END;"+
				    "  END;");
			
			db.execSQL("CREATE TRIGGER fk_empstack_stackid_mem " +
				    " BEFORE INSERT "+
				    " ON "+MEM_STATS_TABLE+
				    
				    " FOR EACH ROW BEGIN"+
				    " SELECT CASE WHEN ((SELECT "+STACK_ID+" FROM "+STACK_TABLE+
				    " WHERE "+STACK_ID+"=new."+STACK+" ) IS NULL)"+
				    " THEN RAISE (ABORT,'Foreign Key Violation') END;"+
				    "  END;");


			
			db.execSQL("CREATE VIEW "+VIEW_CARDS+
				    " AS SELECT "+CARD_TABLE+"."+ROW_ID+" AS _id,"+
				    " "+CARD_TABLE+"."+TITLE+","+
				    " "+CARD_TABLE+"."+DEFINITION+","+
				    " "+CARD_TABLE+"."+HITS+","+
				    " "+CARD_TABLE+"."+ATTEMPTS+","+
				    " "+STACK_TABLE+"."+STACK_NAME+""+
				    " FROM "+CARD_TABLE+" JOIN "+STACK_TABLE+
				    " ON "+CARD_TABLE+"."+STACK+" ="+STACK_TABLE+"."+STACK_ID);	
		}
		
		public void onUpgrade(SQLiteDatabase db, int oldVers, int newVers) {
			db.execSQL("DROP TABLE IF EXISTS " + CARD_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + STACK_TABLE);
			
			db.execSQL("DROP VIEW IF EXISTS " + VIEW_CARDS);
			onCreate(db);
		}
		
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
			
			if(db.isReadOnly()) {
				db.execSQL("PRAGMA foreign_keys = ON;");
			}
		}
	}
	
	private final Context context;	
    private SmartDatabaseHelper smartdb;
    private SQLiteDatabase db;
    
    public SmartDBAdapter(Context context) {
    	this.context = context;
    }
    
    public SmartDBAdapter open() throws SQLException {
    	smartdb = new SmartDatabaseHelper(context);
    	    	
    	db = smartdb.getWritableDatabase();
    	
    	return this;
    }
    
    public void close() {
    	smartdb.close();
    }
    
    public long insertCard(String title, String definition, String stack) {
    	int stackID = getStackID(stack);
    	
    	ContentValues values = new ContentValues();
    	values.put(STACK, stackID);
    	values.put(TITLE, title);
    	values.put(DEFINITION, definition);
    	values.put(HITS, 0);
    	values.put(ATTEMPTS, 0);
    	return db.insert(CARD_TABLE, null, values);
    }
    
    public long insertStack(String newStack) {
    	ContentValues values = new ContentValues();
    	values.put(STACK_NAME, newStack);
    	
    	return db.insert(STACK_TABLE, STACK_ID, values);
    }
    
    public boolean matchCard(String title, String definition, String stackName) {
    	int stackID = getStackID(stackName);
    	Cursor cursor = db.query(CARD_TABLE, new String[] { TITLE }, 
    			TITLE+"=? AND "+DEFINITION+"=? AND " + STACK+"=?", new String[] { title, definition, 
    			String.valueOf(stackID)}, null, null, null);
    	    	
    	if (cursor!=null && cursor.getCount() == 1) 
    		return true;
    	
    	return false;
    	
    }
    
    public boolean matchStack(String stack) {
    	Cursor cursor = db.query(STACK_TABLE, new String[] {STACK_NAME},
    			STACK_NAME+"=?", new String[] {stack}, null, null, null);
    	
    	if (cursor!=null && cursor.getCount() == 1)
    		return true;
    	
    	return false;
    }
    
    public int getStackID(String stackName) {
    	SQLiteDatabase db = smartdb.getReadableDatabase();
    	Cursor cursor = db.query(STACK_TABLE, new String[]{STACK_ID + " as _id", STACK_NAME}, 
    			STACK_NAME + "=?",  new String[] {stackName}, null, null, null); 
    	    	    		
    	cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("_id"));
    	 
    }
    
    public List<CardModel> getItems(String stack) {
    	Cursor cursor;
    	
    	if (stack.equals("")) {
    	
    		cursor = db.query("Cards", new String[] { ROW_ID, TITLE, DEFINITION }, null,
    			null, null, null, null); 
    	} else {
    		int stackID = getStackID(stack);
    		cursor = db.query("Cards", new String[] { ROW_ID, TITLE, DEFINITION, HITS, ATTEMPTS, STACK}, 
    				STACK+"=?", new String[] {String.valueOf(stackID)}, null, null, null);
    	}
    	
    	int defIndex = cursor.getColumnIndex(DEFINITION);
		int titleIndex = cursor.getColumnIndex(TITLE);
		int idIndex = cursor.getColumnIndex(ROW_ID);
		int stackIndex = cursor.getColumnIndex(STACK);
		int hitIndex = cursor.getColumnIndex(HITS);
		int attIndex = cursor.getColumnIndex(ATTEMPTS);
				
		cursor.moveToFirst();
			
		List<CardModel> cardList = new ArrayList<CardModel>();
		
		while (!cursor.isAfterLast()) {
			
			String title = cursor.getString(titleIndex);
			String definition = cursor.getString(defIndex);
			int id = cursor.getInt(idIndex);
			int stackID = cursor.getInt(stackIndex);
			int hits = cursor.getInt(hitIndex);
			int att = cursor.getInt(attIndex);
			cardList.add(new CardModel(title, definition, id, stackID, hits, att));
			
			cursor.moveToNext();
		}
		return cardList;
    }
    
    public List<Model> getStacks() {
    	Cursor cursor = db.query(STACK_TABLE, new String[] {STACK_ID, STACK_NAME}, null, 
    			null, null, null, null);
    	
		int stackNameIndex = cursor.getColumnIndex(STACK_NAME);
		cursor.moveToFirst();
		
		List<Model> list = new ArrayList<Model>();
		
		while(!cursor.isAfterLast()) {
			String name = cursor.getString(stackNameIndex);
			list.add(new Model(name));
			
			cursor.moveToNext();
		}
		
		return list;
    }
    
    public List<Quiz> getMemQuiz(String stack) {
 
    	int stackID = getStackID(stack);
    	
    	Cursor cursor = db.query(MEM_STATS_TABLE, new String[] {ROW_ID, HITS, ATTEMPTS}, STACK + "=?",
    			new String[] {String.valueOf(stackID)}, null, null, null);
    	
    	int hitIndex = cursor.getColumnIndex(HITS);
    	int attIndex = cursor.getColumnIndex(ATTEMPTS);
    	
    	List<Quiz> list = new ArrayList<Quiz>();
    	
    	cursor.moveToFirst();
    	
    	while(!cursor.isAfterLast()) {
    		int hits = cursor.getInt(hitIndex);
    		int atts = cursor.getInt(attIndex);
    		
    		list.add(new Quiz(hits, atts));
    		cursor.moveToNext();
    	}
    	
    	return list;

    }
    
    public List<Quiz> getMcQuiz(String stack) {
    	
    	int stackID = getStackID(stack);
    	Cursor cursor = db.query(MC_STATS_TABLE, new String[] {ROW_ID, HITS, ATTEMPTS}, STACK + "=?",
    			new String[] {String.valueOf(stackID)}, null, null, null);
    	
    	int hitIndex = cursor.getColumnIndex(HITS);
    	int attIndex = cursor.getColumnIndex(ATTEMPTS);
    	
    	List<Quiz> list = new ArrayList<Quiz>();
    	
    	cursor.moveToFirst();
    	
    	while(!cursor.isAfterLast()) {
    		int hits = cursor.getInt(hitIndex);
    		int atts = cursor.getInt(attIndex);
    		
    		list.add(new Quiz(hits, atts));
    	}
    	
    	return list;
    }
    
    public int updateCard(CardModel card) {
    	ContentValues cv=new ContentValues();
    	cv.put(DEFINITION, card.getDef());
    	cv.put(STACK, card.getStack());
    	cv.put(TITLE, card.getTitle());
    	cv.put(HITS, card.getHits());
    	cv.put(ATTEMPTS, card.getAttempts());
    	return db.update(CARD_TABLE, cv, ROW_ID+"=?", 
    			new String []{String.valueOf(card.getId())});   
    }
    
    public int deleteCard(CardModel card) {
    	return db.delete(CARD_TABLE, ROW_ID+"=?", new String [] {String.valueOf(card.getId())});
    }
    
    public int deleteStack(String stackName) {
    	return db.delete(STACK_TABLE, STACK_NAME+"=?", new String [] {stackName});
    }
    
    public long insertMCTest(int hits, int attempts, String stack) {
    	int stackID = getStackID(stack);
    	
    	ContentValues cv = new ContentValues();
    	cv.put(HITS, hits);
    	cv.put(ATTEMPTS, attempts);
    	cv.put(STACK, stackID);
    	return db.insert(MC_STATS_TABLE, null, cv);
    }
    
    public long insertMemTest(int hits, int attempts, String stack) {
    	int stackID = getStackID(stack);
    	
    	ContentValues cv = new ContentValues();
    	cv.put(HITS, hits);
    	cv.put(ATTEMPTS, attempts);
    	cv.put(STACK, stackID);
    	return db.insert(MEM_STATS_TABLE, null, cv);
    }

}

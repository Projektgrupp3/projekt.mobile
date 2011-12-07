package tddd36.grupp3.database;

import java.util.ArrayList;
import java.util.Observable;
import java.util.regex.Pattern;

import tddd36.grupp3.resources.Contact;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.resources.MapObject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

public class ClientDatabaseManager extends Observable{
	private SQLiteDatabase db; // a reference to the database manager class.
	static Context context;
	private final String DB_NAME = "client_database"; // the name of our database
	private final int DB_VERSION = 1; // the version of the database
	private boolean firstRun = true;
	// the names for our database columns
	private final String[] TABLE_NAME = {"map","mission","contacts"};
	private final String[] TABLE_MAP = {"type","mapobject"};
	private final String[] TABLE_MISSION = {"type","ID","event"};
	private final String[] TABLE_CONTACT = {"name","address"};

	public ClientDatabaseManager(Context context){
		ClientDatabaseManager.context = context;

		// create or open the database
		CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
		db = helper.getWritableDatabase();
	}

	/**
	 * Method for adding MapObject to database.
	 * @param o
	 */
	public void addRow(MapObject o)
	{
		ContentValues values = new ContentValues();
		Gson gson = new Gson();

		values.put(TABLE_MAP[0], o.getClass().getName());
		values.put(TABLE_MAP[1], gson.toJson(o));		

		// ask the database object to insert the new data 
		try
		{
			db.insert(TABLE_NAME[0], null, values);
		}
		catch(Exception e)
		{
			Log.e("DB ERROR", e.toString()); // prints the error message to the log
			e.printStackTrace(); // prints the stack trace to the log
		}
	}
	/**
	 * Method for adding current mission to database
	 * @param c
	 */
	public void addRow(Event ev)
	{
		ContentValues values = new ContentValues();
		Gson gson = new Gson();

		values.put(TABLE_MISSION[0], ev.getClass().getName());
		values.put(TABLE_MISSION[1], ev.getID());
		values.put(TABLE_MISSION[2], gson.toJson(ev));

		try
		{
			db.insert(TABLE_NAME[1], null, values);
		}
		catch(Exception e)
		{
			Log.e("DB ERROR", e.toString()); 
			e.printStackTrace(); 
		}
		setChanged();
		notifyObservers(ev);
	}
	/**
	 * Method for adding contact to database
	 * @param c
	 */
	public void addRow(Contact c)
	{
		ContentValues values = new ContentValues();

		values.put(TABLE_CONTACT[0], c.getName());
		values.put(TABLE_CONTACT[1], c.getSipaddress());

		try
		{
			db.insert(TABLE_NAME[2], null, values);
		}
		catch(Exception e)
		{
			Log.e("DB ERROR", e.toString()); 
			e.printStackTrace(); 
		}
		setChanged();
		notifyObservers(c);
	}
	/**
	 * Method for updating a specific row in the Contacts 
	 * @param c - Contact to update
	 * @param name - New name
	 * @param sipaddress - New SIP-address
	 */
	public void updateRow(Contact c, String name, String sipaddress)
	{
		ContentValues values = new ContentValues();
		values.put(TABLE_CONTACT[0], name);
		values.put(TABLE_CONTACT[1], sipaddress);

		try {db.update(TABLE_NAME[2], values, TABLE_CONTACT[0] + " = '" + c.getName()+"'", null);}
		catch (Exception e)
		{
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Method for updating current event 
	 * @param Event
	 */
	public void updateRow(Event ev)
	{
		Gson gson = new Gson();
		ContentValues values = new ContentValues();
		values.put(TABLE_MISSION[0], ev.getClass().getName());
		values.put(TABLE_MISSION[1], ev.getID());
		values.put(TABLE_MISSION[2], gson.toJson(ev));

		try {db.update(TABLE_NAME[1], values, TABLE_MISSION[0] + " = '" + ev.getID()+"'", null);}
		catch (Exception e)
		{
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
		setChanged();
		notifyObservers(ev);
	}
	

public boolean checkRow(String str){
		if(firstRun==true){
			firstRun = false;
			return false;
		}
		else{
			Cursor cursor = null;
			System.out.println("Kör checkRow med str "+str);
			cursor = db.query(TABLE_NAME[2], TABLE_CONTACT, null,null,null,null,null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				System.out.println("Cursor är "+cursor.getString(1));
				if(cursor.getString(1).equals(str)){
					System.out.println("Adressen "+str+" fanns redan i database");
					return true;
				}
				cursor.moveToNext();
			}
		}
		return false;
	}

	/**
	 * Method for deleting a row
	 * @param name - name of contact to delete
	 */
	public void deleteRow(String str)
	{
		if(Pattern.matches("[0-9]{12}", str)){
			// ask the database manager to delete the event of given id
			try
			{
				db.delete(TABLE_NAME[1], TABLE_MISSION[0] + " = " + str, null);
			}
			catch (Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
		}else{
			// ask the database manager to delete the contact of given name
			try
			{
				db.delete(TABLE_NAME[2], TABLE_CONTACT[0] + " = " + str, null);
			}
			catch (Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
		}
	}


	public Event getCurrentEvent(){
		Cursor cursor = null;
		try
		{
			cursor = db.query(TABLE_NAME[1], TABLE_MISSION, null, null, null, null, null);
			cursor.moveToLast();
			if(cursor != null){
				Gson gson = new Gson();
				if(cursor.getString(2) != null){
				return gson.fromJson(cursor.getString(1), Event.class);
				}
			}
		}catch(SQLException e){

		}catch(NullPointerException e){

		}
		return null;
	}
	/**********************************************************************
	 * RETRIEVING ALL ROWS FROM THE DATABASE TABLE
	 * 
	 * This is an example of how to retrieve all data from a database
	 * table using this class.  You should edit this method to suit your
	 * needs.
	 * 
	 * the key is automatically assigned by the database
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList getAllRowsAsArrayList(String table)
	{
		// create an ArrayList that will hold all of the data collected from
		// the database.
		ArrayList dataList = new ArrayList();

		// this is a database call that creates a "cursor" object.
		// the cursor object store the information collected from the
		// database and is used to iterate through the data.
		Cursor cursor = null;

		try
		{	
			//Request map table
			if(table.equals(TABLE_NAME[0])){
				cursor = db.query(
						table,
						TABLE_MAP,
						null, null, null, null, null
				);

				// move the cursor's pointer to position zero.
				cursor.moveToFirst();
				Gson gson = new Gson();

				// if there is data after the current cursor position, add it
				// to the ArrayList.
				if (!cursor.isAfterLast())
				{
					do
					{
						Class c = null;
						try {
							c = Class.forName(cursor.getString(0));
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dataList.add(gson.fromJson(cursor.getString(1), c));
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}
			}	
			//Request mission table
			if(table.equals(TABLE_NAME[1])){
				cursor = db.query(
						table,
						TABLE_MISSION,
						null, null, null, null, null
				);
				Gson gson = new Gson();
				cursor.moveToFirst();
				if (!cursor.isAfterLast())
				{
					do
					{ 
						Class c = null;
						try {
							c = Class.forName(cursor.getString(0));
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						dataList.add(gson.fromJson(cursor.getString(2), c));
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}
			}
			//Request contact
			if(table.equals(TABLE_NAME[2])){
				cursor = db.query(
						table,
						TABLE_CONTACT,
						null, null, null, null, null
				);
				cursor.moveToFirst();

				if (!cursor.isAfterLast())
				{
					do
					{
						dataList.add(new Contact(cursor.getString(0), cursor.getString(1)));
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}
			}

		}
		catch (SQLException e)
		{
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
		// return the ArrayList that holds the data collected from
		// the database.
		cursor.close();
		return dataList;
	}

	// the beginnings our SQLiteOpenHelper class
	private class CustomSQLiteOpenHelper extends SQLiteOpenHelper{

		public CustomSQLiteOpenHelper(Context context){
			super(context, DB_NAME, null, DB_VERSION);
		}		

		@Override
		public void onCreate(SQLiteDatabase db)	{
			// the SQLite query string that will create our 3 column database table.
			//			String missionTableQueryString = 	
			//				"create table " +
			//				TABLE_NAME[1] +
			//				" (" +
			//				TABLE_MISSION[0] + " integer primary key autoincrement not null," +
			//				TABLE_ROW_ONE + " text," +
			//				TABLE_ROW_TWO + " text" +
			//				");";

			String mapTableQueryString = 	
				"CREATE TABLE " +
				TABLE_NAME[0] +
				" (" +
				TABLE_MAP[0] + " TEXT," +
				TABLE_MAP[1] + " TEXT" +
				");";
			String missionTableQueryString = 	
				"CREATE TABLE " +
				TABLE_NAME[1] +
				" (" +
				TABLE_MISSION[0] + " TEXT," +
				TABLE_MISSION[1] + " TEXT," +
				TABLE_MISSION[2] + " TEXT" +
				");";
			String contactTableQueryString = 	
				"CREATE TABLE " +
				TABLE_NAME[2] +
				" (" +
				TABLE_CONTACT[0] + " TEXT," +
				TABLE_CONTACT[1] + " TEXT" +
				");";

			// execute the query string to the database.
			db.execSQL(mapTableQueryString);
			db.execSQL(missionTableQueryString);
			db.execSQL(contactTableQueryString);
		}


		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}
	}

	public void close() {
		db.close();
	}
}

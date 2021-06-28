package com.shubham.mts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsManager.db";
    private static final String TABLE_BOOKINGS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "num";
    private static final String KEY_PH_NO = "route";
    private static final String KEY_FARE = "fare";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + " (" + KEY_ID + " TEXT PRIMARY KEY, " + KEY_NAME + " TEXT, " + KEY_PH_NO + " TEXT, " + KEY_FARE + " TEXT" + ");";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public boolean addBooking(String ID, String num, String route, String fare) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, ID);
        values.put(KEY_NAME, num); // Contact Name
        values.put(KEY_PH_NO, route); // Contact Phone
        //values.put(KEY_FARE, fare);
        // Inserting Row
        long res= db.insert(TABLE_BOOKINGS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
        if(res == -1)
            return false;
        else
            return true;
    }


    // code to get all contacts in a list view
    public Cursor getAllContacts() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BOOKINGS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // return contact list
        return cursor;
    }

    // code to update the single contact
    public int updateContact(Bookings contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getNum());
        values.put(KEY_PH_NO, contact.getRoute());

        // updating row
        return db.update(TABLE_BOOKINGS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getID())});
    }

    // Deleting single contact
    public void deleteContact(Bookings contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKINGS, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getID())});
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + TABLE_BOOKINGS;

        Cursor cursor = db.rawQuery(countQuery, null);


        // return count
        int c = cursor.getCount();
        cursor.close();
        return c;
    }

}
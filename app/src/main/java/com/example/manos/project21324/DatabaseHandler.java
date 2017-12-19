package com.example.manos.project21324;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
    public static final int DATABASE_VERSION = 1;

	// Database Name
	public static final String DATABASE_NAME = "CoordinatesManager";

	// Coordinates table name
    public static final String TABLE_COORDINATES = "Coordinates";

	// Coordinates Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_USERID = "userid";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_DATETIME = "dt";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_COORDINATES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_COORDINATES + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_USERID + " VARCHAR(10),"
				+ KEY_LATITUDE + " FLOAT,"
				+ KEY_LONGITUDE + " FLOAT,"
				+ KEY_DATETIME + " VARCHAR(20)" + ")";
		db.execSQL(CREATE_COORDINATES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COORDINATES);
		onCreate(db);
	}

	/**
	 * CRUD Operations
	 */

	// Adding new Coordinates
	void addCoordinates(Coordinates coordinates) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_USERID, coordinates.get_userid()); //  User Id
		values.put(KEY_LATITUDE, coordinates.get_latitude()); //  Latitude
		values.put(KEY_LONGITUDE, coordinates.get_longitude()); //  Longitude
		values.put(KEY_DATETIME, coordinates.get_dt()); //  Datetime

		// Inserting Row
		db.insert(TABLE_COORDINATES, null, values);
		db.close(); // Closing database connection
	}

    // Get Single Coordinates For A User
    public List<String> getCoordinates(String userID, String datetime) {
		SQLiteDatabase db = this.getReadableDatabase();
        List<String> coordinatesList = new ArrayList<>();
        String selection = KEY_USERID + "='" + userID + "' AND " + KEY_DATETIME + "='" + datetime + "'";
		Cursor cursor = db.query(TABLE_COORDINATES, new String[] {KEY_ID, KEY_USERID, KEY_LATITUDE, KEY_LONGITUDE, KEY_DATETIME},
                selection,null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                coordinatesList.add("Latitude: " + cursor.getString(2));
                coordinatesList.add("Longitude: " + cursor.getString(3));
            } while (cursor.moveToNext());
        }
        cursor.close();
		return coordinatesList;
	}

	// Getting All Coordinates from Database
	public List<Coordinates> getAllCoordinates() {
		List<Coordinates> coordinatesList = new ArrayList<>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(
		        TABLE_COORDINATES,
                new String[] {KEY_ID, KEY_USERID, KEY_LATITUDE, KEY_LONGITUDE, KEY_DATETIME},
                null, null, null, null, null);

		// looping through all rows and adds them to list
		if (cursor.moveToFirst()) {
			do {
				Coordinates coordinates = new Coordinates();
				coordinates.set_id(Integer.parseInt(cursor.getString(0)));
				coordinates.set_userid(cursor.getString(1));
				coordinates.set_latitude(cursor.getString(2));
				coordinates.set_longitude(cursor.getString(3));
				coordinates.set_dt(cursor.getString(4));

                coordinatesList.add(coordinates);
			} while (cursor.moveToNext());
		}
        cursor.close();
		return coordinatesList;
	}

    // Getting All DateTimes For Dropdown View
    public List<String> getAllDateTimes() {
        List<String> dateTimesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_COORDINATES,
                new String[] {KEY_ID, KEY_USERID, KEY_LATITUDE, KEY_LONGITUDE, KEY_DATETIME},
                null, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding Datetime to list
                dateTimesList.add(cursor.getString(4));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dateTimesList;
    }

	// Delete all coordinates (function for my convenience)
	public void deleteCoordinates(Coordinates coordinates) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_COORDINATES, KEY_ID + " = ?",
				new String[] { String.valueOf(coordinates.get_id()) });
        Log.i("Deleted Coordinates", coordinates.toString() );
        db.close();
	}

}

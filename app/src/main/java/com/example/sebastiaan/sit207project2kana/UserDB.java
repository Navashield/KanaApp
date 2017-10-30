package com.example.sebastiaan.sit207project2kana;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class UserDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    // The database is saved as userinfo.db
    private static final String DATABASE_NAME = "userinfo.db";
    // Here we save strings associated with the database
    public static final String USER_INFO = "userInfo";
    public static final String COLUMN_USER = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_HIRA_SCORE = "hiraScore";

    public UserDB(Context context, String name, SQLiteDatabase.CursorFactory
            factory, int version) {
        // Here we say whats what, in this case the name of the database is
        // mapped to DATABASE_NAME while version is DATABASE_VERSION
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    // Here we create the database using SQL commands, create a table with the
    // name userInfo, make username text and the primary key, add password as
    // text and hira high score as an int
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + USER_INFO + "(" +
                COLUMN_USER + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_HIRA_SCORE + " INTEGER);";
        sqLiteDatabase.execSQL(query);

    }

    // In the case of upgrading we drop the existing table
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP_TABLE IF EXISTS " + USER_INFO);
        onCreate(sqLiteDatabase);
    }

    // Adding Info to DB
    public void addToTable(String CurUser, String CurPass) {
        ContentValues values = new ContentValues();
        // We add our values to a ContentValues
        values.put(COLUMN_USER, CurUser);
        values.put(COLUMN_PASSWORD, CurPass);
        values.put(COLUMN_HIRA_SCORE, 0);

        // Then we get an instance of the datebase
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        // And add the information into the database
        sqLiteDatabase.insert(USER_INFO, null, values);
        // Close it so we can access it later
        sqLiteDatabase.close();
    }

    // As we go through the game, the score gets updated
    public void updateScore(String userName, int inScore) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        // We update the users score
        String query = "UPDATE " + USER_INFO + " SET " + COLUMN_HIRA_SCORE + " = "
                + inScore + " WHERE " + COLUMN_USER + " = '" + userName + "';";
        sqLiteDatabase.execSQL(query);
    }

    // We also want to get the score when we load the app
    public Integer getScore(String userName) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        // Search for the scores associated with a user
        String query = "SELECT " + COLUMN_HIRA_SCORE + " FROM " + USER_INFO +
                " WHERE " + COLUMN_USER + " = '" + userName + "'";
        // We use a sursor to select the element
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        // There is only one score, we select the first
        cursor.moveToFirst();
        int myInt = cursor.getInt(0);
        cursor.close();
        // Return the score we found
        return myInt;
    }

    // Just like find the score, except we're looking for a password
    public String getPassword(String userName) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "SELECT " + COLUMN_PASSWORD + " FROM " + USER_INFO +
                " WHERE " + COLUMN_USER + " = '" + userName + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        String myStr = cursor.getString(0);
        cursor.close();
        return myStr;
    }

    // We use this method to determine whether a user exists in the database
    // True tells us there is a user with that name, false says the opposite
    public Boolean userExists(String userName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_USER + " FROM " + USER_INFO +
                " WHERE " + COLUMN_USER + " = '" + userName + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}

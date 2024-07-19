package com.example.resturantproject.Security;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 3; // Update the version
    private static final String TABLE_NAME_USER = "USER";
    private static final String TABLE_NAME_STRINGS = "USER_STRINGS"; // New table

    private static final String COLUMN_EMAIL = "EMAIL";
    private static final String COLUMN_FIRST_NAME = "FIRST_NAME";
    private static final String COLUMN_LAST_NAME = "LAST_NAME";
    private static final String COLUMN_PHONE_NUMBER = "PHONE_NUMBER";
    private static final String COLUMN_GENDER = "GENDER";
    private static final String COLUMN_HASHED_PASSWORD = "HASHED_PASSWORD";
    private static final String COLUMN_STRING = "STRING"; // New column
    private static final String COLUMN_IS_ADMIN = "IS_ADMIN"; // New column


    private static final String DEFAULT_ADMIN_EMAIL = "admin@admin.com";
    private static final String DEFAULT_ADMIN_FIRST_NAME = "Admin";
    private static final String DEFAULT_ADMIN_LAST_NAME = "Admin";
    private static final String DEFAULT_ADMIN_PHONE_NUMBER = "0599314380";
    private static final String DEFAULT_ADMIN_GENDER = "Other";
    private static final String DEFAULT_ADMIN_HASHED_PASSWORD = "admin"; // Ideally, this should be hashed


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createUserTableQuery = "CREATE TABLE " + TABLE_NAME_USER + " (" +
                COLUMN_EMAIL + " TEXT PRIMARY KEY, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT, " +
                COLUMN_PHONE_NUMBER + " TEXT, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_HASHED_PASSWORD + " TEXT, " +
                COLUMN_IS_ADMIN + " INTEGER)"; // New column
        sqLiteDatabase.execSQL(createUserTableQuery);

        String createStringTableQuery = "CREATE TABLE " + TABLE_NAME_STRINGS + " (" +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_STRING + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_EMAIL + ") REFERENCES " + TABLE_NAME_USER + "(" + COLUMN_EMAIL + "))";
        sqLiteDatabase.execSQL(createStringTableQuery);

        addDefaultAdminUser(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STRINGS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        onCreate(sqLiteDatabase);
    }

    public void insertUser(User user) throws SQLException {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_EMAIL, user.getEmail());
            contentValues.put(COLUMN_FIRST_NAME, user.getFirstName());
            contentValues.put(COLUMN_LAST_NAME, user.getLastName());
            contentValues.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());
            contentValues.put(COLUMN_GENDER, user.getGender());
            contentValues.put(COLUMN_HASHED_PASSWORD, user.getHashedPassword());
            contentValues.put(COLUMN_IS_ADMIN, user.isAdmin() ? 1 : 0); // New attribute

            long rowId = sqLiteDatabase.insert(TABLE_NAME_USER, null, contentValues);
            if (rowId == -1) {
                throw new SQLException("Failed to insert user. Email already exists.");
            }

            insertUserStrings(user.getEmail(), user.getFavorites());

            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    private void insertUserStrings(String email, Set<String> stringSet) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        for (String str : stringSet) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_EMAIL, email);
            contentValues.put(COLUMN_STRING, str);
            sqLiteDatabase.insert(TABLE_NAME_STRINGS, null, contentValues);
        }
    }

    private void addDefaultAdminUser(SQLiteDatabase sqLiteDatabase) {
        // Check if the admin user already exists
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT 1 FROM " + TABLE_NAME_USER + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{DEFAULT_ADMIN_EMAIL});
        boolean adminExists = cursor.moveToFirst();
        cursor.close();

        if (!adminExists) {
            Log.d("SYS120", "ADDING ADMIN!");
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_EMAIL, DEFAULT_ADMIN_EMAIL);
            contentValues.put(COLUMN_FIRST_NAME, DEFAULT_ADMIN_FIRST_NAME);
            contentValues.put(COLUMN_LAST_NAME, DEFAULT_ADMIN_LAST_NAME);
            contentValues.put(COLUMN_PHONE_NUMBER, DEFAULT_ADMIN_PHONE_NUMBER);
            contentValues.put(COLUMN_GENDER, DEFAULT_ADMIN_GENDER);
            contentValues.put(COLUMN_HASHED_PASSWORD, Hash.hashPassword(DEFAULT_ADMIN_HASHED_PASSWORD));
            contentValues.put(COLUMN_IS_ADMIN, 1);

            sqLiteDatabase.insert(TABLE_NAME_USER, null, contentValues);
        }
    }

    public void deleteUser(String email) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.delete(TABLE_NAME_STRINGS, COLUMN_EMAIL + " = ?", new String[]{email});
            sqLiteDatabase.delete(TABLE_NAME_USER, COLUMN_EMAIL + " = ?", new String[]{email});
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    public Cursor getAllUsers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_USER, null);
    }

    public Cursor findUserByEmail(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_USER + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
    }

    @SuppressLint("Range")
    public User getUserByEmail(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor userCursor = findUserByEmail(email);
        User user = null;

        if (userCursor != null && userCursor.moveToFirst()) {
            user = new User();
            user.setEmail(userCursor.getString(userCursor.getColumnIndex(COLUMN_EMAIL)));
            user.setFirstName(userCursor.getString(userCursor.getColumnIndex(COLUMN_FIRST_NAME)));
            user.setLastName(userCursor.getString(userCursor.getColumnIndex(COLUMN_LAST_NAME)));
            user.setPhoneNumber(userCursor.getString(userCursor.getColumnIndex(COLUMN_PHONE_NUMBER)));
            user.setGender(userCursor.getString(userCursor.getColumnIndex(COLUMN_GENDER)));
            user.setHashedPassword(userCursor.getString(userCursor.getColumnIndex(COLUMN_HASHED_PASSWORD)));
            user.setAdmin(userCursor.getInt(userCursor.getColumnIndex(COLUMN_IS_ADMIN)) == 1); // New attribute

            // Retrieve the set of strings for this user
            Set<String> stringSet = getUserStrings(email);
            user.setFavorites(stringSet);
        }

        if (userCursor != null) {
            userCursor.close();
        }

        return user;
    }

    @SuppressLint("Range")
    public Set<String> getUserStrings(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + COLUMN_STRING + " FROM " + TABLE_NAME_STRINGS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        Set<String> stringSet = new HashSet<>();
        while (cursor.moveToNext()) {
            stringSet.add(cursor.getString(cursor.getColumnIndex(COLUMN_STRING)));
        }
        cursor.close();
        return stringSet;
    }

    // Method to add a string to the user's set
    public void addUserString(String email, String newString) throws SQLException {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_STRING, newString);
        long rowId = sqLiteDatabase.insert(TABLE_NAME_STRINGS, null, contentValues);
        if (rowId == -1) {
            throw new SQLException("Failed to add string to user.");
        }
    }

    // Method to remove a string from the user's set
    public void removeUserString(String email, String stringToRemove) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME_STRINGS, COLUMN_EMAIL + " = ? AND " + COLUMN_STRING + " = ?", new String[]{email, stringToRemove});
    }

    public void updateUser(User user) throws SQLException {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_FIRST_NAME, user.getFirstName());
            contentValues.put(COLUMN_LAST_NAME, user.getLastName());
            contentValues.put(COLUMN_PHONE_NUMBER, user.getPhoneNumber());
            contentValues.put(COLUMN_HASHED_PASSWORD, user.getHashedPassword());
            contentValues.put(COLUMN_IS_ADMIN, user.isAdmin() ? 1 : 0); // New attribute

            int rowsAffected = sqLiteDatabase.update(TABLE_NAME_USER, contentValues, COLUMN_EMAIL + " = ?", new String[]{user.getEmail()});
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update user. User not found.");
            }

            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    // New method to check if a user is an admin
    @SuppressLint("Range")
    public boolean isAdmin(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + COLUMN_IS_ADMIN + " FROM " + TABLE_NAME_USER + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        boolean isAdmin = false;
        if (cursor != null && cursor.moveToFirst()) {
            isAdmin = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_ADMIN)) == 1;
            cursor.close();
        }
        return isAdmin;
    }
}

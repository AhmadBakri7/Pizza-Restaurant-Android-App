package com.example.resturantproject.Security;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.resturantproject.models.SpecialOffer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SpecialOfferDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SpecialOffersDatabase.db";
    private static final int DATABASE_VERSION = 2; // Incremented version
    private static final String TABLE_NAME_SPECIAL_OFFERS = "SPECIAL_OFFERS";

    private static final String COLUMN_PIZZA_NAME = "PIZZA_NAME";
    private static final String COLUMN_PIZZA_SIZE = "PIZZA_SIZE";
    private static final String COLUMN_OFFER_START_DATE = "OFFER_START_DATE";
    private static final String COLUMN_OFFER_END_DATE = "OFFER_END_DATE";
    private static final String COLUMN_TOTAL_PRICE = "TOTAL_PRICE";
    private static final String COLUMN_PIZZA_COUNT = "PIZZA_COUNT"; // Add this line

    public SpecialOfferDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSpecialOfferTableQuery = "CREATE TABLE " + TABLE_NAME_SPECIAL_OFFERS + " (" +
                COLUMN_PIZZA_NAME + " TEXT, " +
                COLUMN_PIZZA_SIZE + " TEXT, " +
                COLUMN_OFFER_START_DATE + " TEXT, " +
                COLUMN_OFFER_END_DATE + " TEXT, " +
                COLUMN_TOTAL_PRICE + " REAL, " +
                COLUMN_PIZZA_COUNT + " INTEGER)"; // Add this line
        sqLiteDatabase.execSQL(createSpecialOfferTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME_SPECIAL_OFFERS + " ADD COLUMN " + COLUMN_PIZZA_COUNT + " INTEGER DEFAULT 0");
        }
    }

    public void addSpecialOffer(SpecialOffer specialOffer) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PIZZA_NAME, specialOffer.getPizzaName());
        contentValues.put(COLUMN_PIZZA_SIZE, specialOffer.getPizzaSize());
        contentValues.put(COLUMN_OFFER_START_DATE, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(specialOffer.getOfferStartDate()));
        contentValues.put(COLUMN_OFFER_END_DATE, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(specialOffer.getOfferEndDate()));
        contentValues.put(COLUMN_TOTAL_PRICE, specialOffer.getTotalPrice());
        contentValues.put(COLUMN_PIZZA_COUNT, specialOffer.getPizzaCount()); // Add this line
        sqLiteDatabase.insert(TABLE_NAME_SPECIAL_OFFERS, null, contentValues);
    }

    @SuppressLint("Range")
    public List<SpecialOffer> getAllSpecialOffers() {
        List<SpecialOffer> specialOffers = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_SPECIAL_OFFERS, null);
        if (cursor.moveToFirst()) {
            do {
                String pizzaName = cursor.getString(cursor.getColumnIndex(COLUMN_PIZZA_NAME));
                String pizzaSize = cursor.getString(cursor.getColumnIndex(COLUMN_PIZZA_SIZE));
                String offerStartDateString = cursor.getString(cursor.getColumnIndex(COLUMN_OFFER_START_DATE));
                String offerEndDateString = cursor.getString(cursor.getColumnIndex(COLUMN_OFFER_END_DATE));
                double totalPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL_PRICE));
                int pizzaCount = cursor.getInt(cursor.getColumnIndex(COLUMN_PIZZA_COUNT)); // Add this line
                Date offerStartDate = null;
                Date offerEndDate = null;
                try {
                    offerStartDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(offerStartDateString);
                    offerEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(offerEndDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                specialOffers.add(new SpecialOffer(pizzaName, pizzaSize, offerStartDate, offerEndDate, totalPrice, pizzaCount)); // Add this line
            } while (cursor.moveToNext());
        }
        cursor.close();
        return specialOffers;
    }

    @SuppressLint("Range")
    public List<SpecialOffer> getSpecialOffersByPizzaName(String pizzaName) {
        List<SpecialOffer> specialOffers = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] columns = {COLUMN_PIZZA_NAME, COLUMN_PIZZA_SIZE, COLUMN_OFFER_START_DATE, COLUMN_OFFER_END_DATE, COLUMN_TOTAL_PRICE, COLUMN_PIZZA_COUNT}; // Add COLUMN_PIZZA_COUNT
        String selection = COLUMN_PIZZA_NAME + " = ?";
        String[] selectionArgs = {pizzaName};

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME_SPECIAL_OFFERS, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String pizzaSize = cursor.getString(cursor.getColumnIndex(COLUMN_PIZZA_SIZE));
                String offerStartDateString = cursor.getString(cursor.getColumnIndex(COLUMN_OFFER_START_DATE));
                String offerEndDateString = cursor.getString(cursor.getColumnIndex(COLUMN_OFFER_END_DATE));
                double totalPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL_PRICE));
                int pizzaCount = cursor.getInt(cursor.getColumnIndex(COLUMN_PIZZA_COUNT)); // Add this line
                Date offerStartDate = null;
                Date offerEndDate = null;
                try {
                    offerStartDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(offerStartDateString);
                    offerEndDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(offerEndDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                specialOffers.add(new SpecialOffer(pizzaName, pizzaSize, offerStartDate, offerEndDate, totalPrice, pizzaCount)); // Add this line
            } while (cursor.moveToNext());
        }
        cursor.close();
        return specialOffers;
    }
}

package com.example.resturantproject.Security;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.resturantproject.models.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "OrderDatabase.db";
    private static final int DATABASE_VERSION = 3; // Updated version
    private static final String TABLE_NAME_ORDER = "ORDERS";

    private static final String COLUMN_USER_EMAIL = "USER_EMAIL";
    private static final String COLUMN_PIZZA_NAME = "PIZZA_NAME";
    private static final String COLUMN_PIZZA_SIZE = "PIZZA_SIZE";
    private static final String COLUMN_ORDER_DATE = "ORDER_DATE";
    private static final String COLUMN_PIZZA_COUNT = "PIZZA_COUNT";
    private static final String COLUMN_COST = "COST"; // New column

    public OrderDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createOrderTableQuery = "CREATE TABLE " + TABLE_NAME_ORDER + " (" +
                COLUMN_USER_EMAIL + " TEXT, " +
                COLUMN_PIZZA_NAME + " TEXT, " +
                COLUMN_PIZZA_SIZE + " TEXT, " +
                COLUMN_ORDER_DATE + " TEXT, " +
                COLUMN_PIZZA_COUNT + " INTEGER, " +
                COLUMN_COST + " REAL)"; // New column
        sqLiteDatabase.execSQL(createOrderTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            String alterTableQuery1 = "ALTER TABLE " + TABLE_NAME_ORDER + " ADD COLUMN " + COLUMN_PIZZA_COUNT + " INTEGER DEFAULT 0";
            sqLiteDatabase.execSQL(alterTableQuery1);
        }
        if (oldVersion < 3) {
            String alterTableQuery2 = "ALTER TABLE " + TABLE_NAME_ORDER + " ADD COLUMN " + COLUMN_COST + " REAL DEFAULT 0.0";
            sqLiteDatabase.execSQL(alterTableQuery2);
        }
    }

    public void addOrder(Order order) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_EMAIL, order.getUserEmail());
        contentValues.put(COLUMN_PIZZA_NAME, order.getPizzaName());
        contentValues.put(COLUMN_PIZZA_SIZE, order.getPizzaSize());
        contentValues.put(COLUMN_ORDER_DATE, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(order.getOrderDate()));
        contentValues.put(COLUMN_PIZZA_COUNT, order.getPizzaCount());
        contentValues.put(COLUMN_COST, order.getCost()); // New field
        sqLiteDatabase.insert(TABLE_NAME_ORDER, null, contentValues);
    }

    @SuppressLint("Range")
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_ORDER, null);
        if (cursor.moveToFirst()) {
            do {
                String userEmail = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL));
                String pizzaName = cursor.getString(cursor.getColumnIndex(COLUMN_PIZZA_NAME));
                String pizzaSize = cursor.getString(cursor.getColumnIndex(COLUMN_PIZZA_SIZE));
                String dateString = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DATE));
                int pizzaCount = cursor.getInt(cursor.getColumnIndex(COLUMN_PIZZA_COUNT));
                double cost = cursor.getDouble(cursor.getColumnIndex(COLUMN_COST)); // New field
                Date orderDate = null;
                try {
                    orderDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                orders.add(new Order(userEmail, pizzaName, pizzaSize, orderDate, pizzaCount, cost)); // Updated constructor
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }

    @SuppressLint("Range")
    public List<Order> getOrdersByUserEmail(String userEmail) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] columns = {COLUMN_USER_EMAIL, COLUMN_PIZZA_NAME, COLUMN_PIZZA_SIZE, COLUMN_ORDER_DATE, COLUMN_PIZZA_COUNT, COLUMN_COST};
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {userEmail};

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME_ORDER, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String pizzaName = cursor.getString(cursor.getColumnIndex(COLUMN_PIZZA_NAME));
                String pizzaSize = cursor.getString(cursor.getColumnIndex(COLUMN_PIZZA_SIZE));
                String dateString = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DATE));
                int pizzaCount = cursor.getInt(cursor.getColumnIndex(COLUMN_PIZZA_COUNT));
                double cost = cursor.getDouble(cursor.getColumnIndex(COLUMN_COST)); // New field
                Date orderDate = null;
                try {
                    orderDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                orders.add(new Order(userEmail, pizzaName, pizzaSize, orderDate, pizzaCount, cost)); // Updated constructor
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }

    // New Methods

    // Calculate the number of orders for a specific pizza name
    public int getOrderCountForPizzaName(String pizzaName) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_ORDER + " WHERE " + COLUMN_PIZZA_NAME + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{pizzaName});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // Calculate the total price for all orders of a specific pizza name
    public double getTotalPriceForPizzaName(String pizzaName) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_COST + ") FROM " + TABLE_NAME_ORDER + " WHERE " + COLUMN_PIZZA_NAME + " = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{pizzaName});
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    // Calculate the total price for all orders
    public double getTotalPriceForAllOrders() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_COST + ") FROM " + TABLE_NAME_ORDER;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }
}
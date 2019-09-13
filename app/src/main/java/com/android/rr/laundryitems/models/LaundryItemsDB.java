package com.android.rr.laundryitems.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class LaundryItemsDB extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "LaundryItemDB";
    private final String ITEM_NAMES_TABLE = "ItemsNamesTable";
    private final String COLUMN_ITEM_NAME = "ItemName";
    private final String SAVE_ITEMS_TABLE = "SaveItemsTable";
    private final String COLUMN_ITEM_QUANTITY = "ItemQuantity";
    private final String COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS = "DateTimeInMillis";

    private SQLiteDatabase mSqLiteDatabase;

    public LaundryItemsDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ITEM_NAMES_TABLE+"(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLUMN_ITEM_NAME+" TEXT UNIQUE)");

        db.execSQL("CREATE TABLE "+SAVE_ITEMS_TABLE+"(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLUMN_ITEM_NAME+" TEXT,"+ COLUMN_ITEM_QUANTITY+" TEXT,"+
                COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS+" LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ITEM_NAMES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+SAVE_ITEMS_TABLE);

        onCreate(db);
    }

    public void insertLaundryItem (String laundryItemName) {
        mSqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEM_NAME, laundryItemName);

        mSqLiteDatabase.insertWithOnConflict(ITEM_NAMES_TABLE, null,
                contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public List<String> getLaundryItems () {
        List<String> laundryItemNames = null;

        mSqLiteDatabase = this.getReadableDatabase();
        String orderBy = COLUMN_ITEM_NAME+" ASC";
        Cursor cursor = mSqLiteDatabase.query(ITEM_NAMES_TABLE, null, null,
                null, null, null, orderBy);

        if (null != cursor && cursor.getCount() > 0) {
            cursor.moveToFirst();
            laundryItemNames = new ArrayList<>();

            while (cursor.moveToNext())
                laundryItemNames.add(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)));
        }

        return  laundryItemNames;
    }

    public int getTotalLaundryItemsCount () {
        mSqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = mSqLiteDatabase.query(ITEM_NAMES_TABLE, null, null,
                null, null, null, null);

        return null != cursor ? cursor.getCount() : -4;
    }

    public void deleteLaundryItem (String laundryItemsToDelete) {
//        if (null != laundryItemsToDelete && laundryItemsToDelete.length > 0) {
            mSqLiteDatabase = this.getWritableDatabase();
            String whereClause = COLUMN_ITEM_NAME + "=?";
            String[] whereArgs = {laundryItemsToDelete};
            mSqLiteDatabase.delete(ITEM_NAMES_TABLE, whereClause, whereArgs);
//        }
    }

    public void saveLaundryItemsDetails (List<LaundryItemsModel> laundryItemsModels) {
        mSqLiteDatabase = this.getWritableDatabase();

        for (LaundryItemsModel laundryItemsModel : laundryItemsModels) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_ITEM_NAME, laundryItemsModel.getItemName());
            contentValues.put(COLUMN_ITEM_QUANTITY, laundryItemsModel.getItemQuantity());
            contentValues.put(COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS,
                    laundryItemsModel.getDateTimeInMillis());

            mSqLiteDatabase.insert(SAVE_ITEMS_TABLE, null, contentValues);
        }
    }

    public List<LaundryItemsModel> getSavedLaundryItemsDetails () {
        List<LaundryItemsModel> savedLaundryItems = null;

        mSqLiteDatabase = this.getReadableDatabase();
        String orderBy = COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS+" ASC";
        Cursor cursor = mSqLiteDatabase.query(SAVE_ITEMS_TABLE, null, null,
                null, null, null, orderBy);

        if (null != cursor && cursor.getCount() > 0) {
            cursor.moveToFirst();
            savedLaundryItems = new ArrayList<>();

            while (cursor.moveToNext())
                savedLaundryItems.add(new LaundryItemsModel(
                        cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_QUANTITY)),
                        cursor.getLong(cursor.getColumnIndex(
                                COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS))));
        }

        return  savedLaundryItems;
    }

    /*public void deleteSavedLaundryItemsDetails (String[] laundryItemsToDelete) {

    }*//*public void deleteSavedLaundryItemsDetails (String[] laundryItemsToDelete) {

    }*/

}
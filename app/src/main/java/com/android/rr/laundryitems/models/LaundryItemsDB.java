package com.android.rr.laundryitems.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LaundryItemsDB extends SQLiteOpenHelper {
    private final String TAG = LaundryItemsDB.class.getSimpleName();
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
        final String orderBy = COLUMN_ITEM_NAME+" ASC";
        final Cursor cursor = mSqLiteDatabase.query(ITEM_NAMES_TABLE, null, null,
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
        final Cursor cursor = mSqLiteDatabase.query(ITEM_NAMES_TABLE, null, null,
                null, null, null, null);

        return null != cursor ? cursor.getCount() : -4;
    }

    public void deleteLaundryItem (String laundryItemsToDelete) {
        mSqLiteDatabase = this.getWritableDatabase();
        final String whereClause = COLUMN_ITEM_NAME + "=?";
        final String[] whereArgs = {laundryItemsToDelete};
        mSqLiteDatabase.delete(ITEM_NAMES_TABLE, whereClause, whereArgs);
    }

    public void saveLaundryItemsDetails (List<LaundryItemsModel> laundryItemsModels) {
        mSqLiteDatabase = this.getWritableDatabase();
        Log.i(TAG, "save laundry items: "+laundryItemsModels.size());
        for (LaundryItemsModel laundryItemsModel : laundryItemsModels) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_ITEM_NAME, laundryItemsModel.getItemName());
            contentValues.put(COLUMN_ITEM_QUANTITY, laundryItemsModel.getItemQuantity());
            contentValues.put(COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS,
                    laundryItemsModel.getDateTimeInMillis());

            mSqLiteDatabase.insert(SAVE_ITEMS_TABLE, null, contentValues);
        }
    }

    public List<LauncherItemsDetailsModel> getSavedLaundryItemsDetails () {
        final List<Long> dateTimeInMillsList = getDateTimeInMillis();
        List<LauncherItemsDetailsModel> laundryItemsDetailsModels = new ArrayList<>();

        if (null != dateTimeInMillsList && dateTimeInMillsList.size() > 0) {
            for (long dateTimeInMillis : dateTimeInMillsList) {
                Log.i(TAG, "get saved laundry items for: "+dateTimeInMillis);
                List<LaundryItemsModel> laundryItemsModels = getLaundryDetailsForGivenTime(
                        dateTimeInMillis);
                LauncherItemsDetailsModel launcherItemsDetailsModel = new LauncherItemsDetailsModel();
                launcherItemsDetailsModel.setDateTimeInMillis(dateTimeInMillis);
                launcherItemsDetailsModel.setLaundryItemsModels(laundryItemsModels);

                laundryItemsDetailsModels.add(launcherItemsDetailsModel);
            }
        }

        Log.i(TAG, "get saved laundry items size: "+
                laundryItemsDetailsModels.size());
        return  laundryItemsDetailsModels;
    }

    private List<Long> getDateTimeInMillis () {
        List<Long> dateTimeMillsList = new ArrayList<>();
        mSqLiteDatabase = this.getReadableDatabase();

        final String[] columns = {COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS};
        final String orderBy = COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS+" DESC";
        final Cursor cursor = mSqLiteDatabase.query(SAVE_ITEMS_TABLE, columns, null,
                null, null, null, orderBy);

        if (null != cursor && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                dateTimeMillsList.add(cursor.getLong(
                        cursor.getColumnIndex(COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS)));
            }
        }

        Log.i(TAG, "get date time size: "+dateTimeMillsList.size());
        return dateTimeMillsList;
    }

    private List<LaundryItemsModel> getLaundryDetailsForGivenTime (long dateTimeInMillis) {
        List<LaundryItemsModel> savedLaundryItems = null;
        mSqLiteDatabase = this.getReadableDatabase();
        final String selection = COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS+"=?";
        final String[] selectionArgs = {String.valueOf(dateTimeInMillis)};
        final Cursor cursor = mSqLiteDatabase.query(SAVE_ITEMS_TABLE, null, selection,
                selectionArgs, null, null, null);
        Log.i(TAG, "get laundry details for: "+dateTimeInMillis+
                ", count: "+(null != cursor ? cursor.getCount() : -1));

        if (null != cursor && cursor.getCount() > 0) {
            savedLaundryItems = new ArrayList<>();

            while (cursor.moveToNext()) {
                savedLaundryItems.add(new LaundryItemsModel(
                        cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_QUANTITY)),
                        cursor.getLong(cursor.getColumnIndex(
                                COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS))));
            }
        }

        Log.i(TAG, "get laundry details size: "+
                (null != savedLaundryItems ? savedLaundryItems.size() : -1));
        return savedLaundryItems;
    }

    public void deleteOlderSavedLaundryDetails () {
        final long sixMonthsInDays = 6 * 30;
        final long sixMonthsInHours = sixMonthsInDays * 24;
        final long sixMonthsInMinutes = sixMonthsInHours * 60;
        final long sixMonthsInSeconds = sixMonthsInMinutes * 60;
        final long sixMonthsTimeInMillis  = sixMonthsInSeconds * 1000;
        final long currentTimeInMillis = System.currentTimeMillis();
        final long sixMonthsTimeInMillisFromCurrent = currentTimeInMillis - sixMonthsTimeInMillis;

        final String[] columns = {COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS};
        final String whereClause = COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS+"<=?";
        final String[] whereArgs = {String.valueOf(sixMonthsTimeInMillisFromCurrent)};

        mSqLiteDatabase = this.getReadableDatabase();
        final Cursor cursor = mSqLiteDatabase.query(SAVE_ITEMS_TABLE, columns, whereClause, whereArgs,
                null, null, null);
        Log.i(TAG, "delete old items count: "+(null != cursor?cursor.getCount():-1));
        if (null != cursor && cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                final String dateTimeInMillisToDelete = String.valueOf(cursor.getLong(
                        cursor.getColumnIndex(COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS)));
                deleteSavedLaundryItemBaseOnTime(dateTimeInMillisToDelete);
            }
        }
    }

    private void deleteSavedLaundryItemBaseOnTime (String timeInMillis) {
        Log.i(TAG, "delete old laundry item for: "+timeInMillis);
        final String whereClause = COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS+"=?";
        final String[] whereArgs = {String.valueOf(timeInMillis)};

        mSqLiteDatabase = this.getWritableDatabase();
        mSqLiteDatabase.delete(SAVE_ITEMS_TABLE, whereClause, whereArgs);
    }

}
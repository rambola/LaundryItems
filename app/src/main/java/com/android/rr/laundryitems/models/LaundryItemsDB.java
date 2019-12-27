package com.android.rr.laundryitems.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Log.e(TAG, "saveLaundryItemDetails..... laundryItemsModels: "+laundryItemsModels.size());
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
        List<Long> dateTimeInMillsList = getDateTimeInMillis();
        List<LauncherItemsDetailsModel> laundryItemsDetailsModels = new ArrayList<>();

        if (null != dateTimeInMillsList && dateTimeInMillsList.size() > 0) {
            for (long dateTimeInMillis : dateTimeInMillsList) {
                Log.i(TAG, "getSavedLaundryItemsDetails... dateTimeInMillis: "+dateTimeInMillis);
                List<LaundryItemsModel> laundryItemsModels = getLaundryDetailsForGivenTime(
                        dateTimeInMillis);
                LauncherItemsDetailsModel launcherItemsDetailsModel = new LauncherItemsDetailsModel();
                launcherItemsDetailsModel.setDateTimeInMillis(dateTimeInMillis);
                launcherItemsDetailsModel.setLaundryItemsModels(laundryItemsModels);

                laundryItemsDetailsModels.add(launcherItemsDetailsModel);
            }
        }

        Log.i(TAG, "getSavedLaundryItemsDetails... before return size: "+
                laundryItemsDetailsModels.size());
        return  laundryItemsDetailsModels;
    }

    /*private HashMap<String, Long> getDateTimeInMillis () {
        HashMap<String, Long> dateTimeMillsMap = new HashMap<>();
        mSqLiteDatabase = this.getReadableDatabase();
        String orderBy = COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS+" DESC";
        String[] columns = {COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS};
        Cursor cursor = mSqLiteDatabase.query(SAVE_ITEMS_TABLE, columns, null,
                null, null, null, orderBy);

        if (null != cursor && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int count=0;

            while (cursor.moveToNext()) {
                Log.i(TAG, "getDateTimeInMillis().. dateTimeInMillis.. "+cursor.getLong(
                        cursor.getColumnIndex(COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS))+", count: "+count);
                dateTimeMillsMap.put("dateTimeInMillis"+count, cursor.getLong(
                        cursor.getColumnIndex(COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS)));
                count++;
            }
        }

        Log.i(TAG, "getDateTimeInMillis().. before return map size is: "+dateTimeMillsMap.size());
        return dateTimeMillsMap;
    }*/

    private List<Long> getDateTimeInMillis () {
        List<Long> dateTimeMillsList = new ArrayList<>();
        mSqLiteDatabase = this.getReadableDatabase();

        String[] columns = {COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS};
        String orderBy = COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS+" DESC";
        Cursor cursor = mSqLiteDatabase.query(SAVE_ITEMS_TABLE, columns, null,
                null, null, null, orderBy);

        if (null != cursor && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Log.i(TAG, "getDateTimeInMillis().. dateTimeInMillis.. "+cursor.getLong(
                        cursor.getColumnIndex(COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS)));
                dateTimeMillsList.add(cursor.getLong(
                        cursor.getColumnIndex(COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS)));
            }
        }

        Log.i(TAG, "getDateTimeInMillis().. before return map size is: "+dateTimeMillsList.size());
        return dateTimeMillsList;
    }

    private List<LaundryItemsModel> getLaundryDetailsForGivenTime (long dateTimeInMillis) {
        List<LaundryItemsModel> savedLaundryItems = null;
        mSqLiteDatabase = this.getReadableDatabase();
        String selection = COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS+"=?";
        String[] selectionArgs = {String.valueOf(dateTimeInMillis)};
        Cursor cursor = mSqLiteDatabase.query(SAVE_ITEMS_TABLE, null, selection,
                selectionArgs, null, null, null);
        Log.e(TAG, "getLaundryDetailsForGivenTime... dateTimeInMillis.. "+dateTimeInMillis+", cursor size: "+(null != cursor ? cursor.getCount() : -1));

        if (null != cursor && cursor.getCount() > 0) {
//            cursor.moveToFirst();
            savedLaundryItems = new ArrayList<>();

            while (cursor.moveToNext()) {
                savedLaundryItems.add(new LaundryItemsModel(
                        cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_QUANTITY)),
                        cursor.getLong(cursor.getColumnIndex(
                                COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS))));

                Log.i(TAG, "getLaundryDetailsForGivenTime... inside while: "+
                        (null != savedLaundryItems ? savedLaundryItems.size() : -1));
            }
        }

        Log.i(TAG, "getLaundryDetailsForGivenTime... before return size: "+
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

        Log.e(TAG, "deleteOlderSavedLaundryDetails... sixMonthsTimeInMillisFromCurrent: "+sixMonthsTimeInMillisFromCurrent);
        final String[] columns = {COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS};
        final String whereClause = COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS+"<=?";
        final String[] whereArgs = {String.valueOf(sixMonthsTimeInMillisFromCurrent)};

        mSqLiteDatabase = this.getReadableDatabase();
        final Cursor cursor = mSqLiteDatabase.query(SAVE_ITEMS_TABLE, columns, whereClause, whereArgs,
                null, null, null);
        Log.e(TAG, "deleteOlderSavedLaundryDetails... cursor count: "+(null != cursor?cursor.getCount():-1));
        if (null != cursor && cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                final String dateTimeInMillisToDelete = String.valueOf(cursor.getLong(
                        cursor.getColumnIndex(COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS)));
                Log.e(TAG, "deleteOlderSavedLaundryDetails... dateTimeInMillisToDelete: "+
                        dateTimeInMillisToDelete);
                deleteSavedLaundryItemBaseOnTime(dateTimeInMillisToDelete);
            }
        }
    }

    private void deleteSavedLaundryItemBaseOnTime (String timeInMillis) {
        Log.e(TAG, "deleteSavedLaundryItemBaseOnTime... timeInMillis: "+timeInMillis);
        final String whereClause = COLUMN_SAVE_ITEM_DATE_TIME_IN_MILLIS+"=?";
        final String[] whereArgs = {String.valueOf(timeInMillis)};

        mSqLiteDatabase = this.getWritableDatabase();
        mSqLiteDatabase.delete(SAVE_ITEMS_TABLE, whereClause, whereArgs);
    }

}
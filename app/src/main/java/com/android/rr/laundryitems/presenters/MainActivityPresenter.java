package com.android.rr.laundryitems.presenters;

import android.util.Log;
import android.view.View;

import com.android.rr.laundryitems.R;
import com.android.rr.laundryitems.adapter.LaundryItemsAdapter;
import com.android.rr.laundryitems.models.LaundryItemsDB;
import com.android.rr.laundryitems.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivityPresenter implements View.OnClickListener {
    private final String TAG = MainActivityPresenter.class.getSimpleName();
    private MainActivity mMainActivity;
    private List<String> mLaundryItemsList;
    private LaundryItemsAdapter mLaundryItemsAdapter;
    private LaundryItemsDB mLaundryItemsDB;

    public MainActivityPresenter (MainActivity mainActivity) {
        mMainActivity = mainActivity;
        mLaundryItemsDB = new LaundryItemsDB(mMainActivity);
    }

    public void loadDefaultItems() {
        saveDefaultLaundryItems();
        mMainActivity.setAdapter(mLaundryItemsAdapter);
    }


    private void saveLaundryItems () {

    }

    private void showLaundryItems () {

    }

    private void showAddLaundryItem () {

    }

    private void deleteLaundryItem () {

    }

    private void saveDefaultLaundryItems () {
        int count = mLaundryItemsDB.getTotalLaundryItemsCount();
        Log.e(TAG, "saveDefaultLaundryItems... count: "+count);
        if (count == -4 || count == 0) {
            String[] defaultItems = mMainActivity.getResources().getStringArray(
                    R.array.default_laundry_items);
            for (String defaultItem : defaultItems)
                mLaundryItemsDB.insertLaundryItem(defaultItem);
        }

        mLaundryItemsList = new ArrayList<>();
        mLaundryItemsList = mLaundryItemsDB.getLaundryItems();
        mLaundryItemsAdapter = new LaundryItemsAdapter(mLaundryItemsList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                mMainActivity.animateFAB();
                break;
            case R.id.prevSavedItemsTV:
                break;
            case R.id.addLaundryItemTV:
                break;
            case R.id.deleteLaundryItemTV:
                break;
        }
    }

    public interface MainActivityViewPresenter {
        void setAdapter(LaundryItemsAdapter laundryItemsAdapter);
        void animateFAB();
    }

}
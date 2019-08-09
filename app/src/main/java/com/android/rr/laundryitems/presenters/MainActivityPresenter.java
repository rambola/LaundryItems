package com.android.rr.laundryitems.presenters;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.rr.laundryitems.R;
import com.android.rr.laundryitems.adapter.LaundryItemsAdapter;
import com.android.rr.laundryitems.models.LaundryItemsDB;
import com.android.rr.laundryitems.models.MyDividerItemDecoration;
import com.android.rr.laundryitems.views.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivityPresenter {
    private final String TAG = MainActivityPresenter.class.getSimpleName();
    private MainActivity mMainActivity;
    private List<String> mLaundryItemsList;
    private LaundryItemsAdapter mLaundryItemsAdapter;
    private LaundryItemsDB mLaundryItemsDB;

    public MainActivityPresenter (MainActivity mainActivity) {
        mMainActivity = mainActivity;
        mLaundryItemsDB = new LaundryItemsDB(mMainActivity);
    }

    public void loadDefaultItems(RecyclerView recyclerView) {
        saveDefaultLaundryItems();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mMainActivity);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(
                mMainActivity, LinearLayoutManager.VERTICAL, 9));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mLaundryItemsAdapter);
    }

    public void saveLaundryItems () {

    }

    public void showLaundryItems () {

    }

    public void showAddLaundryItem () {

    }

    public void deleteLaundryItem () {

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
//            Collections.sort(mLaundryItemsList);
        mLaundryItemsAdapter = new LaundryItemsAdapter(mLaundryItemsList);
    }

}
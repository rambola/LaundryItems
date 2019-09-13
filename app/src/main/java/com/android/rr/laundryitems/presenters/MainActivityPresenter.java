package com.android.rr.laundryitems.presenters;

import android.util.Log;
import android.view.View;

import com.android.rr.laundryitems.R;
import com.android.rr.laundryitems.adapter.LaundryItemsAdapter;
import com.android.rr.laundryitems.models.LaundryItemsDB;
import com.android.rr.laundryitems.utils.LaundryItemsDialog;
import com.android.rr.laundryitems.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivityPresenter implements View.OnClickListener {
    private final String TAG = MainActivityPresenter.class.getSimpleName();
    private MainActivity mMainActivity;
    private List<String> mLaundryItemsList;
    private LaundryItemsAdapter mLaundryItemsAdapter;
    private LaundryItemsDB mLaundryItemsDB;
    private LaundryItemsDialog mDeleteItemDialog;

    public MainActivityPresenter (MainActivity mainActivity) {
        mMainActivity = mainActivity;
        mLaundryItemsDB = new LaundryItemsDB(mMainActivity);
    }

    public void loadDefaultItems() {
        saveDefaultLaundryItems();
        mMainActivity.setAdapter(mLaundryItemsAdapter);
    }

    private void showLaundryItems () {
        new LaundryItemsDialog(mMainActivity, MainActivityPresenter.this,
                "show").show();
    }

    private void addLaundryItem () {
        new LaundryItemsDialog(mMainActivity,MainActivityPresenter.this,
                "add").show();
    }

    public void saveLaundryItem (String item) {
        mLaundryItemsDB.insertLaundryItem(item);
        if (null != mLaundryItemsList && mLaundryItemsList.size() > 0)
            mLaundryItemsList.clear();

        mLaundryItemsList = mLaundryItemsDB.getLaundryItems();
        mLaundryItemsAdapter.updateAdapter(mLaundryItemsList);
    }

    private void deleteLaundryItem () {
        if (null != mLaundryItemsList && mLaundryItemsList.size() > 0)
            mDeleteItemDialog = new LaundryItemsDialog(mMainActivity,
                    MainActivityPresenter.this, "delete");
        mDeleteItemDialog.show();
    }

    public void deleteLaundryItem (String item) {
        mLaundryItemsDB.deleteLaundryItem(item);
        if (null != mLaundryItemsList && mLaundryItemsList.size() > 0)
            mLaundryItemsList.clear();

        mLaundryItemsList = mLaundryItemsDB.getLaundryItems();
        mLaundryItemsAdapter.updateAdapter(mLaundryItemsList);
    }

    public void dismissDialog () {
        mDeleteItemDialog.dismiss();
    }

    private void saveDefaultLaundryItems () {
        int count = mLaundryItemsDB.getTotalLaundryItemsCount();
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
        mMainActivity.animateFAB();
        switch (v.getId()) {
            case R.id.prevSavedItemsTV:
                showLaundryItems();
                break;
            case R.id.addLaundryItemTV:
                addLaundryItem();
                break;
            case R.id.deleteLaundryItemTV:
                deleteLaundryItem();
                break;
        }
    }

    public interface MainActivityViewPresenter {
        void setAdapter(LaundryItemsAdapter laundryItemsAdapter);
        void animateFAB();
    }

}
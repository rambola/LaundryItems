package com.android.rr.laundryitems.presenters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.rr.laundryitems.R;
import com.android.rr.laundryitems.adapter.LaundryItemsAdapter;
import com.android.rr.laundryitems.models.LaundryItemsDB;
import com.android.rr.laundryitems.models.LaundryItemsModel;
import com.android.rr.laundryitems.utils.LaundryItemsDialog;
import com.android.rr.laundryitems.utils.RootViewTreeObserver;
import com.android.rr.laundryitems.views.LaundryDetailsActivity;
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
        if (mLaundryItemsDB.getSavedLaundryItemsDetails().size() > 0)
            mMainActivity.startActivity(new Intent(mMainActivity, LaundryDetailsActivity.class));
        else
            Toast.makeText(mMainActivity, "No saved items to show...", Toast.LENGTH_SHORT).show();
    }

    private void addLaundryItem () {
        new LaundryItemsDialog(mMainActivity,MainActivityPresenter.this,
                "add").show();
    }

    private void saveLaundryItemsDetails (List<LaundryItemsModel> laundryItemsModels) {
        mLaundryItemsDB.saveLaundryItemsDetails(laundryItemsModels);
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

    public void getLaundryDataAndSaveToDB (RecyclerView recyclerView) throws NullPointerException {
        int recyclerItemCount = recyclerView.getAdapter().getItemCount();
        int emptyFieldsCount = 0;
        List<LaundryItemsModel> laundryItemsModels = null;
        long currentDateTimeInMillis = System.currentTimeMillis();
        Log.e(TAG, "getLaundryDataAndSaveToDB, adapterItemCount: "+recyclerItemCount);

        for (int i=0; i<recyclerItemCount; i++) {
            View view = recyclerView.getChildAt(i);
            TextView textView = view.findViewById(R.id.itemNameTV);
            EditText editText = view.findViewById(R.id.itemQuantityET);

            String laundryItem = textView.getText().toString().trim();
            String itemQuantity = editText.getText().toString().trim();

            Log.e(TAG, "itemNameTV: "+laundryItem+ ", quantity: "+itemQuantity+
                    ", currentDateTimeInMillis: "+ currentDateTimeInMillis+", emptyFieldsCount: "+
                    emptyFieldsCount);

            if (TextUtils.isEmpty(itemQuantity)) {
                emptyFieldsCount++;
                continue;
            }

            laundryItemsModels = new ArrayList<>();
            laundryItemsModels.add(new LaundryItemsModel(laundryItem, itemQuantity,
                    currentDateTimeInMillis));
        }

        if (emptyFieldsCount == recyclerItemCount)
            Toast.makeText(mMainActivity, "Please fill the quantity for items...",
                    Toast.LENGTH_SHORT).show();
        else
            saveLaundryItemsDetails(laundryItemsModels);
    }

    public void registerForObserver (View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new RootViewTreeObserver(
                mMainActivity, (RelativeLayout) view));
    }

    public void unRegisterForObserver (View view) {
        view.getViewTreeObserver().removeOnGlobalLayoutListener(new RootViewTreeObserver(
                mMainActivity, (RelativeLayout) view));
    }

    public interface MainActivityViewPresenter {
        void setAdapter(LaundryItemsAdapter laundryItemsAdapter);
        void animateFAB();
        void checkAndCloseFAB(boolean isKeyboardOpen);
    }

}
package com.android.rr.laundryitems.presenters;

import android.util.Log;

import com.android.rr.laundryitems.adapter.LaundryDetailsAdapter;
import com.android.rr.laundryitems.models.LauncherItemsDetailsModel;
import com.android.rr.laundryitems.models.LaundryItemsDB;
import com.android.rr.laundryitems.utils.LaundryItemsDialog;
import com.android.rr.laundryitems.views.LaundryDetailsActivity;

import java.util.List;

public class LaundryDetailsActivityPresenter {
    private final String TAG = LaundryDetailsActivityPresenter.class.getSimpleName();
    private LaundryDetailsActivity mLaundryDetailsActivity;
    private LaundryItemsDB mLaundryItemsDB;
    private List<LauncherItemsDetailsModel> mLauncherItemsDetailsModels;

    public LaundryDetailsActivityPresenter (LaundryDetailsActivity laundryDetailsActivity) {
        mLaundryDetailsActivity = laundryDetailsActivity;
        mLaundryItemsDB = new LaundryItemsDB(laundryDetailsActivity);
    }

    public void initializeAdapter () {
        mLauncherItemsDetailsModels = mLaundryItemsDB.getSavedLaundryItemsDetails();
        final LaundryDetailsAdapter laundryDetailsAdapter =
                new LaundryDetailsAdapter(mLaundryDetailsActivity,
                LaundryDetailsActivityPresenter.this,
                mLauncherItemsDetailsModels);
        mLaundryDetailsActivity.setAdapter(laundryDetailsAdapter);
    }

    public void showLaundryFullDetails (int position) {
        Log.i(TAG, "show full for: "+position);
        new LaundryItemsDialog(mLaundryDetailsActivity,
                mLauncherItemsDetailsModels.get(position)).show();
    }

    public interface ILaundryDetailsActivityPresenter {
        void setAdapter(LaundryDetailsAdapter laundryDetailsAdapter);
    }
}
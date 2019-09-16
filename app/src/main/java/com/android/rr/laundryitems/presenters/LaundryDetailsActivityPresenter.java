package com.android.rr.laundryitems.presenters;

import android.view.View;

import com.android.rr.laundryitems.adapter.LaundryDetailsAdapter;
import com.android.rr.laundryitems.models.LauncherItemsDetailsModel;
import com.android.rr.laundryitems.models.LaundryItemsDB;
import com.android.rr.laundryitems.views.LaundryDetailsActivity;

import java.util.List;

public class LaundryDetailsActivityPresenter {
    private LaundryDetailsActivity mLaundryDetailsActivity;
    private LaundryDetailsAdapter mLaundryDetailsAdapter;
    private LaundryItemsDB mLaundryItemsDB;

    public LaundryDetailsActivityPresenter (LaundryDetailsActivity laundryDetailsActivity) {
        mLaundryDetailsActivity = laundryDetailsActivity;
        mLaundryItemsDB = new LaundryItemsDB(laundryDetailsActivity);
    }

    public void initializeAdapter () {
        List<LauncherItemsDetailsModel> launcherItemsDetailsModels = mLaundryItemsDB.
                getSavedLaundryItemsDetails();
        mLaundryDetailsAdapter = new LaundryDetailsAdapter(mLaundryDetailsActivity,
                LaundryDetailsActivityPresenter.this,
                launcherItemsDetailsModels);
        mLaundryDetailsActivity.setAdapter(mLaundryDetailsAdapter);
    }

    public void showLaundryFullDetails (int position) {

    }

    public interface ILaundryDetailsActivityPresenter {
        void setAdapter(LaundryDetailsAdapter laundryDetailsAdapter);
    }
}
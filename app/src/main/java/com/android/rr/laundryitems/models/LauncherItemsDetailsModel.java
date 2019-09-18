package com.android.rr.laundryitems.models;

import java.util.List;

public class LauncherItemsDetailsModel {
    private long dateTimeInMillis;
    private List<LaundryItemsModel> laundryItemsModels;

    public long getDateTimeInMillis() {
        return this.dateTimeInMillis;
    }

    public List<LaundryItemsModel> getLaundryItemsModels() {
        return this.laundryItemsModels;
    }

    void setDateTimeInMillis(long dateTimeInMillis) {
        this.dateTimeInMillis = dateTimeInMillis;
    }

    void setLaundryItemsModels(List<LaundryItemsModel> laundryItemsModels) {
        this.laundryItemsModels = laundryItemsModels;
    }
}
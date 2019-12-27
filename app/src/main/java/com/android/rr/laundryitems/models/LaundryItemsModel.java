package com.android.rr.laundryitems.models;

public class LaundryItemsModel {
    private String itemName;
    private String itemQuantity;
    private long dateTimeInMillis;

    public LaundryItemsModel (String itemName, String itemQuantity, long dateTimeInMillis) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.dateTimeInMillis = dateTimeInMillis;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    long getDateTimeInMillis() {
        return dateTimeInMillis;
    }

}
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

    String getItemName() {
        return itemName;
    }

    String getItemQuantity() {
        return itemQuantity;
    }

    long getDateTimeInMillis() {
        return dateTimeInMillis;
    }

}
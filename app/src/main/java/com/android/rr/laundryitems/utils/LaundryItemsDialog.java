package com.android.rr.laundryitems.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.rr.laundryitems.R;
import com.android.rr.laundryitems.adapter.ItemsDeleteAdapter;
import com.android.rr.laundryitems.models.LaundryItemsDB;
import com.android.rr.laundryitems.presenters.MainActivityPresenter;

import java.util.List;

public class LaundryItemsDialog {
    private final String TAG = LaundryItemsDialog.class.getSimpleName();
    private String mShowDialogFor;
    private LayoutInflater mInflater;
    private AlertDialog.Builder mAlert;
    private AlertDialog mDialog;
    private Context mContext;
    private MainActivityPresenter mMainActivityPresenter;

    public LaundryItemsDialog(Context context, MainActivityPresenter mainActivityPresenter,
                              String showDialogFor) {
        mContext = context;
        mMainActivityPresenter = mainActivityPresenter;
        mShowDialogFor = showDialogFor;
        dismiss();
        mInflater = LayoutInflater.from(context);
        mAlert = new AlertDialog.Builder(context);
    }

    private void createAddItemDialog() {
        View alertLayout = mInflater.inflate(R.layout.layout_add_item, null);
        final EditText addItemET = alertLayout.findViewById(R.id.enterNewItemET);
        final Button saveVBtn = alertLayout.findViewById(R.id.saveItemBtn);

        saveVBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredItem = addItemET.getText().toString().trim();
                Log.i(TAG, "entered item: "+enteredItem);

                if (enteredItem.length() > 0)
                    mMainActivityPresenter.saveLaundryItem(enteredItem);
                else
                    Log.i(TAG, "Please enter item name before saving to your laundry list..");

                mDialog.dismiss();
            }
        });

        // this is set the view from XML inside AlertDialog
        mAlert.setView(alertLayout);
    }

    private void createDeleteItemsDialog() {
        View alertLayout = mInflater.inflate(R.layout.layout_delete_items, null);
        final ListView listView = alertLayout.findViewById(R.id.itemToDeleteLV);

        LaundryItemsDB laundryItemsDB = new LaundryItemsDB(mContext);
        List<String> itemsList = laundryItemsDB.getLaundryItems();

//        ItemsDeleteAdapter itemsDeleteAdapter = new ItemsDeleteAdapter(mContext, itemsList);
        listView.setAdapter(new ItemsDeleteAdapter(mContext, mMainActivityPresenter, itemsList));

        // this is set the view from XML inside AlertDialog
        mAlert.setView(alertLayout);
    }

    private void createShowItemsDialog() {

    }

    public void show() {
        if (mShowDialogFor.equalsIgnoreCase("add"))
            createAddItemDialog();
        else if (mShowDialogFor.equalsIgnoreCase("delete"))
            createDeleteItemsDialog();
        else
            createShowItemsDialog();

        mDialog = mAlert.create();
        mDialog.show();
    }

    public void dismiss () {
        if (null != mDialog) {
            mDialog.dismiss();
            mDialog = null;
        }

        if (null != mAlert) {
            mAlert = null;
        }
    }

}
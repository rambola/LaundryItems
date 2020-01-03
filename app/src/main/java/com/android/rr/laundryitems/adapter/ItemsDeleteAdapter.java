package com.android.rr.laundryitems.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.rr.laundryitems.R;
import com.android.rr.laundryitems.presenters.MainActivityPresenter;

import java.util.List;

public class ItemsDeleteAdapter extends BaseAdapter {
    private final String TAG = ItemsDeleteAdapter.class.getSimpleName();
    private List<String> mLaundryItemsList;
    private Context mContext;
    private MainActivityPresenter mMainActivityPresenter;

    public ItemsDeleteAdapter (Context context, MainActivityPresenter mainActivityPresenter,
                               List<String> laundryItemsList) {
        mMainActivityPresenter = mainActivityPresenter;
        mContext = context;
        mLaundryItemsList = laundryItemsList;
    }

    private void updateAdapter () {
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mLaundryItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mLaundryItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_layout, null);

            viewHolder.itemNameTV = convertView.findViewById(R.id.itemNameToDeleteTV);
            viewHolder.deleteIV = convertView.findViewById(R.id.deleteItemIV);

            viewHolder.deleteIV.setOnClickListener(new DeleteIVClickListener(position));
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.itemNameTV.setText(mLaundryItemsList.get(position));

        return convertView;
    }

    private class ViewHolder {
        TextView itemNameTV;
        ImageView deleteIV;
    }

    private class DeleteIVClickListener implements View.OnClickListener {
        int mPosition;

        DeleteIVClickListener (int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "item to delete: "+mPosition);
            mMainActivityPresenter.deleteLaundryItem(mLaundryItemsList.get(mPosition));
            mLaundryItemsList.remove(mPosition);
            if (mLaundryItemsList.size() > 0) {
                updateAdapter();
            }
            else
                mMainActivityPresenter.dismissDialog();
        }
    }

}
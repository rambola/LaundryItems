package com.android.rr.laundryitems.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.rr.laundryitems.R;
import com.android.rr.laundryitems.models.LauncherItemsDetailsModel;
import com.android.rr.laundryitems.models.LaundryItemsModel;

import java.util.List;

public class SelectedPrevItemDetailsAdapter extends BaseAdapter {
    final private List<LaundryItemsModel> mLaundryItemsModelsList;
    final private Context mContext;


    public SelectedPrevItemDetailsAdapter(Context context,
                                          LauncherItemsDetailsModel launcherItemsDetailsModel) {
        mContext = context;
        mLaundryItemsModelsList = launcherItemsDetailsModel.getLaundryItemsModels();
    }

    @Override
    public int getCount() {
        return mLaundryItemsModelsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mLaundryItemsModelsList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.selected_prev_list_item_layout, null);

            viewHolder.itemNameAndQuantityTV = convertView.findViewById(
                    R.id.selectedListItemNameAndQuantityTV);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.itemNameAndQuantityTV.setText(mContext.getString(R.string.prev_item_details,
                mLaundryItemsModelsList.get(position).getItemName(),
                mLaundryItemsModelsList.get(position).getItemQuantity()));

        return convertView;
    }

    private class ViewHolder {
        TextView itemNameAndQuantityTV;
    }

}
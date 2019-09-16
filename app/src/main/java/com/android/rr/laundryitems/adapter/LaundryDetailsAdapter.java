package com.android.rr.laundryitems.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.rr.laundryitems.R;
import com.android.rr.laundryitems.models.LauncherItemsDetailsModel;
import com.android.rr.laundryitems.presenters.LaundryDetailsActivityPresenter;

import java.util.List;

public class LaundryDetailsAdapter extends RecyclerView.Adapter<LaundryDetailsAdapter.MyViewHolder> {
    private List<LauncherItemsDetailsModel> mLauncherItemsDetailsModels;
    private LaundryDetailsActivityPresenter mLaundryDetailsActivityPresenter;

    public LaundryDetailsAdapter (Context context,
                                  LaundryDetailsActivityPresenter laundryDetailsActivityPresenter,
                                  List<LauncherItemsDetailsModel> launcherItemsDetailsModels) {
        mLauncherItemsDetailsModels = launcherItemsDetailsModels;
        mLaundryDetailsActivityPresenter = laundryDetailsActivityPresenter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_laundry_details_adapter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.mDateTimeTV.setText(convertMillisToDateTime(mLauncherItemsDetailsModels.get(i).
                getDateTimeInMillis()));
        myViewHolder.mTotalCountTV.setText(mLauncherItemsDetailsModels.get(i).
                getLaundryItemsModels().size());
        myViewHolder.mCardView.setOnClickListener(new MyClickListener(i));
    }

    @Override
    public int getItemCount() {
        return mLauncherItemsDetailsModels.size();
    }

    private String convertMillisToDateTime (long dateTimeInMillis) {
        return DateFormat.format("dd-MM-yyyy hh:mm", dateTimeInMillis).toString();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mDateTimeTV;
        TextView mTotalCountTV;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.cardView);
            mDateTimeTV = itemView.findViewById(R.id.dateTimeTV);
            mTotalCountTV = itemView.findViewById(R.id.totalItemsTV);
        }
    }

    private class MyClickListener implements View.OnClickListener {
        final private int mClickPosition;

        MyClickListener (int clickPosition) {
            mClickPosition = clickPosition;
        }

        @Override
        public void onClick(View v) {
            mLaundryDetailsActivityPresenter.showLaundryFullDetails(mClickPosition);
        }
    }

}
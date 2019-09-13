package com.android.rr.laundryitems.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.rr.laundryitems.R;

import java.util.List;

public class LaundryItemsAdapter extends RecyclerView.Adapter<LaundryItemsAdapter.MyViewHolder> {
    private final String TAG = LaundryItemsAdapter.class.getSimpleName();
    private List<String> mLaundryItemsList;

    public LaundryItemsAdapter (List<String> moviesList) {
        this.mLaundryItemsList = moviesList;
    }

    public void updateAdapter (List<String> moviesList) {
        mLaundryItemsList.clear();
        mLaundryItemsList.addAll(moviesList);
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adadpter_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String itemName = mLaundryItemsList.get(position);
        holder.itemNameTV.setText(itemName+"    ");
    }

    @Override
    public int getItemCount() {
        return mLaundryItemsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView itemNameTV;
        private EditText itemQuantityET;

        private MyViewHolder(View view) {
            super(view);
            itemNameTV = view.findViewById(R.id.itemNameTV);
            itemQuantityET = view.findViewById(R.id.itemQuantityET);
        }
    }

}
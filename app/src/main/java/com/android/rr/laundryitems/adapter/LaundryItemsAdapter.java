package com.android.rr.laundryitems.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.rr.laundryitems.R;

import java.util.HashMap;
import java.util.List;

public class LaundryItemsAdapter extends RecyclerView.Adapter<LaundryItemsAdapter.MyViewHolder> {
    private final String TAG = LaundryItemsAdapter.class.getSimpleName();
    private List<String> mLaundryItemsList;
    private HashMap<String, String> mLaundryItemCount = new HashMap<>();

    public LaundryItemsAdapter (List<String> laundryItemsList) {
        this.mLaundryItemsList = laundryItemsList;
    }

    public void updateAdapter (List<String> laundryItemsList) {
        mLaundryItemsList.clear();
        mLaundryItemsList.addAll(laundryItemsList);
        this.notifyDataSetChanged();
    }

    public void resetFields (List<String> laundryItemsList) {
        mLaundryItemsList.clear();
        mLaundryItemCount.clear();
        mLaundryItemsList.addAll(laundryItemsList);
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adadpter_layout, parent, false);

        return new MyViewHolder(itemView, new CustomEditTextChangeListener());
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final String itemName = mLaundryItemsList.get(position);
        holder.itemNameTV.setText(itemName+"    ");
        holder.customEditTextChangeListener.textChangePosition(holder.getAdapterPosition());
        if (null != mLaundryItemCount && mLaundryItemCount.size() > 0) {
            final String itemCount = mLaundryItemCount.containsKey(itemName) ?
                    mLaundryItemCount.get(itemName) : "";
            holder.itemQuantityET.setText(itemCount);
        } else {
            holder.itemQuantityET.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mLaundryItemsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView itemNameTV;
        private EditText itemQuantityET;
        private CustomEditTextChangeListener customEditTextChangeListener;

        private MyViewHolder(View view, CustomEditTextChangeListener customEditTextChangeListener) {
            super(view);
            itemNameTV = view.findViewById(R.id.itemNameTV);
            itemQuantityET = view.findViewById(R.id.itemQuantityET);
            this.customEditTextChangeListener = customEditTextChangeListener;
            itemQuantityET.addTextChangedListener(this.customEditTextChangeListener);
        }
    }

    private class CustomEditTextChangeListener implements TextWatcher {
        int position;

        void textChangePosition (int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mLaundryItemCount.put(mLaundryItemsList.get(position), s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    public HashMap<String, String> getEnteredLaundryItemsDetails () {
        Log.i(TAG, "items to save: "+mLaundryItemCount.size());
        return mLaundryItemCount;
    }

}
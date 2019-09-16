package com.android.rr.laundryitems.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.rr.laundryitems.R;
import com.android.rr.laundryitems.adapter.LaundryDetailsAdapter;
import com.android.rr.laundryitems.presenters.LaundryDetailsActivityPresenter;
import com.android.rr.laundryitems.utils.MyDividerItemDecoration;

public class LaundryDetailsActivity extends AppCompatActivity implements
        LaundryDetailsActivityPresenter.ILaundryDetailsActivityPresenter {
    private RecyclerView mRecyclerView;
    private LaundryDetailsActivityPresenter mLaundryDetailsActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_details);

        mRecyclerView = findViewById(R.id.laundryDetailsRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(
                getApplicationContext(), LinearLayoutManager.VERTICAL, 9));

        mLaundryDetailsActivityPresenter = new LaundryDetailsActivityPresenter(
                LaundryDetailsActivity.this);
        mLaundryDetailsActivityPresenter.initializeAdapter();
    }

    @Override
    public void setAdapter(LaundryDetailsAdapter laundryDetailsAdapter) {
        mRecyclerView.setAdapter(laundryDetailsAdapter);
    }
}
package com.android.rr.laundryitems.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.android.rr.laundryitems.R;
import com.android.rr.laundryitems.presenters.MainActivityPresenter;

public class MainActivity extends AppCompatActivity {
    private MainActivityPresenter mMainActivityPresenter;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainActivityPresenter = new MainActivityPresenter(MainActivity.this);
        mRecyclerView = findViewById(R.id.recycler_view);

        mMainActivityPresenter.loadDefaultItems(mRecyclerView);
        //mMainActivityPresenter.hideSoftKeyBoard(mRecyclerView);
    }

}
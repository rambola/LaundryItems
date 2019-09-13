package com.android.rr.laundryitems.views;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.rr.laundryitems.R;
import com.android.rr.laundryitems.adapter.LaundryItemsAdapter;
import com.android.rr.laundryitems.utils.MyDividerItemDecoration;
import com.android.rr.laundryitems.presenters.MainActivityPresenter;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements
        MainActivityPresenter.MainActivityViewPresenter {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private TextView mPrevSavedTV;
    private TextView mAddItemTV;
    private TextView mDeleteItemTV;

    private Animation fab_open;
    private Animation fab_close;
    private Animation rotate_forward;
    private Animation rotate_backward;
    private boolean mIsFabOpen;
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityPresenter mMainActivityPresenter = new MainActivityPresenter(MainActivity.this);
        mRecyclerView = findViewById(R.id.recycler_view);
        mFloatingActionButton = findViewById(R.id.fab);
        mPrevSavedTV = findViewById(R.id.prevSavedItemsTV);
        mAddItemTV = findViewById(R.id.addLaundryItemTV);

        mDeleteItemTV = findViewById(R.id.deleteLaundryItemTV);
        findViewById(R.id.saveLaundryDetailsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getLaundryDataAndSaveToDB();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_backward);

        mFloatingActionButton.setOnClickListener(mMainActivityPresenter);
        mPrevSavedTV.setOnClickListener(mMainActivityPresenter);
        mAddItemTV.setOnClickListener(mMainActivityPresenter);
        mDeleteItemTV.setOnClickListener(mMainActivityPresenter);

        mMainActivityPresenter.loadDefaultItems();
    }

    @Override
    public void setAdapter (LaundryItemsAdapter laundryItemsAdapter) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(
                getApplicationContext(), LinearLayoutManager.VERTICAL, 9));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(laundryItemsAdapter);
    }

    @Override
    public void animateFAB() {
        if(mIsFabOpen) {
            mFloatingActionButton.startAnimation(rotate_backward);
            mPrevSavedTV.startAnimation(fab_close);
            mAddItemTV.startAnimation(fab_close);
            mDeleteItemTV.startAnimation(fab_close);
            mPrevSavedTV.setClickable(false);
            mAddItemTV.setClickable(false);
            mDeleteItemTV.setClickable(false);
            mIsFabOpen = false;
        } else {
            mFloatingActionButton.startAnimation(rotate_forward);
            mPrevSavedTV.startAnimation(fab_open);
            mAddItemTV.startAnimation(fab_open);
            mDeleteItemTV.startAnimation(fab_open);
            mPrevSavedTV.setClickable(true);
            mAddItemTV.setClickable(true);
            mDeleteItemTV.setClickable(true);
            mIsFabOpen = true;
        }
    }

    private void getLaundryDataAndSaveToDB () throws NullPointerException {
        int recyclerItemCount = mRecyclerView.getAdapter().getItemCount();
        Log.e(TAG, "getLaundryDataAndSaveToDB, adapterItemCount: "+recyclerItemCount);

        for (int i=0; i<recyclerItemCount; i++) {
            View view = mRecyclerView.getChildAt(i);
            TextView textView = view.findViewById(R.id.itemNameTV);
            EditText editText = view.findViewById(R.id.itemQuantityET);

            Log.e(TAG, "itemNameTV: "+textView.getText().toString().trim()+
                    ", quantity: "+editText.getText().toString());
        }
    }

}
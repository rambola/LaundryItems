package com.android.rr.laundryitems.utils;

import android.graphics.Rect;
import android.util.TypedValue;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.android.rr.laundryitems.views.MainActivity;

public class RootViewTreeObserver implements ViewTreeObserver.OnGlobalLayoutListener {
    private MainActivity mainActivity;
    private RelativeLayout relativeLayout;

    public RootViewTreeObserver (MainActivity mainActivity, RelativeLayout relativeLayout) {
        this.mainActivity = mainActivity;
        this.relativeLayout = relativeLayout;
    }

    @Override
    public void onGlobalLayout() {
        mainActivity.checkAndCloseFAB(isKeyboardOpen());
    }

    private boolean isKeyboardOpen () {
        Rect visibleBounds = new Rect();
        relativeLayout.getRootView().getWindowVisibleDisplayFrame(visibleBounds);
        float heightDiff = relativeLayout.getRootView().getHeight() - visibleBounds.height();
        float marginOfError = Math.round(this.convertDpToPx(50F));
        return heightDiff > marginOfError;
    }
    private Float convertDpToPx (float val) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                val, mainActivity.getResources().getDisplayMetrics());
    }

}

package com.softdesign.devintensive.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by ant on 28.06.2016.
 */
public class ZoomingLinearLayout extends CoordinatorLayout.Behavior<LinearLayout> {

    public ZoomingLinearLayout() { }

    public ZoomingLinearLayout(Context context, AttributeSet attrs) {

    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {


        Log.d("TAG", "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");



        return true;
                //super.onDependentViewChanged(parent, child, dependency);
    }
}

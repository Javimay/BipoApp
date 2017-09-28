package com.bipo.javier.bipo.home.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by dmayorga on 6/04/2017.
 */

public class RVItemTouchListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener clickListener;

    private GestureDetector gestureDetector;

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    public RVItemTouchListener(Context context, OnItemClickListener listener) {

        this.clickListener = listener;
        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rview, MotionEvent e) {
        View childView = rview.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && clickListener != null && gestureDetector.onTouchEvent(e)) {

            clickListener.onItemClick(childView, rview.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

package com.example.ceyda.friendlypaws;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {
    //private Button button;

    private LostAnimalNoticeActivity activity;

    /*
    public GestureListener(Button button) {
        this.button = button;
    }
    */

    public GestureListener(LostAnimalNoticeActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        // Handle the double-tap gesture
        /*
        ((LostAnimalNoticeActivity) button.getContext()).doublePostBtnFunction();
        return true;
        */
        // Handle single tap gesture
        activity.doublePostBtnFunction();
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Handle fling gesture (swipe)
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        // Handle single tap gesture
        //((LostAnimalNoticeActivity) button.getContext()).singlePostBtnFunction();
        /*
        ((LostAnimalNoticeActivity) button.getContext()).singlePostBtnFunction();
        return  true;
        */
        // Handle single tap gesture
        activity.singlePostBtnFunction();
        return true;
    }


}

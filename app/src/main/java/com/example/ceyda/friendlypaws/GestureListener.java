package com.example.ceyda.friendlypaws;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {
    private Button button;

    public GestureListener(Button button) {
        this.button = button;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        // Handle the double-tap gesture
        ((LostAnimalNoticeActivity) button.getContext()).doublePostBtnFunction();
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
        ((LostAnimalNoticeActivity) button.getContext()).singlePostBtnFunction();
        return true;
    }


}

package com.zlk.bigdemo.freeza.widget.refreshlist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by zale on 2015/12/4.
 */
public abstract class PullLayoutBase<T extends View> extends LinearLayout{


    private float mInitialMotionY;
    private float mTouchSlop = 10;
    private boolean mIsBeDragged = false;
    private boolean mPullToRefreshEnable = true;


    public PullLayoutBase(Context context) {
        super(context);
    }

    public PullLayoutBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if (isReadToPull()){
                    mInitialMotionY = event.getY();
                    mIsBeDragged = false;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (isReadToPull()){
                    float y = event.getY();
                    float dy = y - mInitialMotionY;
                    if (dy>mTouchSlop){
                        mIsBeDragged = true;
                    }
                }

                break;
        }
        return super.onInterceptTouchEvent(event);
    }



    public abstract boolean isReadToPull();


}

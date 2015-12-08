package com.zlk.bigdemo.freeza.widget.pullrefreshview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by zale on 2015/12/8.
 */
public class BaseContainer extends ViewGroup{


    private View mContentView;
    private static final boolean DEBUG_LAYOUT = true;
    public static boolean DEBUG = true;
    private static int ID = 1;
    protected final String LOG_TAG = "zale-frame-" + ++ID;

    private Indicator indicator;

    public BaseContainer(Context context) {
        this(context,null);
    }

    public BaseContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        indicator = new Indicator();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (DEBUG && DEBUG_LAYOUT) {
            Log.i(LOG_TAG, String.format("onMeasure frame: width: %s, height: %s, padding: %s %s %s %s",
                    getMeasuredHeight(), getMeasuredWidth(),
                    getPaddingLeft(), getPaddingRight(), getPaddingTop(), getPaddingBottom()));

        }

        if (mContentView!=null){
            measureContentView(mContentView,widthMeasureSpec,heightMeasureSpec);
        }

    }

    private void measureContentView(View child, int widthMeasureSpec, int heightMeasureSpec) {


        final ViewGroup.LayoutParams lp =  child.getLayoutParams();
        final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,getPaddingLeft()+getPaddingRight(),lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,getPaddingTop()+getPaddingBottom(),lp.height);

        child.measure(childWidthMeasureSpec,childHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildren();
    }

    private void layoutChildren() {

        int offsetY = (int) indicator.getOffsetY();

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        if (mContentView!=null){
            LayoutParams lp = mContentView.getLayoutParams();
            final int left = paddingLeft;
            final int top = paddingTop  + offsetY;
            final int right = left + mContentView.getMeasuredWidth();
            final int bottom = top + mContentView.getMeasuredHeight();
            if (DEBUG && DEBUG_LAYOUT) {
                Log.d(LOG_TAG, String.format("onLayout content: %s %s %s %s", left, top, right, bottom));
            }
            mContentView.layout(left,top,right,bottom);
        }
    }

    @Override
    protected void onFinishInflate() {
        final int childCount = getChildCount();
        if (childCount == 1){
            mContentView = getChildAt(0);
        }
        super.onFinishInflate();
    }

//    @Override
//    public boolean (MotionEvent event) {
//
//        int action = event.getAction();
//        switch (action){
//            case MotionEvent.ACTION_DOWN:
//                indicator.setPressedPos(event.getX(),event.getY());
//                break;
//            case MotionEvent.ACTION_MOVE:
//                indicator.setCurrentPos(event.getX(),event.getY());
//                updateView();
//                break;
//            case MotionEvent.ACTION_UP:
//                indicator.setLastPos(event.getX(),event.getY());
//                break;
//        }
//
//        return super.onInterceptTouchEvent(event);
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.i(LOG_TAG,"down:"+indicator.getOffsetY());
                indicator.setPressedPos(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(LOG_TAG,"move:"+indicator.getOffsetY());
                indicator.setCurrentPos(event.getX(),event.getY());
                updateView();
                break;
            case MotionEvent.ACTION_UP:
                Log.i(LOG_TAG,"up:"+indicator.getOffsetY());
                indicator.setLastPos(event.getX(),event.getY());
                break;
        }

        return true;
    }

    private void updateView() {
        Log.i(LOG_TAG,"indicator.getOffsetY():"+indicator.getOffsetY());
        mContentView.offsetTopAndBottom((int) indicator.getOffsetY());
        requestLayout();
        invalidate();

    }
}

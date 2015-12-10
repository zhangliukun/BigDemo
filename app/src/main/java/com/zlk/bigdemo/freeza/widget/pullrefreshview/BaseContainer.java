package com.zlk.bigdemo.freeza.widget.pullrefreshview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.Toast;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.util.ViewUtil;

/**
 * Created by zale on 2015/12/8.
 */
public class BaseContainer extends ViewGroup{


    private View mContentView;
    private View mHeaderView;
    private static final boolean DEBUG_LAYOUT = true;
    public static boolean DEBUG = true;
    private static int ID = 1;
    protected final String LOG_TAG = "zale-frame-" + ++ID;

    private Indicator indicator;
    private Scroller mScroller;

    public BaseContainer(Context context) {
        this(context, null);
    }

    public BaseContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        indicator = new Indicator();
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mHeaderView!=null){
            measureChild(mHeaderView,widthMeasureSpec,heightMeasureSpec);
            indicator.setHeaderHeight(mHeaderView.getMeasuredHeight());
            Log.i("BaseContainer:",mHeaderView.getMeasuredHeight()+":"+mHeaderView.getHeight());
        }

        if (mContentView!=null){
            measureChild(mContentView,widthMeasureSpec,heightMeasureSpec);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildren();
    }

    private void layoutChildren() {

        int offsetY = (int) indicator.getOffsetY();

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        if (mHeaderView!=null){
            BaseContainer.MyLayoutParams lp = (MyLayoutParams) mHeaderView.getLayoutParams();
            //LayoutParams lp = mHeaderView.getLayoutParams();
            final int left = paddingLeft;
            final int top = paddingTop  + offsetY - mHeaderView.getMeasuredHeight();
            final int right = left + mHeaderView.getMeasuredWidth();
            final int bottom = top + mHeaderView.getMeasuredHeight();
            if (DEBUG && DEBUG_LAYOUT) {
                Log.i(LOG_TAG, String.format("onLayout header: %s %s %s %s", left, top, right, bottom));
            }
            mHeaderView.layout(left,top,right,bottom);
        }

        if (mContentView!=null){
            BaseContainer.MyLayoutParams lp = (MyLayoutParams) mContentView.getLayoutParams();
            final int left = paddingLeft;
            final int top = paddingTop  + offsetY;
            final int right = left + mContentView.getMeasuredWidth();
            final int bottom = top + mContentView.getMeasuredHeight();
            if (DEBUG && DEBUG_LAYOUT) {
                Log.i(LOG_TAG, String.format("onLayout content: %s %s %s %s", left, top, right, bottom));
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

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            updateView(indicator.getOffsetY() - mScroller.getCurrY());
            //Log.i("mScroller.getoffset()", String.valueOf(mScroller.getCurrY() - indicator.getOffsetY()));
            indicator.setOffsetY(mScroller.getCurrY());
            invalidate();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:

                Log.i("press down lo", event.getX() + " " + event.getY() + " ");
                indicator.setLastPos(event.getX(), event.getY());
                mScroller.forceFinished(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!ViewUtil.checkCanPullDown(mContentView)){
                    return false;
                }
                indicator.onMove(event.getX(), event.getY());
                updateView(indicator.getOffsetY());
                break;
            case MotionEvent.ACTION_UP:
                indicator.setLastPos(event.getX(), event.getY());
                resetLocation();
                break;
        }

        return true;
    }

    private void resetLocation() {
        int marginTop = mContentView.getTop();

        mScroller.startScroll(0, 0, 0,
                marginTop, 2000);
        indicator.setOffsetY(0);
        Log.i("Scroller-startY:", "" + mScroller.getStartY());
        Log.i("Scroller-CurrY:", "" + mScroller.getCurrY());
        Log.i("finalY", "" + mScroller.getFinalY());
        Log.i("timePassed", "" + mScroller.timePassed());
        Log.i("duration", "" + mScroller.getDuration());
        postInvalidate();

    }

    private void updateView(float y) {
        mContentView.offsetTopAndBottom((int) y);
        mHeaderView.offsetTopAndBottom((int) y);
        invalidate();
    }


    public void setHeaderView(View headerView){
        BaseContainer.MyLayoutParams lp = (MyLayoutParams) headerView.getLayoutParams();
        if (lp == null) {
            lp = new MyLayoutParams(-1, -2);
            headerView.setLayoutParams(lp);
        }
        mHeaderView = headerView;
        addView(headerView);
    }



    public static class MyLayoutParams extends ViewGroup.LayoutParams{

        int marginLeft;
        int marginRight;

        public MyLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.BaseContainer);
            marginLeft = typedArray.getDimensionPixelSize(R.styleable.BaseContainer_layoutMarginLeft,0);
            marginRight = typedArray.getDimensionPixelSize(R.styleable.BaseContainer_layoutMarginRight,0);
            Log.i("mylayoutparams","marginLeft marginRight "+marginLeft+" "+marginRight);
            typedArray.recycle();
        }

        public MyLayoutParams(int width, int height) {
            super(width, height);
        }

        public MyLayoutParams(LayoutParams source) {
            super(source);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MyLayoutParams(this.getContext(),attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MyLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MyLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    }
}

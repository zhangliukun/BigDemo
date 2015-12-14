package com.zlk.bigdemo.freeza.widget.pullrefreshview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.util.ViewUtil;

/**
 * Created by zale on 2015/12/8.
 */
public class BaseContainer extends ViewGroup{


    private static final int STATUS_PULL =0;//下拉状态
    private static final int STATUS_REFRESH =1;//下拉状态

    private View mContentView;
    private View mHeaderView;
    private static final boolean DEBUG_LAYOUT = true;
    public static boolean DEBUG = true;
    private static int ID = 1;
    protected final String LOG_TAG = "zale-frame-" + ++ID;

    private Indicator indicator;
    private Scroller mScroller;
    private HeaderInterface headerInterface;
    int contentMarginTop =0;




    private boolean mHasSendCancelEvent = false;//是否已经发送了取消事件
    private boolean mHasSendDownEvent =false;//是否已经发送了点击事件

    private MotionEvent mLastMotionEvent;

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
                if (checkIsBeingDraged()){
                    Log.i("isBeingDraged", String.valueOf(checkIsBeingDraged()));
                    return true;
                }
                return super.dispatchTouchEvent(event);
            case MotionEvent.ACTION_MOVE:
                headerInterface.onUIPositionChange(indicator);
                mLastMotionEvent = event;
                if (!mHasSendCancelEvent&&indicator.getOffsetY()>0&&checkIsBeingDraged()){
                    sendCancelEvent();
                }

                indicator.onMove(event.getX(), event.getY());
                //Log.i("getOffsetY,CanPullDown", indicator.getOffsetY() + " " + ViewUtil.checkCanPullDown(mContentView));
                if (indicator.getOffsetY()>=0&&ViewUtil.checkCanPullDown(mContentView)) {
                    mHasSendDownEvent = false;
                    updateView(indicator.getOffsetY());
                    return true;
                }
                if (indicator.getOffsetY()<0&&checkIsBeingDraged()){
                    if (-indicator.getOffsetY()>contentMarginTop){
                        updateView(-contentMarginTop);
                    }else {
                        updateView(indicator.getOffsetY());
                    }
                    return true;
                }
                if (!mHasSendDownEvent&&!checkIsBeingDraged()){
                    Log.i("sendDownEvent", indicator.getOffsetY()+" "+checkIsBeingDraged());
                    mHasSendDownEvent = true;
                    sendDownEvent();
                }
                //Log.i("action_move_disp","dispatchevent");
                return super.dispatchTouchEvent(event);

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mHasSendCancelEvent = false;
                mHasSendDownEvent = false;
                indicator.setLastPos(event.getX(), event.getY());
                resetLocation();
                return super.dispatchTouchEvent(event);
        }

        return true;
    }

    private void resetLocation() {
        contentMarginTop = mContentView.getTop();

        mScroller.startScroll(0, 0, 0,
                contentMarginTop, 2000);
        indicator.setOffsetY(0);
//        Log.i("Scroller-startY:", "" + mScroller.getStartY());
//        Log.i("Scroller-CurrY:", "" + mScroller.getCurrY());
//        Log.i("finalY", "" + mScroller.getFinalY());
//        Log.i("timePassed", "" + mScroller.timePassed());
//        Log.i("duration", "" + mScroller.getDuration());
        postInvalidate();

    }

    private void sendCancelEvent() {
        if (mLastMotionEvent == null) {
            return;
        }
        mHasSendCancelEvent = true;
        MotionEvent last = mLastMotionEvent;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime() + ViewConfiguration.getLongPressTimeout(), MotionEvent.ACTION_CANCEL, last.getX(), last.getY(), last.getMetaState());
        super.dispatchTouchEvent(e);
    }

    private void sendDownEvent() {
        final MotionEvent last = mLastMotionEvent;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime(), MotionEvent.ACTION_DOWN, last.getX(), last.getY(), last.getMetaState());
        super.dispatchTouchEvent(e);
    }

    private boolean checkIsBeingDraged(){
        contentMarginTop = mContentView.getTop();
        if (contentMarginTop<=0){
            return false;
        }
        return true;
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

        headerInterface = (HeaderInterface) headerView;
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

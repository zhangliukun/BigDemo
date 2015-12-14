package com.zlk.bigdemo.freeza.widget.pullrefreshview;

import android.util.Log;

/**
 * Created by zale on 2015/12/8.
 */
public class Indicator {

    private float mOffsetX;
    private float mOffsetY;

    private int mLastPosX = 0;
    private int mLastPosY = 0;

    private int mCurrentY = 0;

    private boolean mIsUnderTouch = false;

    private int mHeaderHeight;

    public int getCurrentY() {
        return mCurrentY;
    }

    public void setCurrentY(int mCurrentY) {
        this.mCurrentY = mCurrentY;
    }

    private void setOffset(float x, float y) {
        mOffsetX = x - mLastPosX;
        mOffsetY = y - mLastPosY;
    }

    public void onMove(float x, float y){
        Log.i("indicator","move");
        setOffset(x,y);
        setLastPos(x,y);

    }

    public float getOffsetY(){
        return mOffsetY;
    }


    public void setOffsetY(float offsetY){
        mOffsetY = offsetY;
    }

    public void setLastPos(float x,float y){
        mLastPosX = (int) x;
        mLastPosY = (int) y;
    }

    public void setHeaderHeight(int height){
        mHeaderHeight = height;
    }

    public int getHeaderHeight(){
        return mHeaderHeight;
    }





}

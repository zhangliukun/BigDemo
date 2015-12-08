package com.zlk.bigdemo.freeza.widget.pullrefreshview;

/**
 * Created by zale on 2015/12/8.
 */
public class Indicator {

    private float mOffsetX;
    private float mOffsetY;
    private int mCurrentPosX = 0;
    private int mCurrentPosY = 0;
    private int mLastPosX = 0;
    private int mLastPosY = 0;
    private int mPressedPosX = 0;
    private int mPressedPosY = 0;

    private void setOffset(float x, float y) {
        mOffsetX = x - mPressedPosX;
        mOffsetY = y - mPressedPosY;
    }

    public void setCurrentPos(float x,float y){
        mCurrentPosX = (int) x;
        mCurrentPosY = (int) y;
        setOffset(x,y);
    }

    public float getOffsetY(){
        return mOffsetY;
    }

    public void setPressedPos(float x,float y){
        mPressedPosX = (int) x;
        mPressedPosY = (int) y;
    }

    public void setLastPos(float x,float y){
        mLastPosX = (int) x;
        mLastPosY = (int) y;
    }



}

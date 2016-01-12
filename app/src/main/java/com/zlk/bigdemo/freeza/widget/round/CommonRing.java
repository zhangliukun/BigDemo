package com.zlk.bigdemo.freeza.widget.round;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zlk.bigdemo.R;


/**
 * Created by zale on 15/12/22.
 */
public class CommonRing extends View{


    private Paint mPaint;
    private int roundCenter;
    private int radius;

    private int roundColor;
    private int roundProgressColor;
    private float roundWidth;
    private int progressMax;
    private int progressCurrent;
    private int style;

    private Bitmap bitmap;


    public CommonRing(Context context) {
        this(context, null);
    }

    public CommonRing(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        mPaint = new Paint();

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonRing);
        roundColor = mTypedArray.getColor(R.styleable.CommonRing_commonRingRoundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.CommonRing_commonRingRoundProgressColor, Color.GREEN);
        roundWidth = mTypedArray.getDimension(R.styleable.CommonRing_commonRingRoundWidth, 5);
        progressMax = mTypedArray.getInteger(R.styleable.CommonRing_commonRingMax, 100);
        style = mTypedArray.getInt(R.styleable.CommonRing_commonRingType, 0);

        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initial();
        drawCircle(canvas);
        drawOval(canvas);
        drawCenterBitmap(canvas);
    }

    private void drawCenterBitmap(Canvas canvas) {
        if (this.bitmap!=null){
            //canvas.drawBitmap(this.bitmap,0,0,mPaint);
            Rect fitRect = new Rect(0,0,getWidth(),getHeight());
            canvas.drawBitmap(this.bitmap,null,fitRect,mPaint);
        }
    }

    private void initial() {
        roundCenter = getWidth() / 2;
        radius = (int) ((roundCenter-roundWidth/2));
    }

    private void drawOval(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(roundWidth);
        mPaint.setColor(roundProgressColor);
        mPaint.setAntiAlias(true);
        RectF oval = new RectF(roundCenter - radius,roundCenter-radius,roundCenter + radius,roundCenter + radius);
        canvas.drawArc(oval,-90,90,false,mPaint);
    }

    private void drawCircle(Canvas canvas) {
        mPaint.setColor(roundColor);
        mPaint.setStrokeWidth(roundWidth);
        if (style == 0){
            mPaint.setStyle(Paint.Style.FILL);
        }else if (style == 1){

            mPaint.setStyle(Paint.Style.STROKE);
        }
        canvas.drawCircle(roundCenter, roundCenter, radius, mPaint);
    }

    public void setCenterBitmap(int resourceId) {
        this.bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
    }

    public int getRoundColor() {
        return roundColor;
    }

    public void setRoundColor(int roundColor) {
        this.roundColor = roundColor;
    }

    public int getRoundProgressColor() {
        return roundProgressColor;
    }

    public void setRoundProgressColor(int roundProgressColor) {
        this.roundProgressColor = roundProgressColor;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public  int getProgressMax() {
        return progressMax;
    }

    public  void setProgressMax(int progressMax) {
        this.progressMax = progressMax;
    }

    public int getProgressCurrent() {
        return progressCurrent;
    }

    public synchronized void setProgressCurrent(int progressCurrent) {
        this.progressCurrent = progressCurrent;
    }
}

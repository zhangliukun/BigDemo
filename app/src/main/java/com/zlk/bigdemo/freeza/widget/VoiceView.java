package com.zlk.bigdemo.freeza.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zlk.bigdemo.R;


/**
 * 语音view
 * Created by Yetwish on 2015/9/19.
 */
public class VoiceView extends View {

    public static final int MAX_LEVEL = 10;
    public static final int MIN_LEVEL = 1;

    private int[] mVoice;
    private int[] listens;

    private int mSize;

    private Paint mPaint;

    private int mLineColor;

    private int mListenColor;

    private int mMaxHeight;

    private int mLineWidth;

    private int mPadding;

    private static final int LEVEL_NONE = 0;

    public static final int MODE_LEFT = 0x01;
    public static final int MODE_RIGHT = 0x02;

    private int mMode = MODE_RIGHT;

    private boolean isListen;

    private int progress = -1;

    public VoiceView(Context context) {
        this(context, null);
    }

    public VoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VoiceView);
        fetchAttr(ta);
        ta.recycle();
        init();
    }

    private void init() {
        mVoice = new int[60 * 20];
        mSize = 0;
        mPaint = new Paint();
        mPaint.setColor(mLineColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mLineWidth);
    }

    private void fetchAttr(TypedArray ta) {
        mMaxHeight = ta.getDimensionPixelSize(R.styleable.VoiceView_maxHeight, 0);
        mLineColor = ta.getColor(R.styleable.VoiceView_lineColor, getResources().getColor(R.color.voice_normal));
        mListenColor = ta.getColor(R.styleable.VoiceView_lineColor, getResources().getColor(R.color.voice_listen));
        mLineWidth = ta.getDimensionPixelSize(R.styleable.VoiceView_lineWidth, getResources().getDimensionPixelSize(R.dimen.voice_line_width));
        mPadding = mLineWidth;
    }


    /**
     * 设置延伸模式
     *
     * @param mode
     */
    public void setMode(int mode) {
        if (mode != MODE_LEFT && mode != MODE_RIGHT) return;
        mMode = mode;
    }

    /**
     * 获取语音元素数组
     *
     * @return
     */
    public int[] getVoiceArray() {
        return mVoice;
    }

    /**
     * 添加一格语言
     *
     * @param voice
     */
    public void addVoice(int voice) {
        if (voice > MAX_LEVEL || voice < MIN_LEVEL) {
            throw new IllegalArgumentException("The level of voice must between the maxLevel, cur is "
                    + MAX_LEVEL + " and " + MIN_LEVEL);
        }
        synchronized (this) {
            mVoice[mSize] = voice;
            mSize++;
            invalidate();
        }
    }

    /**
     * 清楚view状态
     */
    public void clear() {
        for (int i = 0; i <= mSize; i++) {
            mVoice[i] = LEVEL_NONE;
        }
        isListen = false;
        mSize = 0;
    }

    /**
     * 试听
     */
    public void listen() {
        if (mSize == 0) return;
        isListen = true;
        int width = mSize * mLineWidth + mPadding * mSize;
        if (width > getWidth()) {
            float radio = width * 1.0f / getWidth();
            int size = (int) Math.floor(mSize * 1.0 / radio);
            listens = new int[size];
            for (int i = 0; i < size; i++) {
                listens[i] = mVoice[(int) Math.floor(i * radio)];
            }
        }else {
            listens = new int[mSize];
            for (int i = 0; i < mSize; i++) {
                listens[i] = mVoice[i];
            }
        }
    }

    public void onProgress(int progress) {
        isListen = true;
        this.progress = progress * (listens.length - 1) / 100;
        invalidate();
    }


    public void onComplete() {
        isListen = false;
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mSize == 0) return;
        if (mMaxHeight == 0) mMaxHeight = getHeight();
        int x = 0, y = 0;
        if (isListen) {
            if (listens == null) return;
            mPaint.setColor(mLineColor);
            for (int i = 0; i <= listens.length - 1; i++) {
                if (mMode == MODE_RIGHT) {
                    x = i * mLineWidth + mPadding * i;
                } else {
                    x = getWidth() - (i * mLineWidth + mPadding * i);
                }
                y = (getHeight() - listens[i] * mMaxHeight / MAX_LEVEL) / 2;
                canvas.drawLine(x, y, x, y + listens[i] * mMaxHeight / MAX_LEVEL, mPaint);
            }
            mPaint.setColor(mListenColor);
            for (int i = 0; i <= progress; i++) {
                if (mMode == MODE_RIGHT) {
                    x = i * mLineWidth + mPadding * i;
                } else {
                    x = getWidth() - (i * mLineWidth + mPadding * i);
                }
                y = (getHeight() - listens[i] * mMaxHeight / MAX_LEVEL) / 2;
                canvas.drawLine(x, y, x, y + listens[i] * mMaxHeight / MAX_LEVEL, mPaint);
            }
        } else {
            mPaint.setColor(mLineColor);
            for (int i = mSize; i >= 0; i--) {
                if (mMode == MODE_RIGHT) {
                    x = (mSize - i) * mLineWidth + mPadding * (mSize - i);
                } else {
                    x = getWidth() - ((mSize - i) * mLineWidth + mPadding * (mSize - i));
                }
                y = (getHeight() - mVoice[i] * mMaxHeight / MAX_LEVEL) / 2;
                canvas.drawLine(x, y, x, y + mVoice[i] * mMaxHeight / MAX_LEVEL, mPaint);
            }
        }
    }
}

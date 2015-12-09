package com.zlk.bigdemo.freeza.widget.pullrefreshview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zlk.bigdemo.R;

/**
 * Created by zale on 2015/12/8.
 */
public class MyContent extends RelativeLayout {

    private View mContent;

    public MyContent(Context context) {
        this(context, null);
    }

    public MyContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContent = LayoutInflater.from(context).inflate(R.layout.item_content,this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}

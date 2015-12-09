package com.zlk.bigdemo.freeza.widget.pullrefreshview.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.zlk.bigdemo.R;

/**
 * Created by zale on 2015/12/9.
 */
public class SimpleHeader extends RelativeLayout{


    public SimpleHeader(Context context) {
        this(context, null);
    }

    public SimpleHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View headView = LayoutInflater.from(context).inflate(R.layout.item_simple_headview,this);
    }


}

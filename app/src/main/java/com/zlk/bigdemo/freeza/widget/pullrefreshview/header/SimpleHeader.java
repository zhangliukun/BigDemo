package com.zlk.bigdemo.freeza.widget.pullrefreshview.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.HeaderInterface;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.Indicator;

/**
 * Created by zale on 2015/12/9.
 */
public class SimpleHeader extends RelativeLayout implements HeaderInterface{


    private TextView stateTV;


    public SimpleHeader(Context context) {
        this(context, null);
    }

    public SimpleHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View headView = LayoutInflater.from(context).inflate(R.layout.item_simple_headview,this);
        stateTV = (TextView)headView.findViewById(R.id.state);
    }


    @Override
    public void onUIReset() {
        stateTV.setText("下拉刷新");
    }

    @Override
    public void onUIRefreshPerpare() {

    }

    @Override
    public void onUIRefreshBegin() {

    }

    @Override
    public void onUIRefreshComplete() {

    }

    @Override
    public void onUIPositionChange(Indicator indicator) {
        stateTV.setText("位置已经更新");
    }
}

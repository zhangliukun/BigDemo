package com.zlk.bigdemo.freeza.widget.pullrefreshview.header;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.HeaderInterface;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.Indicator;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.RefreshCallBack;

/**
 * Created by zale on 2015/12/9.
 */
public class SimpleHeader extends RelativeLayout implements HeaderInterface {


    private TextView stateTV;


    public SimpleHeader(Context context) {
        this(context, null);
    }

    public SimpleHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View headView = LayoutInflater.from(context).inflate(R.layout.item_simple_headview, this);
        stateTV = (TextView) headView.findViewById(R.id.state);
    }


    @Override
    public void onUIReset() {
        stateTV.setText("下拉刷新");
    }

    @Override
    public void onUIRefreshPerpare() {
        stateTV.setText("释放以刷新");
    }

    @Override
    public void onUIRefreshBegin() {
        stateTV.setText("正在刷新");
    }

    @Override
    public void onUIRefreshComplete() {
        stateTV.setText("更新完成");
    }

    @Override
    public void onUIPositionChange(Indicator indicator, boolean isOnTouch, RefreshCallBack refreshCallBack) {
        Log.i("onUIPositionChange",indicator.getPullStatus() + " " + isOnTouch + indicator.getCurrentY());
        if (indicator.getPullStatus() == Indicator.STATUS_REFRESH||indicator.getPullStatus()==Indicator.STATUS_REFRESH_COMPLETE){
            return;
        }
        if (isOnTouch) {
            if (indicator.getCurrentY() < indicator.getHeaderHeight() && indicator.getPullStatus() != Indicator.STATUS_PULL) {
                indicator.setPullStatus(Indicator.STATUS_PULL);
                onUIReset();

            }
            if (indicator.getCurrentY()>=indicator.getHeaderHeight()&&indicator.getPullStatus() != Indicator.STATUS_PULL_PREPARE) {
                indicator.setPullStatus(Indicator.STATUS_PULL_PREPARE);
                onUIRefreshPerpare();
            }
        } else {
            if (indicator.getPullStatus() == Indicator.STATUS_PULL_PREPARE) {
                indicator.setPullStatus(Indicator.STATUS_REFRESH);
                onUIRefreshBegin();
                refreshCallBack.onRefreshBegin();

            }

        }


    }
}

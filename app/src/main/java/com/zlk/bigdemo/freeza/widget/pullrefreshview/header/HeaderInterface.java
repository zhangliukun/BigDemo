package com.zlk.bigdemo.freeza.widget.pullrefreshview.header;

import com.zlk.bigdemo.freeza.widget.pullrefreshview.indicator.Indicator;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.RefreshCallBack;

/**
 * Created by zale on 2015/12/14.
 */
public interface HeaderInterface {

    public void onUIReset(Indicator indicator);

    public void onUIRefreshPerpare(Indicator indicator);

    public void onUIRefreshBegin(Indicator indicator);

    public void onUIRefreshComplete(Indicator indicator);

    public void onUIPositionChange(Indicator indicator,boolean isOnTouch,RefreshCallBack callBack);

}
package com.zlk.bigdemo.freeza.widget.pullrefreshview;

/**
 * Created by zale on 2015/12/14.
 */
public interface HeaderInterface {

    public void onUIReset();

    public void onUIRefreshPerpare();

    public void onUIRefreshBegin();

    public void onUIRefreshComplete();

    public void onUIPositionChange(Indicator indicator,boolean isOnTouch,RefreshCallBack callBack);

}

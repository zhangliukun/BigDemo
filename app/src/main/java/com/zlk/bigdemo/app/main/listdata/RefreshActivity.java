package com.zlk.bigdemo.app.main.listdata;

import android.os.Bundle;
import android.os.Handler;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.app.BaseActivity;
import com.zlk.bigdemo.freeza.Freeza;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.BaseContainer;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.RefreshCallBack;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.header.SimpleHeader;


/**
 * Created by zale on 2015/12/4.
 */
public class RefreshActivity extends BaseActivity{

    BaseContainer mBaseContainer;
    SimpleHeader simpleHeader;
    Handler mHandler = Freeza.getInstance().getHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_container);

        initView();
    }

    private void initView() {
        mBaseContainer = (BaseContainer) findViewById(R.id.base_container);
        simpleHeader = new SimpleHeader(this);
        mBaseContainer.setHeaderView(simpleHeader);
        mBaseContainer.setRefreshCallBack(new RefreshCallBack() {

            @Override
            public void onRefreshBegin() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBaseContainer.refreshComplete();
                    }
                }, 2000);
            }
        });




    }
}

package com.zlk.bigdemo.app.main.listdata;

import android.os.Bundle;
import android.os.Handler;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.app.BaseActivity;
import com.zlk.bigdemo.freeza.Freeza;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.PullContainer;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.RefreshCallBack;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.header.header.impl.SimpleHeader;


/**
 * Created by zale on 2015/12/4.
 */
public class RefreshActivity extends BaseActivity{

    PullContainer mPullContainer;
    SimpleHeader simpleHeader;
    Handler mHandler = Freeza.getInstance().getHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_container);

        initView();
    }

    private void initView() {
        mPullContainer = (PullContainer) findViewById(R.id.base_container);
        simpleHeader = new SimpleHeader(this);
        mPullContainer.setHeaderView(simpleHeader);
        mPullContainer.setRefreshCallBack(new RefreshCallBack() {

            @Override
            public void onRefreshBegin() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullContainer.refreshComplete();
                    }
                }, 2000);
            }
        });
        mPullContainer.setPullResistance(2.8f);




    }
}

package com.zlk.bigdemo.app.main.listdata;

import android.os.Bundle;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.app.BaseActivity;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.BaseContainer;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.header.SimpleHeader;

/**
 * Created by zale on 2015/12/4.
 */
public class RefreshActivity extends BaseActivity{

    BaseContainer mBaseContainer;
    SimpleHeader simpleHeader;

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
    }
}

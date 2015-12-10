package com.zlk.bigdemo.app.main.listdata;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.app.BaseActivity;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.BaseContainer;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.header.SimpleHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zale on 2015/12/10.
 */
public class RefreshListActivity extends BaseActivity{


    BaseContainer mBaseContainer;
    ListView mListView;
    List<String> dataList = new ArrayList<String>();
    SimpleHeader simpleHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_list);

        initView();
        initData();
    }

    private void initView() {
        mBaseContainer = (BaseContainer) findViewById(R.id.base_container);
        mListView = (ListView) findViewById(R.id.myListView);
        simpleHeader = new SimpleHeader(this);
        mBaseContainer.setHeaderView(simpleHeader);
    }

    private void initData() {
        for (int i=0;i<30;i++){
            dataList.add("test data");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        mListView.setAdapter(arrayAdapter);
    }
}

package com.zlk.bigdemo.app.main.listdata;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.app.BaseActivity;
import com.zlk.bigdemo.freeza.Freeza;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.BaseContainer;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.RefreshCallBack;
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

    Handler mHandler = Freeza.getInstance().getHandler();

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
        mBaseContainer.setRefreshCallBack(new RefreshCallBack() {

            @Override
            public void onRefreshBegin() {

                Log.i("network","Refresh Start");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBaseContainer.refreshComplete();
                        Log.i("network", "Refresh end");
                    }
                }, 8000);
            }
        });
    }

    private void initData() {
        for (int i=0;i<30;i++){
            dataList.add("test data");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        mListView.setAdapter(arrayAdapter);
    }

}

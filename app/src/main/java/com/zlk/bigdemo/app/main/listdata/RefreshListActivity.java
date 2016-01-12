package com.zlk.bigdemo.app.main.listdata;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.app.BaseActivity;
import com.zlk.bigdemo.freeza.Freeza;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.PullContainer;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.RefreshCallBack;
import com.zlk.bigdemo.freeza.widget.pullrefreshview.header.header.impl.SimpleHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zale on 2015/12/10.
 */
public class RefreshListActivity extends BaseActivity{


    PullContainer mPullContainer;
    ListView mListView;
    List<String> dataList = new ArrayList<String>();
    SimpleHeader simpleHeader;
    ArrayAdapter<String> arrayAdapter;
    Handler mHandler = Freeza.getInstance().getHandler();

    private int previousItemHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_list);

        initView();
        initData();
    }

    private void initView() {
        mPullContainer = (PullContainer) findViewById(R.id.base_container);
        mListView = (ListView) findViewById(R.id.myListView);
        simpleHeader = new SimpleHeader(this);
        mPullContainer.setHeaderView(simpleHeader);
        mPullContainer.setRefreshCallBack(new RefreshCallBack() {

            @Override
            public void onRefreshBegin() {

                previousItemHeight = simpleHeader.getHeight();

                Log.i("network", "Refresh Start");
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 4; i++) {
                            dataList.add("new data");
                        }
                        arrayAdapter.notifyDataSetChanged();

                        mPullContainer.refreshComplete();
                        Log.i("network", "Refresh end");
                    }
                }, 3000);
            }
        });
        mPullContainer.setPullResistance(2.8f);
    }

    private void initData() {
        for (int i=0;i<10;i++){
            dataList.add("test data");
        }
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        mListView.setAdapter(arrayAdapter);
    }


    //listview中的恢复成原来的位置
    private int getListItemHeight(int Position) {
        int height = 0;
        if (mListView.getAdapter() != null
                && Position >= 0
                && Position < mListView.getAdapter().getCount()
                + mListView.getHeaderViewsCount()) {
            View view = mListView.getAdapter().getView(Position, null, null);
            AbsListView.LayoutParams p = (AbsListView.LayoutParams) view.getLayoutParams();
            if (p == null) {
                p = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 0);
                view.setLayoutParams(p);
            }
            int childWidthSpec = ViewGroup.getChildMeasureSpec(View.MeasureSpec.UNSPECIFIED, p.width, AbsListView.LayoutParams.WRAP_CONTENT);
            int lpHeight = p.height;
            int childHeightSpec;
            if (lpHeight > 0) {
                childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
            } else {
                childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            }
            view.measure(childWidthSpec, childHeightSpec);

            height = view.getMeasuredHeight();
        }
        return height;
    }

    public void setListToPosition(int position) {
        if (mListView != null && mListView.getAdapter() != null) {
            int difference = previousItemHeight - getListItemHeight(position);
            mListView.setSelectionFromTop(position, mPullContainer.getHeight() + difference);
        }
    }

}

package com.zlk.bigdemo.freeza.widget.refreshlist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by zale on 2015/12/4.
 */
public class PullRefreshListView extends PullLayoutBase implements AbsListView.OnScrollListener{


    private ListView mListView;



    public PullRefreshListView(Context context) {
        super(context);
    }

    public PullRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public boolean isReadToPull() {
        return false;
    }
}

package com.zlk.bigdemo.app.main.record;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.app.BaseActivity;
import com.zlk.bigdemo.freeza.widget.FullScreenRecordView;

/**
 * Created by zale on 2015/11/13.
 */
public class RecordActivity extends BaseActivity {

    private Button button;

    private FullScreenRecordView fullScreenRecordView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initView();
    }

    private void initView() {
        fullScreenRecordView = (FullScreenRecordView) findViewById(R.id.full_screen_view);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView3);

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                fullScreenRecordView.setVisibility(View.VISIBLE);
                return true;
            }
        });

        fullScreenRecordView.setmRecordStatusListener(new FullScreenRecordView.onRecordStatusListener() {
            @Override
            public void startRecord() {
                fullScreenRecordView.setVisibility(View.VISIBLE);
            }
            @Override
            public void endRecord(String recordPath, int recordTime,boolean isSend) {
                fullScreenRecordView.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        fullScreenRecordView.getOnTouchEvent(event);
        return true;
    }

}

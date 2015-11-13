package com.zlk.bigdemo.app.main.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.app.BaseActivity.OnActivityResultListener;
import com.zlk.bigdemo.app.BaseFragment;
import com.zlk.bigdemo.app.main.MainActivity;
import com.zlk.bigdemo.freeza.widget.FullScreenRecordView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by ShineMo-177 on 2015/9/23.
 */
public class SecondFragment extends BaseFragment implements OnActivityResultListener {

    //广播意图标志
    public static final String DESKTOP_WIDGET_BROADCAST_ACTION = "com.zalezone.NOTE_UPDATE";
    //广播动作action
    public static final String DESKTOP_WIDGET_NEW_NOTE = "new_note";

    private String mTitle = "default";
    private String newNoteString = "";
    private Context mContext;

    @Bind(R.id.new_note)
    EditText newNoteEdit;
    @Bind(R.id.accept)
    Button acceptButton;
    @Bind(R.id.full_screen_view)
    FullScreenRecordView fullScreenRecordView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            mTitle = getArguments().getString(MainActivity.TITLE);
        }
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        mContext = view.getContext();
        ButterKnife.bind(this, view);

        initView();
        initData();


        return view;
    }

    @OnLongClick(R.id.accept)
    boolean showFullScreen() {
        fullScreenRecordView.setVisibility(View.VISIBLE);
        return false;
    }

    @OnClick(R.id.accept)
    void acceptNewNote() {
        if (!newNoteEdit.getText().equals("")) {
            newNoteString = newNoteEdit.getText().toString();
            Intent desktopIntent = new Intent(DESKTOP_WIDGET_BROADCAST_ACTION);
            desktopIntent.putExtra(DESKTOP_WIDGET_NEW_NOTE, newNoteString);
            mContext.sendBroadcast(desktopIntent);
        } else {
            showToast(getString(R.string.cannt_create_empty_note));
        }
    }

    private void initView() {

        fullScreenRecordView.setVisibility(View.GONE);


    }

    private void initData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

            }
        }
    }

}

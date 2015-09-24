package com.zlk.bigdemo.app.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zlk.bigdemo.R;
import com.zlk.bigdemo.app.BaseActivity.OnActivityResultListener;
import com.zlk.bigdemo.app.BaseFragment;
import com.zlk.bigdemo.app.main.MainActivity;
import com.zlk.bigdemo.app.utils.FileUtils;
import com.zlk.bigdemo.app.widget.selector.MultiPictureSelectorActivity;
import com.zlk.bigdemo.freeza.util.CameraUtils;
import com.zlk.bigdemo.freeza.util.ThumbnailUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ShineMo-177 on 2015/9/23.
 */
public class ThirdFragment extends BaseFragment implements OnActivityResultListener {

    private String  mTitle = "default";

    private Uri mAvatarUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            mTitle = getArguments().getString(MainActivity.TITLE);
        }

        View view = inflater.inflate(R.layout.fragment_three, container,false);
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onActivityResult
            (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

            }
        }
    }

}

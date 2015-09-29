package com.zlk.bigdemo.app.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zlk.bigdemo.R;
import com.zlk.bigdemo.android.volley.Request;
import com.zlk.bigdemo.android.volley.RequestQueue;
import com.zlk.bigdemo.android.volley.Response;
import com.zlk.bigdemo.android.volley.VolleyError;
import com.zlk.bigdemo.android.volley.toolbox.StringRequest;
import com.zlk.bigdemo.android.volley.toolbox.Volley;
import com.zlk.bigdemo.app.BaseActivity.OnActivityResultListener;
import com.zlk.bigdemo.app.BaseFragment;
import com.zlk.bigdemo.app.main.MainActivity;
import com.zlk.bigdemo.app.main.MyApplication;
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
public class SecondFragment extends BaseFragment implements OnActivityResultListener {

    private String  mTitle = "default";

    private Uri mAvatarUri;

    @Bind(R.id.webView)
    WebView webView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            mTitle = getArguments().getString(MainActivity.TITLE);
        }

        View view = inflater.inflate(R.layout.fragment_two, container,false);
        ButterKnife.bind(this,view);

        initView();
        initData();




        return view;
    }

    private void initView() {

        webView.getSettings().setDefaultTextEncodingName("UTF-8");//设置默认为utf-8
        webView.getSettings().setJavaScriptEnabled(true);

    }

    private void initData() {
        RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance());
        String url = "http://www.baidu.com";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast(String.valueOf(error));
            }
        });
        requestQueue.add(stringRequest);
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

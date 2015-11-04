package com.zlk.bigdemo.app.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.app.BaseActivity.OnActivityResultListener;
import com.zlk.bigdemo.app.BaseFragment;
import com.zlk.bigdemo.app.main.MainActivity;
import com.zlk.bigdemo.app.main.adapter.ImageAdapter;
import com.zlk.bigdemo.app.main.model.ImageItem;
import com.zlk.bigdemo.freeza.widget.selector.MultiPictureSelectorActivity;
import com.zlk.bigdemo.freeza.util.CameraUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ShineMo-177 on 2015/9/23.
 */
public class ThirdFragment extends BaseFragment implements OnActivityResultListener {

    private String mTitle = "default";
    private ArrayList<ImageItem> gridViewList;
    private ImageAdapter imageAdapter;

    @Bind(R.id.listView)
    ListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            mTitle = getArguments().getString(MainActivity.TITLE);
        }

        View view = inflater.inflate(R.layout.fragment_three, null, false);
        ButterKnife.bind(this, view);

        initView(view);

        return view;
    }

    private void initView(View view) {

        gridViewList = new ArrayList<ImageItem>();
        gridViewList.add(new ImageItem("1", "http://img1.imgtn.bdimg.com/it/u=3507519196,1882157449&fm=21&gp=0.jpg", "hello fresco"));
        gridViewList.add(new ImageItem("2", "http://img5.imgtn.bdimg.com/it/u=3767258876,282061377&fm=21&gp=0.jpg", "hello fresco"));
        gridViewList.add(new ImageItem("3", "http://b.hiphotos.baidu.com/image/pic/item/342ac65c10385343228e37fd9713b07ecb8088ad.jpg", "hello fresco"));
        gridViewList.add(new ImageItem("4", "http://g.hiphotos.baidu.com/image/pic/item/4034970a304e251fbb065424a386c9177e3e537c.jpg", "hello fresco"));
        gridViewList.add(new ImageItem("5", "http://g.hiphotos.baidu.com/image/pic/item/63d0f703918fa0ecae34d4d1229759ee3c6ddb7f.jpg", "hello fresco"));
        gridViewList.add(new ImageItem("6", "http://img4.imgtn.bdimg.com/it/u=434281914,1810736344&fm=21&gp=0.jpg", "hello fresco"));
        gridViewList.add(new ImageItem("7", "http://img1.imgtn.bdimg.com/it/u=692244592,2401325307&fm=21&gp=0.jpg", "hello fresco"));
        gridViewList.add(new ImageItem("8", "http://img3.imgtn.bdimg.com/it/u=2484202869,2302280260&fm=21&gp=0.jpg", "hello fresco"));
        gridViewList.add(new ImageItem("9", "http://img5.imgtn.bdimg.com/it/u=2645226784,1850525231&fm=21&gp=0.jpg", "hello fresco"));
        gridViewList.add(new ImageItem("10", "http://img0.imgtn.bdimg.com/it/u=966019619,3537283481&fm=21&gp=0.jpg", "hello fresco"));


        imageAdapter = new ImageAdapter(view.getContext(), gridViewList);
        listView.setAdapter(imageAdapter);
//        Uri uri = Uri.parse("http://img1.imgtn.bdimg.com/it/u=3507519196,1882157449&fm=21&gp=0.jpg");
//

    }

    @Override
    public void onActivityResult
            (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CameraUtils.REQUEST_PHOTO_LIBRARY:
                    String[] paths = data.getStringArrayExtra(MultiPictureSelectorActivity.RET_KEY);
                    if (paths != null && paths.length > 0) {
                        for (int i=0;i<paths.length;i++){
                            gridViewList.add(new ImageItem(String.valueOf(i),"file://"+paths[i],"my photo"));
                        }
                    }
                    imageAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

}

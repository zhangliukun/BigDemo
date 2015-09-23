package com.zlk.bigdemo.application.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.zlk.bigdemo.R;
import com.zlk.bigdemo.application.main.MainActivity;
import com.zlk.bigdemo.application.widget.selector.MultiPictureSelectorActivity;
import com.zlk.bigdemo.freeza.util.CameraUtils;
import com.zlk.bigdemo.freeza.util.FileUtils;
import com.zlk.bigdemo.freeza.util.ThumbnailUtils;

import java.io.File;
import java.net.URI;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ShineMo-177 on 2015/9/23.
 */
public class MyFragment extends Fragment {

    private String  mTitle = "default";

    private Uri mAvatarUri;

    @Bind(R.id.headImage)
    ImageView headImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            mTitle = getArguments().getString(MainActivity.TITLE);
        }

        View view = inflater.inflate(R.layout.fragment_my, container,false);
        ButterKnife.bind(this,view);

        return view;
    }

//    @OnClick(R.id.headImage)
//    private void chooseHeadImage(){
//        MultiPictureSelectorActivity.startSingleActivity(getActivity(), MultiPictureSelectorActivity.REQUEST_CODE);
//    }

    private void setHeadImage(String path){

//        Uri uri = Uri.fromFile(new File(path));
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setControllerListener(controllerListener)
//                .setUri(uri).build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case MultiPictureSelectorActivity.REQUEST_CODE:
                    String[] path = data.getStringArrayExtra(MultiPictureSelectorActivity.RET_KEY);
                    Uri avatarUri = Uri.fromFile(new File(path[0]));
                    File output = FileUtils.newImageCacheFile(getActivity());
                    mAvatarUri = Uri.fromFile(output);
                    CameraUtils.startCropImageActivityForCamera(getActivity(), avatarUri, mAvatarUri);

                    break;
                case CameraUtils.REQUEST_CODE_IMAGE_CROP:
                    if (mAvatarUri != null) {
                        File avatar = ThumbnailUtils
                                .compressAndRotateToBitmapThumbFile(getActivity(),
                                        mAvatarUri, 800, 600);
                        mAvatarUri = null;
                        if (avatar != null) {
                            setHeadImage(avatar.getPath());
                        }
                    }
                    break;
            }
        }
    }

}

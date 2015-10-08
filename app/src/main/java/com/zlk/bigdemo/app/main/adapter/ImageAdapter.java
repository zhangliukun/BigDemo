package com.zlk.bigdemo.app.main.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zlk.bigdemo.R;
import com.zlk.bigdemo.app.main.model.ImageItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ShineMo-177 on 2015/10/8.
 */
public class ImageAdapter extends BaseAdapter {

    private ArrayList<ImageItem> list;
    private Context context;

    public ImageAdapter(Context context, ArrayList<ImageItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_imagelist, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        setImageView(holder, position);


        return convertView;
    }

    private void setImageView(ViewHolder holder, int position) {

        //设置圆角
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(7f);
        //设置圆圈
        //roundingParams.setRoundAsCircle(true);
        roundingParams.setOverlayColor(context.getResources().getColor(R.color.white));

        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setProgressBarImage(new ProgressBarDrawable())
                .setRoundingParams(roundingParams)
                .build();

        ControllerListener listener = new ControllerListener() {
            @Override
            public void onSubmit(String s, Object o) {

            }

            @Override
            public void onFinalImageSet(String s, Object o, Animatable animatable) {

            }

            @Override
            public void onIntermediateImageSet(String s, Object o) {

            }

            @Override
            public void onIntermediateImageFailed(String s, Throwable throwable) {

            }

            @Override
            public void onFailure(String s, Throwable throwable) {

            }

            @Override
            public void onRelease(String s) {

            }
        };

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(list.get(position).url))
                .setTapToRetryEnabled(true)
                .setOldController(holder.simpleDraweeView.getController())
                .setControllerListener(listener)
                .build();

        holder.simpleDraweeView.setHierarchy(hierarchy);
        holder.simpleDraweeView.setController(controller);
        holder.textView.setText(list.get(position).message);
    }

    static class ViewHolder {
        @Bind(R.id.simpleView)
        SimpleDraweeView simpleDraweeView;
        @Bind(R.id.textView)
        TextView textView;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

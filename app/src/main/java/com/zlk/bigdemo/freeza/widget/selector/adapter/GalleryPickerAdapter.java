package com.zlk.bigdemo.freeza.widget.selector.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.freeza.widget.selector.MultiPictureSelectorActivity;
import com.zlk.bigdemo.freeza.widget.selector.support.ImageItem;
import com.zlk.bigdemo.freeza.widget.selector.utils.AlbumManager;
import com.zlk.bigdemo.freeza.image.MagicImageView;
import com.zlk.bigdemo.freeza.widget.adapter.ViewHolder;


import java.util.List;

public class GalleryPickerAdapter extends BaseAdapter {

	private Context mContext;
	private List<ImageItem> mImagePathList;

	private int mImageWidth;
	private int mMode;
	private OnClickListener mSelectListener;
	private OnClickListener mOnClickListener;

	public GalleryPickerAdapter(Context context, List<ImageItem> imagePathList,
			OnClickListener listener, OnClickListener click, int mode) {
		mContext = context;
		mImagePathList = imagePathList;
		mSelectListener = listener;
		if (click == null){
			mOnClickListener = listener;
		} else {
			mOnClickListener = click;
		}
        mMode = mode;
		mImageWidth = (context.getResources().getDisplayMetrics().widthPixels - context
				.getResources()
				.getDimensionPixelSize(R.dimen.picture_select_padding) * 2) / 3;
	}

	public int getCount() {
		return mImagePathList.size();
	}

	public Object getItem(int position) {
		if (mImagePathList.isEmpty()) {
			return position;
		}
		return mImagePathList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.multi_picture_selector_selected_item, null);
		}
		LinearLayout layout = ViewHolder.get(convertView, R.id.layout);
		ImageView checkBox = ViewHolder.get(convertView, R.id.image_checkbox);
		MagicImageView image = ViewHolder.get(convertView,
				R.id.image_item);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				mImageWidth, mImageWidth);
		image.setLayoutParams(params);
		layout.setLayoutParams(params);
		ImageItem imageItem = mImagePathList.get(position);
		
		View camera = ViewHolder.get(convertView, R.id.multi_camera);
		camera.setLayoutParams(params);
		if(imageItem.getImageId() == 0 && TextUtils.isEmpty(imageItem.getImagePath()) && position == 0){
			checkBox.setVisibility(View.GONE);
			image.setVisibility(View.GONE);
			camera.setVisibility(View.VISIBLE);
			camera.setTag(position);
			camera.setOnClickListener(mOnClickListener);
		}else{
			camera.setVisibility(View.GONE);
			checkBox.setVisibility(View.VISIBLE);
			image.setVisibility(View.VISIBLE);
			image.loadLocalImage(imageItem.getImagePath(), AlbumManager.THUMBNAIL_SIZE, mContext.getResources().getDrawable(R.drawable.xx_ic_slt), null);
			image.setTag(position);
			image.setOnClickListener(mOnClickListener);
		}

        if(mMode == MultiPictureSelectorActivity.MODE_SINGLE){
            checkBox.setVisibility(View.GONE);
        }else{
            checkBox.setOnClickListener(mSelectListener);
            if (imageItem.isSelect() == 1) {
                checkBox.setImageResource(R.drawable.contacts_select);
				layout.setVisibility(View.VISIBLE);
            } else {
                checkBox.setImageResource(R.drawable.contacts_unselect);
				layout.setVisibility(View.GONE);
            }
            checkBox.setTag(position);
        }

		
		
		return convertView;
	}

}

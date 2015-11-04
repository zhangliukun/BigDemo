package com.zlk.bigdemo.freeza.widget.selector.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View.OnClickListener;

import com.zlk.bigdemo.freeza.widget.selector.fragment.ShowAlbumImageFragment;
import com.zlk.bigdemo.freeza.widget.selector.support.ImageItem;

import java.util.List;


public class ShowAlbumImageAdapter extends FragmentStatePagerAdapter {

	private List<ImageItem> mPictureList;
	private OnClickListener mClickListener;
	
	public ShowAlbumImageAdapter(FragmentManager fm, List<ImageItem> pictureList, OnClickListener listener) {
		super(fm);
		mPictureList = pictureList;
		mClickListener = listener;
	}

	@Override
	public Fragment getItem(int position) {
		ImageItem vo = mPictureList.get(position);
		ShowAlbumImageFragment f = ShowAlbumImageFragment.newInstance(vo.getImagePath());
		f.setOnClickListener(mClickListener);
		return f;
	}

	@Override
	public int getCount() {
		return mPictureList.size();
	}

}

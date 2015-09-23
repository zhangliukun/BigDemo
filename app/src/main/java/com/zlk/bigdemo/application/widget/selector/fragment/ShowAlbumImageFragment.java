package com.zlk.bigdemo.application.widget.selector.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.application.widget.selector.utils.AlbumManager;
import com.zlk.bigdemo.freeza.image.MagicImageView;


public class ShowAlbumImageFragment extends Fragment {
	
	private String mUrl;
	private OnClickListener mListener;

	public static ShowAlbumImageFragment newInstance(String url) {
		ShowAlbumImageFragment f = new ShowAlbumImageFragment();

		Bundle args = new Bundle();
		args.putString("url", url);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUrl = getArguments() != null ? getArguments().getString("url") : "";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.show_album_image_item, container,
				false);
//		final PhotoView imageView = (PhotoView) view.findViewById(R.id.show_album_image);
//		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//		imageView.loadLocalImage(mUrl, AlbumManager.THUMBNAIL_SIZE,null, new MagicImageView.OnImageLoaded() {
//
//			@Override
//			public void onLoaded() {
//				imageView.loadLocalImage(mUrl,AlbumManager.ORGIN_SIZE,imageView.getDrawable(),null);
//			}
//		});
//		imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//			@Override
//			public void onPhotoTap(View view, float x, float y) {
//				if(mListener != null){
//					mListener.onClick(view);
//				}
//			}
//		});
		return view;
	}
	
	public void setOnClickListener(OnClickListener listener){
		mListener = listener;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}

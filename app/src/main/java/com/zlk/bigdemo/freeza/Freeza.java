package com.zlk.bigdemo.freeza;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.zlk.bigdemo.android.volley.Request;
import com.zlk.bigdemo.android.volley.RequestQueue;
import com.zlk.bigdemo.android.volley.toolbox.ImageDiskCache;
import com.zlk.bigdemo.android.volley.toolbox.ImageLoader;
import com.zlk.bigdemo.android.volley.toolbox.Volley;
import com.zlk.bigdemo.framework.ApiCallback;
import com.zlk.bigdemo.freeza.image.cache.MagicImageCache;
import com.zlk.bigdemo.freeza.util.FileUtils;
import com.zlk.bigdemo.freeza.util.SharePrefsManager;

@SuppressWarnings("rawtypes")
public class Freeza {

	private static Freeza sInstance;
	private ImageLoader mImageLoader;
	private RequestQueue mHttpQueue;
	private RequestQueue mImageQueue;
	private ImageDiskCache mImageDiskCache;
	private MagicImageCache mImageCache;
	private Handler mHander = new Handler(Looper.getMainLooper());

	private Freeza() {
	}

	public static Freeza getInstance() {
		if (sInstance == null) {
			sInstance = new Freeza();
		}
		return sInstance;
	}

	public ImageLoader getImageLoader() {
		if (mImageLoader == null) {
			Context context = BaseApplication.getInstance();
			RequestQueue queue = Volley.newImageRequestQueue(context);
			if (mImageCache == null) {
				mImageCache = MagicImageCache.findOrCreateCache(context,
						FileUtils.getImageCachePath(context));
			}
			mImageLoader = new ImageLoader(queue, mImageCache);
			mImageQueue = queue;
			mImageLoader.setBatchedResponseDelay(0);
		}
		return mImageLoader;
	}

	public void addHttpRequest(final Request request, final ApiCallback callback) {
		getHttpQueue().add(request);
	}

	public void addHttpRequest(final Request request) {
		getHttpQueue().add(request);
	}

	public ImageDiskCache getImageDiskCache() {
		if (mImageDiskCache == null) {
			mImageDiskCache = new ImageDiskCache(BaseApplication.getInstance());
		}
		return mImageDiskCache;
	}

	public MagicImageCache getImageCache() {
		return mImageCache;
	}

	private RequestQueue getHttpQueue() {
		if (mHttpQueue == null) {
			mHttpQueue = Volley.newRequestQueue(BaseApplication.getInstance());
		}
		return mHttpQueue;
	}

	public void recycle() {
		if (mHttpQueue != null) {
			mHttpQueue.stop();
			mHttpQueue = null;
		}
		if (mImageQueue != null) {
			mImageQueue.stop();
			mImageQueue = null;
		}
		SharePrefsManager.getInstance().recycle();
		sInstance = null;
	}

	public Handler getHandler() {
		return mHander;
	}
}

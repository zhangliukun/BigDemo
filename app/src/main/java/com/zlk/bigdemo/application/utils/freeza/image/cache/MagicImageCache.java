/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zlk.bigdemo.application.utils.freeza.image.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.dragon.freeza.util.Md5Util;

/**
 * This class holds our bitmap caches (memory and disk).
 */
public class MagicImageCache implements ImageCache {

	// Default memory cache size
	private static final int DEFAULT_MEM_CACHE_SIZE = (int) (Runtime
			.getRuntime().maxMemory() / 12); //

	// Default disk cache size
	private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 100; // 100MB

	// Compression settings when writing images to disk cache
	private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
	private static final int DEFAULT_COMPRESS_QUALITY = 70;

	// Constants to easily toggle various caches
	private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
	private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
	private static final boolean DEFAULT_CLEAR_DISK_CACHE_ON_START = false;

	private LruCache<String, Bitmap> mMemoryCache;

	private static MagicImageCache imageCache;

	public MagicImageCache(Context context, ImageCacheParams cacheParams) {
		init(context, cacheParams);
	}

	/**
	 * Creating a new ImageCache object using the default parameters.
	 * 
	 * @param context
	 *            The context to use
	 * @param uniqueName
	 *            A unique name that will be appended to the cache directory
	 */
	public MagicImageCache(Context context, String uniqueName) {
		init(context, new ImageCacheParams(uniqueName));
	}

	/**
	 * Find and return an existing ImageCache stored in a {@link RetainFragment}
	 * , if not found a new one is created with defaults and saved to a
	 * {@link RetainFragment}.
	 * 
	 * @param activity
	 *            The calling {@link FragmentActivity}
	 * @param uniqueName
	 *            A unique name to append to the cache directory
	 * @return An existing retained ImageCache object or a new one if one did
	 *         not exist.
	 */
	public static MagicImageCache findOrCreateCache(final Context context,
			final String uniqueName) {
		return findOrCreateCache(context, new ImageCacheParams(uniqueName));
	}

	/**
	 * Find and return an existing ImageCache stored in a {@link RetainFragment}
	 * , if not found a new one is created using the supplied params and saved
	 * to a {@link RetainFragment}.
	 * 
	 * @param activity
	 *            The calling {@link FragmentActivity}
	 * @param cacheParams
	 *            The cache parameters to use if creating the ImageCache
	 * @return An existing retained ImageCache object or a new one if one did
	 *         not exist
	 */
	public static synchronized MagicImageCache findOrCreateCache(final Context context,
			ImageCacheParams cacheParams) {
		if (imageCache == null) {
			imageCache = new MagicImageCache(context, cacheParams);
		}
		return imageCache;
	}

	/**
	 * Initialize the cache, providing all parameters.
	 * 
	 * @param context
	 *            The context to use
	 * @param cacheParams
	 *            The cache parameters to initialize the cache
	 */
	private void init(Context context, ImageCacheParams cacheParams) {

		// Set up memory cache
		if (cacheParams.memoryCacheEnabled) {
			mMemoryCache = new LruCache<String, Bitmap>(
					cacheParams.memCacheSize) {
				/**
				 * Measure item size in bytes rather than units which is more
				 * practical for a bitmap cache
				 */
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					int size = bitmap.getRowBytes() * bitmap.getHeight();
					// WxLog.d("setting", "Bitmap size:" + size);
					return size;
				}
			};
		}
	}

	/**
	 * Get from memory cache.
	 * 
	 * @param data
	 *            Unique identifier for which item to get
	 * @return The bitmap if found in cache, null otherwise
	 */
	public Bitmap getBitmapFromMemCache(String data) {
		if (mMemoryCache != null) {
			final Bitmap memBitmap = mMemoryCache.get(data);
			if (memBitmap != null) {
				// if (BuildConfig.DEBUG) {
				// WxLog.d(TAG, "Memory cache hit");
				// }
				return memBitmap;
			}
		}
		return null;
	}

	/**
	 * Get from disk cache.
	 * 
	 * @param data
	 *            Unique identifier for which item to get
	 * @return The bitmap if found in cache, null otherwise
	 */
	public void clearCaches() {
		mMemoryCache.evictAll();
	}

	/**
	 * A holder class that contains cache parameters.
	 */
	public static class ImageCacheParams {
		public String uniqueName;
		public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
		public int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
		public CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
		public int compressQuality = DEFAULT_COMPRESS_QUALITY;
		public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
		public boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
		public boolean clearDiskCacheOnStart = DEFAULT_CLEAR_DISK_CACHE_ON_START;

		public ImageCacheParams(String uniqueName) {
			this.uniqueName = uniqueName;
		}
	}

	@Override
	public Bitmap getBitmap(final String url) {
		final String md5Name = Md5Util.getStringMD5(url);
		Bitmap memBitmap = getBitmapFromMemCache(md5Name);
		if (memBitmap != null) {
			return memBitmap;
		}
		return null;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		String md5Name = Md5Util.getStringMD5(url);
		if (md5Name == null || bitmap == null) {
			return;
		}

		if (mMemoryCache != null && mMemoryCache.get(md5Name) == null) {
			mMemoryCache.put(md5Name, bitmap);
		}
	}

	public void removeBitmap(String key) {
		if (TextUtils.isEmpty(key)) {
			return;
		}
		String md5Name = Md5Util.getStringMD5(key);

		if (mMemoryCache != null) {
			mMemoryCache.remove(md5Name);
		}
	}
}

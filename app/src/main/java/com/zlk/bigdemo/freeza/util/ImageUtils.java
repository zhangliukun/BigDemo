/*
 * Project: babylon_android_bidui_20120313
 * 
 * File Created at 2012-3-17
 * 
 * Copyright 2011 Alibaba.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */

package com.zlk.bigdemo.freeza.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

	/**
	 * 获得图片的exif信息
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static float rotationForImage(Context context, Uri uri) {
		Cursor c = null;
		try {
			if (uri.getScheme().equals("content")) {
				String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
				c = context.getContentResolver().query(uri, projection,
						null, null, null);
				if (c.moveToFirst()) {
					return c.getInt(0);
				}
			} else if (uri.getScheme().equals("file")) {
				try {
					ExifInterface exif = new ExifInterface(uri.getPath());
					int rotation = (int) exifOrientationToDegrees(exif
							.getAttributeInt(ExifInterface.TAG_ORIENTATION,
									ExifInterface.ORIENTATION_NORMAL));
					return rotation;
				} catch (IOException e) {
				}
			}
		} finally {
			if(c != null) {
				c.close();
				c = null;
			}
		}
		return 0f;
	}

	public static boolean writeBitmap(String path, String name, Bitmap bitmap,
			int compressRate) {
		if (null == bitmap || TextUtils.isEmpty(path)
				|| TextUtils.isEmpty(name))
			return false;
		boolean bPng = false;
		if (name.endsWith(".png")) {
			bPng = true;
		}

		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}

		File _file = new File(path, name);
		boolean bNew = true;
		if (_file.exists()) {
			bNew = false;
			_file = new File(path, name + ".tmp");
			_file.delete();
		}
		FileOutputStream fos = null;
		boolean bOK = false;
		try {
			fos = new FileOutputStream(_file);
			if (bPng) {
				bitmap.compress(CompressFormat.PNG, compressRate, fos);
			} else {
				bitmap.compress(CompressFormat.JPEG, compressRate, fos);
			}
			bOK = true;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
					if (bNew == false && bOK) {
						_file.renameTo(new File(path, name));
					}
				} catch (IOException e) {
				}
			}
		}
		return false;
	}

	public static boolean writeBitmap(String path, Bitmap bitmap,
			int compressRate) {
		if (null == bitmap || TextUtils.isEmpty(path))
			return false;

		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, compressRate, fos);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
		return false;
	}

	public static Bitmap decodeFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		try {
			FileInputStream fis = new FileInputStream(file);
			return decodeFile(fis, true);
		} catch (Exception e) {
		} catch (Throwable t) {
		}
		return null;
	}

	public static Bitmap decodeFile(FileInputStream fs,
			boolean bCloseAfterDecode) throws Exception {

		FileDescriptor fd = fs.getFD();

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(fd, null, options);
		if (options.mCancel || options.outWidth == -1
				|| options.outHeight == -1) {
			if (bCloseAfterDecode)
				fs.close();
			throw new Exception("invalid bitmap file: " + fs.toString());

		}
		int index = 1;
		int maxindex = Math.max(index, 20);

		for (int i = index; i <= maxindex; i++) {
			try {
				options.inJustDecodeBounds = false;
				options.inSampleSize = index;
				options.inDither = false;
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				Bitmap bm = BitmapFactory.decodeFileDescriptor(fd, null,
						options);
				if (null != bm) {
					if (bCloseAfterDecode)
						fs.close();

					return bm;
				}
			} catch (OutOfMemoryError e) {
			} catch (Exception e) {
			} catch (Throwable t) {
			}
		}
		if (bCloseAfterDecode)
			fs.close();
		throw new Exception("invalid bitmap file");

	}

	/**
	 * 获得原图片的角度
	 * 
	 * @param exifOrientation
	 * @return
	 */
	private static float exifOrientationToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}

	/**
	 * 旋转图片
	 * 
	 * @param context
	 * @param mImageUri
	 * @param bmp
	 * @return
	 */
	public static Bitmap rotateBitmap(Context context, Uri mImageUri, Bitmap bmp) {
		Matrix matrix = new Matrix();
		float rotation = ImageUtils.rotationForImage(context, mImageUri);
		if (rotation != 0f) {
			matrix.preRotate(rotation);
		}
		Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
				bmp.getHeight(), matrix, true);
		return resizedBitmap;
	}

	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			buffer = bos.toByteArray();
		} catch (Exception e) {
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
				}
			}

		}
		return buffer;
	}

}

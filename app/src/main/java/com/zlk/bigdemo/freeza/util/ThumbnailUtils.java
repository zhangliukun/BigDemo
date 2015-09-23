package com.zlk.bigdemo.freeza.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ThumbnailUtils {

	private static final int UNCONSTRAINED = -1;

	private static final int WIFI_WIDTH = 800;
	private static final int WIFI_HEIGHT = 1280;

	public static final int TARGET_SIZE = 640;

	public static final int WIFI_BITMAP_LENGTH = 110;

	public static Bitmap compressBitmapThumb(Context context, Uri uri,
			int targetSize) {

		Bitmap bm = compressFileToBitmapThumb(uri.getPath(), TARGET_SIZE);
		if (bm != null) {
			return bm;
		}
		return null;
	}

	public static Bitmap compressBitmapThumb(Context context, Uri uri,
			int width, int height) {
		Bitmap bm = compressFileToBitmapThumb(uri.getPath(), width, height);
		if (bm != null) {
			return bm;
		}
		return null;
	}

//	public static Bitmap compressAndRotateToBitmapThumb(Context context, Uri uri) {
//		int width = WIFI_WIDTH;
//		int height = WIFI_HEIGHT;
		// if (!AndTools.isWifi(context)) {
		// width = MOBILE_WIDTH;
		// height = MOBILE_HEIGHT;
		// }
//		return compressAndRotateToBitmapThumb(context, uri, width, height);
//	}

	public static Bitmap compressBitmapToTargetSize(Context context, Uri uri,
			int targetSize) {
		int degree = getOrientation(context, uri.getPath(), null);
		Bitmap bm = getBitmap(context, uri, targetSize);
		if (bm != null) {
			return rotateBitmap(bm, degree);
		}
		return null;
	}

//	public static Bitmap compressAndRotateToBitmapThumb(Context context,
//			Uri uri, int width, int height) {
//		int degree = getOrientation(context, uri.getPath(), null);
//		Bitmap bm = getBitmap(context, uri, width, height);
//		if (bm != null) {
//			return rotateBitmap(bm, degree);
//		}
//		return null;
//	}

	public static void writeBitmap(Context context, String path, String name,
			Bitmap bitmap) {
		int compress = ThumbnailUtils.getBitmapCompress(context, bitmap);
		ImageUtils.writeBitmap(path, name, bitmap, compress);
	}

	public static File compressAndRotateToBitmapThumbFile(Context context,
			Uri uri) {
		int width = WIFI_WIDTH;
		int height = WIFI_HEIGHT;
		// if (!AndTools.isWifi(context)) {
		// width = MOBILE_WIDTH;
		// height = MOBILE_HEIGHT;
		// }
		return compressAndRotateToBitmapThumbFile(context, uri, width, height);
	}

	public static String writeToFile(Context context, Uri uri){
		File file = FileUtils.newImageCacheFile(context);
		FileOutputStream os = null;
		InputStream is = null;
		try {
			os = new FileOutputStream(file);
			is = context.getContentResolver().openInputStream(uri);
			byte[] b = new byte[1024];
			int count = 0;
			while (true) {
				int readLength = is.read(b);
				if (readLength == -1) {
					break;
				}
				os.write(b, 0, readLength);
				count++;
				if (count % 5 == 0) {
					os.flush();
				}
			}
			
		} catch (Exception e) {
		} catch (Throwable t) {
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return file.getAbsolutePath();
	}
	
	public static Bitmap getBitmap(Context context, Uri uri, int targetSize) {
		Bitmap bm = null;
		String path = "";
		if (!"file".equals(uri.getScheme())) {			
			path=writeToFile(context,uri);
			bm = compressFileToBitmapThumb(path, targetSize);
		} else {
			path = uri.getPath();
			bm = compressFileToBitmapThumb(path, targetSize);
		}
		File delFile = new File(path);
		if (delFile.exists()) {
			delFile.delete();
		}
		return bm;
	}

	public static File compressAndRotateToBitmapThumbFile(Context context,
			Uri uri, int width, int height) {
		if (uri == null) {
			return null;
		}
		String path = uri.getPath();
		if (!"file".equals(uri.getScheme())) {			
			path=writeToFile(context,uri);
		}
		return compressAndRotateToBitmapThumbFile(context,path,width,height);
	}
	
	public static File compressAndRotateToBitmapThumbFile(Context context,
			String path, int width, int height) {
		int degree = getOrientation(context, path, null);
		Bitmap bm = compressFileToBitmapThumb(path, width, height);
		File delFile = new File(path);
		if (delFile.exists()) {
			delFile.delete();
		}
		if (bm == null)
			return null;
		Bitmap rotate = rotateBitmap(bm, degree);

		if (rotate != null) {
			String nameString = UUID.randomUUID().toString() + ".jpg";
			String pathString = FileUtils.getImageCachePath(context);
			int size = getBitmapCompress(context, bm);
			if (ImageUtils.writeBitmap(pathString, nameString, rotate, size)) {
				rotate.recycle();
				rotate = null;
				return new File(pathString, nameString);
			}
			rotate.recycle();
			rotate = null;
		}
		return null;
	}
	
	public static File compressAndRotateToBitmapThumbFile(Context context,
			String originPath, String targetPath, int width, int height) {
		int degree = getOrientation(context, originPath, null);
		Bitmap bm = compressFileToBitmapThumb(originPath, width, height);
		if (bm == null)
			return null;
		Bitmap rotate = rotateBitmap(bm, degree);

		if (rotate != null) {
			int size = getBitmapCompress(context, bm);
			if (ImageUtils.writeBitmap(targetPath, rotate, size)) {
				rotate.recycle();
				rotate = null;
				return new File(targetPath);
			}
			rotate.recycle();
			rotate = null;
		}
		return null;
	}
	
	public static Bitmap compressFileToBitmapThumb(String filePath, int targetSize) {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		FileInputStream fis = null;
		FileDescriptor fd = null;
		try {
			fis = new FileInputStream(file);
			fd = fis.getFD();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		Options options = new Options();
		options.inSampleSize = 1;
		options.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeFileDescriptor(fd, null, options);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				fis.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		if (options.mCancel || options.outWidth == -1
				|| options.outHeight == -1) {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		int currentSize = Math.min(options.outWidth, options.outHeight);
		float ratio = (float) targetSize / (float) currentSize;

		int targetWidth = (int) (options.outWidth * ratio);
		int targetHeight = (int) (options.outHeight * ratio);

//		options.outWidth = targetWidth;
//		options.outHeight = targetHeight;
		options.inJustDecodeBounds = false;

		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		int maxPixels = targetWidth * targetHeight;
		if (isLargeImage(options)) {
			if (options.outWidth != 0) {
				maxPixels = options.outHeight * options.outWidth;
				if (options.outWidth >= 800) {
					maxPixels = maxPixels / (options.outWidth / 400);
				}
			} else {
				maxPixels = maxPixels * 4;
			}
		}
		int sampleSize = computeSampleSize(options, targetSize, maxPixels);
		int maxSample = Math.max(sampleSize, 40);

		for (int index = sampleSize; index <= maxSample; index = index * 2) {
			try {
				options.inSampleSize = index;
				Bitmap bm = BitmapFactory.decodeFileDescriptor(fd, null,
						options);
				if (null != bm) {
					Bitmap newBm=Bitmap.createScaledBitmap(bm, targetWidth, targetHeight, true);
					return newBm;
				}
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		try {
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap compressFileToBitmapThumb(String filePath, int width,
			int height) {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		FileInputStream fis = null;
		FileDescriptor fd = null;
		try {
			fis = new FileInputStream(file);
			fd = fis.getFD();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		Options options = new Options();
		options.inSampleSize = 1;
		options.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeFileDescriptor(fd, null, options);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				fis.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		} catch (Throwable t) {
			t.printStackTrace();
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		if (options.mCancel || options.outWidth == -1
				|| options.outHeight == -1) {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		int targetSize = Math.min(width, height);
		int maxPixels = width * height;
		if (isLargeImage(options)) {
			if (options.outWidth != 0) {
				maxPixels = options.outHeight * options.outWidth;
				if (options.outWidth >= 800) {
					maxPixels = maxPixels / (options.outWidth / 400);
				}
			} else {
				maxPixels = maxPixels * 4;
			}
		}
		int sampleSize = computeSampleSize(options, targetSize, maxPixels);
		int maxSample = Math.max(sampleSize, 40);
		options.inJustDecodeBounds = false;

		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		for (int index = sampleSize; index <= maxSample; index = index * 2) {
			try {
				options.inSampleSize = index;
				Bitmap bm = BitmapFactory.decodeFileDescriptor(fd, null,
						options);
				if (null != bm) {
					return bm;
				}
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		try {
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取照片的角度 两种方式:1.根据绝对路径或根据Uri
	 *
	 * @param imagePath
	 *            照片的路径
	 * @param context
	 * @param photoUri
	 *
	 * */
	public static int getOrientation(Context context, String imagePath,
			Uri photoUri) {
		int nOrientation = 0;
		if (!TextUtils.isEmpty(imagePath)) {
			try {
				ExifInterface exif = new ExifInterface(imagePath);
				nOrientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION, 0);
				switch (nOrientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					return 90;
				case ExifInterface.ORIENTATION_ROTATE_270:
					return 270;
				case ExifInterface.ORIENTATION_ROTATE_180:
					return 180;
				case ExifInterface.ORIENTATION_NORMAL:
					return 0;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (context != null && photoUri != null) {
			Cursor cursor = null;
			try {
				cursor = context
						.getContentResolver()
						.query(photoUri,
								new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
								null, null, null);

				if (cursor == null || cursor.getCount() != 1) {
					return 0;
				}

				cursor.moveToFirst();
				int ret = cursor.getInt(0);
				return ret;
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
					cursor = null;
				}
			}
		}
		return 0;
	}

	/**
	 * 获取bitmap大小，转化为实际占用空间物理大小
	 *
	 * @param bm
	 * @return
	 */
	public static int getBitmapCompress(Context context, Bitmap bm) {

		int initCompress = 100;

		int width = bm.getWidth();
		int height = bm.getHeight();

		int size = width * height / 1024 / 8;

		int totalSize = WIFI_BITMAP_LENGTH;

		if (bm.getHeight() / bm.getWidth() >= 3) {
			totalSize = 160;

		}
		if (size > 300) {
			initCompress = 10;
		} else if (size > 260) {
			initCompress = 20;
		} else if (size > 220) {
			initCompress = 30;
		} else if (size > 180) {
			initCompress = 40;
		} else if (size > 140) {
			initCompress = 50;
		} else if (size >= 110) {
			initCompress = 60;
		} else if (size >= 100) {
			initCompress = 65;
		} else if (size >= 90) {
			initCompress = 70;
		} else if (size >= 80) {
			initCompress = 75;
		} else if (size >= 70) {
			initCompress = 80;
		} else if (size >= 60) {
			initCompress = 85;
		} else if (size >= 50) {
			initCompress = 90;
		}

		// if(!AndTools.isWifi(context)){
		// totalSize = MOBIEL_BITMAP_LENGTH;
		// }

		int compress = initCompress;
		try {
			for (int i = compress; i >= 10; i = i - 10) {
				compress = i;
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bm.compress(CompressFormat.JPEG, i, stream);
				byte[] imageInByte = stream.toByteArray();
				if (imageInByte.length / 1024 <= totalSize) {
					break;
				}
			}
		} catch (Exception e) {
			compress = 10;
		} catch (Throwable t) {
			compress = 10;
		}
		return compress;
	}

	public static int computeSampleSize(Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math
				.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math
				.min(Math.floor(w / minSideLength),
						Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == UNCONSTRAINED)
				&& (minSideLength == UNCONSTRAINED)) {
			return 1;
		} else if (minSideLength == UNCONSTRAINED) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static Bitmap rotateBitmap(Bitmap b, int degrees) {
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, b.getWidth() / 2, b.getHeight() / 2);
			try {
				Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
						b.getHeight(), m, true);
				// b.recycle();
				// b = null;
				return b2;
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
		return b;
	}

	public static Bitmap rotateSquerBitmap(Bitmap b, int degrees) {

			Matrix m = new Matrix();
			int width = b.getWidth();
			int height = b.getHeight();
			int x = 0;
			int y = 0;

			if (width > height) {
				x = Math.abs(width-height)/2;
				width = width-x*2;
			} else {
				y = Math.abs(width-height)/2;
				height = height-y*2;
			}
			if (degrees != 0) {
				m.setRotate(degrees, b.getWidth() / 2, b.getHeight() / 2);
			}
			try {
				Bitmap b2 = Bitmap.createBitmap(b, x, y, width,
						height, m, true);
				// b.recycle();
				// b = null;
				return b2;
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Throwable ex) {
				ex.printStackTrace();
			}

		return b;
	}

	public static byte[] compressImageBySize(Bitmap bitmap, int size) {
		return compressImageBySize(bitmap, size, 800f, 480f);
	}

	/**
	 * 对图片尺寸进行压缩
	 *
	 * @param bitmap
	 * @param size
	 * @return
	 */
	public static byte[] compressImageBySize(Bitmap bitmap, int size,
			float height, float width) {
		if (bitmap == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			bitmap.compress(CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Options newOpts = new Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bt = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = height;// 这里设置高度为800f
		float ww = width;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bt = BitmapFactory.decodeStream(isBm, null, newOpts);

		return compressImage(bt, size);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 对图片质量压缩
	 *
	 * @param image
	 *            图片的 bitmap
	 * @param size
	 *            想要得到的大小尺寸 单位是kb 如果压缩不到，则返回null
	 * @return
	 */
	public static byte[] compressImage(Bitmap image, int size) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int quality = 100;
		image.compress(CompressFormat.JPEG, quality, baos);
		while (baos.toByteArray().length / 1024 > size && quality > 10) {
			quality -= 10;
			baos.reset();
			image.compress(CompressFormat.JPEG, quality, baos);
		}
		if (baos.toByteArray().length / 1024 > size) {
			return null;
		}
		return baos.toByteArray();
	}

	/**
	 * 对图片先进行尺寸压缩，再进行质量压缩，最后返回 bitmap 如果未压缩到设置的最大值，则返回 Null
	 *
	 * @param bitmap
	 * @param maxKB
	 * @return
	 */
	public static Bitmap compressAsBitmap(Bitmap bitmap, int maxKB,
			float height, float width) {
		byte[] data = compressImageBySize(bitmap, maxKB, height, width);
		if (data != null) {
			Options ops = new Options();
			ops.inPreferredConfig = Bitmap.Config.RGB_565;
			return BitmapFactory.decodeByteArray(data, 0, data.length, ops);
		}
		return null;
	}

	public static Bitmap compressAsBitmap(Bitmap bitmap, int maxKB) {
		return compressAsBitmap(bitmap, maxKB, 800f, 480f);
	}
	

	public static boolean isLargeImage(Options options) {

		return (options.outHeight > 0 && options.outWidth > 0) ? (options.outHeight
				/ options.outWidth >= 3)
				: false;
	}
}

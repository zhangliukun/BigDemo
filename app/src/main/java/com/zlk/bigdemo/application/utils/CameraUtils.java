package com.zlk.bigdemo.application.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;

public class CameraUtils {
	
	/**
	 * 请求相机.
	 */
	public static final int REQUEST_IMAGE_CAPTURE = 10000;

	/**
	 * 请求相册.
	 */
	public static final int REQUEST_PHOTO_LIBRARY = 10001;
	
	/**
	 * 请求相册.
	 */
	public static final int REQUEST_PHOTO_KITKAT = 10002;
	
	/**
	 * 请求图片剪裁
	 */
	public static final int REQUEST_CODE_IMAGE_CROP = 10003;
	
	public static void openPhotoLibrary(Activity activity) {
		openPhotoLibrary(activity,REQUEST_PHOTO_LIBRARY);
	}

    public static void openPhotoLibrary(Activity activity,int requestCode) {
        try {

            Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
//			if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){
//				activity.startActivityForResult(intent, REQUEST_PHOTO_KITKAT);
//			}else{
            activity.startActivityForResult(intent, requestCode);
//			}

        } catch (Throwable e) {
        }
    }

	public static Uri openCapture(Activity activity) {
		return openCapture(activity,REQUEST_IMAGE_CAPTURE);
	}

    public static Uri openCapture(Activity activity,int requestCode) {
        try {
            File mImageFile = FileUtils.newImageCacheFile(activity);
            Uri mImageUri = Uri.fromFile(mImageFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            activity.startActivityForResult(intent, requestCode);
            return mImageUri;
        } catch (Exception e) {
        }
        return null;
    }
	
	public static Uri startCropImageActivityForPicture(Activity context, Uri uri){
		File mImageFile = FileUtils.newImageCacheFile(context);
		Uri outputUri = Uri.fromFile(mImageFile);
		startCropImageActivityForResult(context, uri, outputUri, 400,400,REQUEST_CODE_IMAGE_CROP);
		return outputUri;
	}

	public static void startCropImageActivityForCamera(Activity context, Uri uri, Uri outputUri){
		startCropImageActivityForResult(context, uri, outputUri, 400,400,REQUEST_CODE_IMAGE_CROP);
	}


	public static void startCropImageActivityForResult(Activity context, Uri uri,Uri outUri, int outputX, int outputY, int requestCode){
		
	    Intent intent = new Intent("com.aidingmao.android.camera.action.CROP");
	    intent.setDataAndType(uri, "image/*");
	    intent.putExtra("crop", "true");
	    intent.putExtra("aspectX", 1);
	    intent.putExtra("aspectY", 1);
	    intent.putExtra("outputX", outputX);
	    intent.putExtra("outputY", outputY);
	    intent.putExtra("scale", true);
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri==null?uri:outUri);
	    intent.putExtra("return-data", false);
	    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
	    intent.putExtra("noFaceDetection", true); // no face detection
	    context.startActivityForResult(intent, requestCode);
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri) {  
		  
	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  
	  
	    // DocumentProvider  
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
	        // ExternalStorageProvider  
	        if (isExternalStorageDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);  
	            final String[] split = docId.split(":");  
	            final String type = split[0];  
	  
	            if ("primary".equalsIgnoreCase(type)) {  
	                return Environment.getExternalStorageDirectory() + "/" + split[1];  
	            }  
	  
	            // TODO handle non-primary volumes  
	        }  
	        // DownloadsProvider  
	        else if (isDownloadsDocument(uri)) {  
	  
	            final String id = DocumentsContract.getDocumentId(uri);  
	            final Uri contentUri = ContentUris.withAppendedId(  
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  
	  
	            return getDataColumn(context, contentUri, null, null);  
	        }  
	        // MediaProvider  
	        else if (isMediaDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);  
	            final String[] split = docId.split(":");  
	            final String type = split[0];  
	  
	            Uri contentUri = null;  
	            if ("image".equals(type)) {  
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("video".equals(type)) {  
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("audio".equals(type)) {  
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
	            }  
	  
	            final String selection = "_id=?";  
	            final String[] selectionArgs = new String[] {  
	                    split[1]  
	            };  
	  
	            return getDataColumn(context, contentUri, selection, selectionArgs);  
	        }  
	    }  
	    // MediaStore (and general)  
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {  
	  
	        // Return the remote address  
	        if (isGooglePhotosUri(uri))  
	            return uri.getLastPathSegment();  
	  
	        return getDataColumn(context, uri, null, null);  
	    }  
	    // File  
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {  
	        return uri.getPath();  
	    }  
	  
	    return null;  
	}  
	  
	/** 
	 * Get the value of the data row for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders. 
	 * 
	 * @param context The context. 
	 * @param uri The Uri to query. 
	 * @param selection (Optional) Filter used in the query. 
	 * @param selectionArgs (Optional) Selection arguments used in the query. 
	 * @return The value of the _data row, which is typically a file path.
	 */  
	public static String getDataColumn(Context context, Uri uri, String selection,  
	        String[] selectionArgs) {  
	  
	    Cursor cursor = null;  
	    final String column = "_data";  
	    final String[] projection = {  
	            column  
	    };  
	  
	    try {  
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
	                null);  
	        if (cursor != null && cursor.moveToFirst()) {  
	            final int index = cursor.getColumnIndexOrThrow(column);  
	            return cursor.getString(index);  
	        }  
	    } finally {  
	        if (cursor != null)  
	            cursor.close();  
	    }  
	    return null;  
	}  
	  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is ExternalStorageProvider. 
	 */  
	public static boolean isExternalStorageDocument(Uri uri) {  
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is DownloadsProvider. 
	 */  
	public static boolean isDownloadsDocument(Uri uri) {  
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is MediaProvider. 
	 */  
	public static boolean isMediaDocument(Uri uri) {  
	    return "com.android.providers.media.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is Google Photos. 
	 */  
	public static boolean isGooglePhotosUri(Uri uri) {  
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
	}  
}

/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.zlk.bigdemo.android.camera;

import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.android.camera.gallery.IImage;
import com.zlk.bigdemo.android.camera.gallery.IImageList;
import com.zlk.bigdemo.application.utils.LogUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

/**
 * The activity can crop specific region of interest from an image.
 * 图片剪裁的处理：2.3的代码（已经过修改与处理）
 */
public class CropImage extends MonitoredActivity {
    private static final String TAG = "CropImage";

    public static final String KEY_RETURN_DATA_FILE_NAME = "return-data-file-name";
    public static final String KEY_RETURN_DATA = "return-data";
    public static final String KEY_ASPECT_X = "aspectX";
    public static final String KEY_ASPECT_Y = "aspectY";
    public static final String KEY_OUTPUT_X = "outputX";
    public static final String KEY_OUTPUT_Y = "outputY";
    public static final String KEY_SCALE = "scale";
    public static final String KEY_DATA = "data";
    public static final String KEY_SCALE_UP_IF_NEEDED = "scaleUpIfNeeded";
    public static final String KEY_OUTPUT_FORMAT = "outputFormat";
    public static final String KEY_SET_AS_WALLPAPER = "set-as-wallpaper";
    public static final String KEY_NO_FACE_DETECTION = "noFaceDetection";
    public static final String KEY_DEFAULT_CROP_RECTANGLE_SIZE = "defaultCropRectangleSize";
    public static final String VALUE_CROP_RECTANGLE_SIZE_DETAULT = "same-with-same-image";
    

    // These are various options can be specified in the intent.
    private Bitmap.CompressFormat mOutputFormat =
            Bitmap.CompressFormat.JPEG; // only used with mSaveUri
    private Uri mSaveUri = null;
    private boolean mSetWallpaper = false;
    private int mAspectX, mAspectY;
    private boolean mDoFaceDetection = true;
    private final Handler mHandler = new Handler();

    // These options specifiy the output image size and whether we should
    // scale the output to fit it (or just crop it).
    private int mOutputX, mOutputY;
    private boolean mScale;
    private boolean mScaleUp = true;

    boolean mWaitingToPick; // Whether we are wait the user to pick a face.
    boolean mSaving;  // Whether the "save" button is already clicked.
    private String defaultRectangleSize = null;

    private CropImageView mCropImageView;
    private ContentResolver mContentResolver;

    private Bitmap mBitmap;
    HighlightRectangle mCropRectangle;

    private IImageList mAllImages;
    private IImage mImage;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mContentResolver = getContentResolver();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cropimage);
        mCropImageView = (CropImageView) findViewById(R.id.image);
        
        final Intent intent = this.getIntent();
        initCropParameters(intent);
        if (mBitmap == null) {
            finish();
            return;
        }

        // Make UI fullscreen.
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findViewById(R.id.discard).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });

        findViewById(R.id.save).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        onSaveClicked();
                    }
                });

        startFaceDetection();
    }

	private void initCropParameters(final Intent intent) {
        Bundle extras = intent.getExtras();

        if (extras != null) {
            mSaveUri = (Uri) extras.getParcelable(MediaStore.EXTRA_OUTPUT);
            if (mSaveUri != null) {
                String outputFormatString = extras.getString(KEY_OUTPUT_FORMAT);
                if (outputFormatString != null) {
                    mOutputFormat = Bitmap.CompressFormat.valueOf(
                            outputFormatString);
                }
            } else {
                mSetWallpaper = extras.getBoolean(KEY_SET_AS_WALLPAPER);
            }
            mBitmap = (Bitmap) extras.getParcelable(KEY_DATA);
            mAspectX = extras.getInt(KEY_ASPECT_X, 0);
            mAspectY = extras.getInt(KEY_ASPECT_Y, 0);
            mOutputX = extras.getInt(KEY_OUTPUT_X);
            mOutputY = extras.getInt(KEY_OUTPUT_Y);
            mScale = extras.getBoolean(KEY_SCALE, true);
            mScaleUp = extras.getBoolean(KEY_SCALE_UP_IF_NEEDED, true);
            defaultRectangleSize = extras.getString(KEY_DEFAULT_CROP_RECTANGLE_SIZE);
            mDoFaceDetection = extras.containsKey(KEY_NO_FACE_DETECTION)
                    ? !extras.getBoolean(KEY_NO_FACE_DETECTION)
                    : true;
        }
        if (mBitmap == null) {
            Uri target = intent.getData();
            mAllImages = ImageManager.makeImageList(mContentResolver, target,
                    ImageManager.SORT_ASCENDING);
            mImage = mAllImages.getImageForUri(target);
            if (mImage != null) {
                // Don't read in really large bitmaps. Use the (big) thumbnail
                // instead.
                // TODO when saving the resulting bitmap use the
                // decode/crop/encode api so we don't lose any resolution.
                mBitmap = mImage.thumbBitmap(IImage.ROTATE_AS_NEEDED);
                if (mBitmap == null) {
                    mBitmap = mImage.fullSizeBitmap(IImage.UNCONSTRAINED, mOutputX * mOutputY,
                            IImage.ROTATE_AS_NEEDED, false);
                }

            }
        }
	}

    private void startFaceDetection() {
        if (isFinishing()) {
            return;
        }

        mCropImageView.setImageBitmapResetBase(mBitmap, true);

        Util.startBackgroundJob(this, null,
                getResources().getString(R.string.running_face_detection),
                new Runnable() {
            public void run() {
                final CountDownLatch latch = new CountDownLatch(1);
                final Bitmap b = (mImage != null)
                        ? mImage.fullSizeBitmap(IImage.UNCONSTRAINED,
                        1024 * 1024)
                        : mBitmap;
                mHandler.post(new Runnable() {
                    public void run() {
                        if (b != mBitmap && b != null) {
                            mCropImageView.setImageBitmapResetBase(b, true);
                            mBitmap.recycle();
                            mBitmap = b;
                        }
                        if (mCropImageView.getScale() == 1F) {
                            mCropImageView.center(true, true);
                        }
                        latch.countDown();
                    }
                });
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                mRunFaceDetection.run();
            }
        }, mHandler);
    }

    private void onSaveClicked() {
        // TODO this code needs to change to use the decode/crop/encode single
        // step api so that we don't require that the whole (possibly large)
        // bitmap doesn't have to be read into memory
        if (mCropRectangle == null) {
            return;
        }

        if (mSaving) return;
        mSaving = true;

        Bitmap croppedImage;

        // If the output is required to a specific size, create an new image
        // with the cropped image in the center and the extra space filled.
        if (mOutputX != 0 && mOutputY != 0 && !mScale) {
            // Don't scale the image but instead fill it so it's the
            // required dimension
            croppedImage = Bitmap.createBitmap(mOutputX, mOutputY,
                    Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(croppedImage);

            Rect srcRect = mCropRectangle.getCropRect();
            Rect dstRect = new Rect(0, 0, mOutputX, mOutputY);

            int dx = (srcRect.width() - dstRect.width()) / 2;
            int dy = (srcRect.height() - dstRect.height()) / 2;

            // If the srcRect is too big, use the center part of it.
            srcRect.inset(Math.max(0, dx), Math.max(0, dy));

            // If the dstRect is too big, use the center part of it.
            dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

            // Draw the cropped bitmap in the center
            canvas.drawBitmap(mBitmap, srcRect, dstRect, null);

            // Release bitmap memory as soon as possible
            mCropImageView.clear();
            mBitmap.recycle();
        } else {
            Rect r = mCropRectangle.getCropRect();

            int width = r.width();
            int height = r.height();

            // If we are circle cropping, we want alpha channel, which is the
            // third param here.
            croppedImage = Bitmap.createBitmap(width, height,Bitmap.Config.RGB_565);

            Canvas canvas = new Canvas(croppedImage);
            Rect dstRect = new Rect(0, 0, width, height);
            canvas.drawBitmap(mBitmap, r, dstRect, null);

            // Release bitmap memory as soon as possible
            mCropImageView.clear();
            mBitmap.recycle();

            // If the required dimension is specified, scale the image.
            if (mOutputX != 0 && mOutputY != 0 && mScale) {
                croppedImage = Util.transform(new Matrix(), croppedImage,
                        mOutputX, mOutputY, mScaleUp, Util.RECYCLE_INPUT);
            }
        }

        mCropImageView.setImageBitmapResetBase(croppedImage, true);
        mCropImageView.center(true, true);
        mCropImageView.mHighlightViews.clear();

        // Return the cropped image directly or save it to the specified URI.
        Bundle myExtras = getIntent().getExtras();
        if (myExtras != null && (myExtras.getParcelable("data") != null
                || myExtras.getBoolean(KEY_RETURN_DATA,false))) {
            Bundle extras = new Bundle();
            String fileName = getIntent().getStringExtra(KEY_RETURN_DATA_FILE_NAME);
            if(fileName != null && fileName.trim().length() != 0){
            	BufferedOutputStream bos = null;
        		try {
        			bos = new BufferedOutputStream(this.openFileOutput(fileName, Context.MODE_PRIVATE));
        			croppedImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        			extras.putString("file-data", fileName);
        		} catch (IOException ioe) {
        			LogUtil.e(TAG, ioe.getMessage());
        		} finally {
        			try {
        				if (bos != null) {
        					bos.flush();
        					bos.close();
        				}
        			} catch (IOException e) {
        				LogUtil.e(TAG, "Could not close file.");
        			}
        		}
            }else{
            	extras.putParcelable("data", croppedImage);
            }
            setResult(RESULT_OK,
                    (new Intent()).setAction("inline-data").putExtras(extras));
            finish();
        } else {
            final Bitmap b = croppedImage;
            final int msdId = mSetWallpaper
                    ? R.string.wallpaper
                    : R.string.saving_image;
            Util.startBackgroundJob(this, null,
                    getResources().getString(msdId),
                    new Runnable() {
                public void run() {
                    saveOutput(b);
                }
            }, mHandler);
        }
    }

    private void saveOutput(Bitmap croppedImage) {
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = mContentResolver.openOutputStream(mSaveUri);
                if (outputStream != null) {
                    croppedImage.compress(mOutputFormat, 75, outputStream);
                }
            } catch (IOException ex) {
                LogUtil.e(TAG, "Cannot open file: " + mSaveUri, ex);
            } finally {
                Util.closeSilently(outputStream);
            }
            Bundle extras = new Bundle();
            setResult(RESULT_OK, new Intent(mSaveUri.toString())
                    .putExtras(extras));
        } else if (mSetWallpaper) {
            try {
                WallpaperManager.getInstance(this).setBitmap(croppedImage);
                setResult(RESULT_OK);
            } catch (IOException e) {
                LogUtil.e(TAG, "Failed to set wallpaper.", e);
                setResult(RESULT_CANCELED);
            }
        } else {
            Bundle extras = new Bundle();
            extras.putString("rect", mCropRectangle.getCropRect().toString());

            File oldPath = new File(mImage.getDataPath());
            File directory = new File(oldPath.getParent());

            int x = 0;
            String fileName = oldPath.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf("."));

            // Try file-1.jpg, file-2.jpg, ... until we find a filename which
            // does not exist yet.
            while (true) {
                x += 1;
                String candidate = directory.toString()
                        + "/" + fileName + "-" + x + ".jpg";
                boolean exists = (new File(candidate)).exists();
                if (!exists) {
                    break;
                }
            }

            try {
                int[] degree = new int[1];
                Uri newUri = ImageManager.addImage(
                        mContentResolver,
                        mImage.getTitle(),
                        mImage.getDateTaken(),
                        null,    // TODO this null is going to cause us to lose
                                 // the location (gps).
                        directory.toString(), fileName + "-" + x + ".jpg",
                        croppedImage, null,
                        degree);

                setResult(RESULT_OK, new Intent()
                        .setAction(newUri.toString())
                        .putExtras(extras));
            } catch (Exception ex) {
                // basically ignore this or put up
                // some ui saying we failed
                LogUtil.e(TAG, "store image fail, continue anyway", ex);
            }
        }

        final Bitmap b = croppedImage;
        mHandler.post(new Runnable() {
            public void run() {
                mCropImageView.clear();
                b.recycle();
            }
        });

        finish();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mAllImages != null) {
            mAllImages.close();
        }
        super.onDestroy();
    }

    Runnable mRunFaceDetection = new Runnable() {
        float mScale = 1F;
        Matrix mImageMatrix;
        FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
        int mNumFaces;

        // For each face, we create a HightlightView for it.
        private void handleFace(FaceDetector.Face f) {
            PointF midPoint = new PointF();

            int r = ((int) (f.eyesDistance() * mScale)) * 2;
            f.getMidPoint(midPoint);
            midPoint.x *= mScale;
            midPoint.y *= mScale;

            int midX = (int) midPoint.x;
            int midY = (int) midPoint.y;

            HighlightRectangle hv = new HighlightRectangle(mCropImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            RectF faceRect = new RectF(midX, midY, midX, midY);
            faceRect.inset(-r, -r);
            if (faceRect.left < 0) {
                faceRect.inset(-faceRect.left, -faceRect.left);
            }

            if (faceRect.top < 0) {
                faceRect.inset(-faceRect.top, -faceRect.top);
            }

            if (faceRect.right > imageRect.right) {
                faceRect.inset(faceRect.right - imageRect.right,
                               faceRect.right - imageRect.right);
            }

            if (faceRect.bottom > imageRect.bottom) {
                faceRect.inset(faceRect.bottom - imageRect.bottom,
                               faceRect.bottom - imageRect.bottom);
            }

            hv.setup(mImageMatrix, imageRect, faceRect, mAspectX != 0 && mAspectY != 0);

            mCropImageView.add(hv);
        }

        // Create a default HightlightView if we found no face in the picture.
        private void makeDefault() {
            HighlightRectangle hv = new HighlightRectangle(mCropImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            int cropHeight = cropWidth;

            if (mAspectX != 0 && mAspectY != 0) {
                if (mAspectX > mAspectY) {
                    cropHeight = cropWidth * mAspectY / mAspectX;
                } else {
                    cropWidth = cropHeight * mAspectX / mAspectY;
                }
            }else if(VALUE_CROP_RECTANGLE_SIZE_DETAULT.equals(defaultRectangleSize)){
            	cropWidth = width - 2;
            	cropHeight = height - 2;
            }

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(mImageMatrix, imageRect, cropRect, mAspectX != 0 && mAspectY != 0);
            mCropImageView.add(hv);
        }

        // Scale the image down for faster face detection.
        private Bitmap prepareBitmap() {
            if (mBitmap == null) {
                return null;
            }

            // 256 pixels wide is enough.
            if (mBitmap.getWidth() > 256) {
                mScale = 256.0F / mBitmap.getWidth();
            }
            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            Bitmap faceBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap
                    .getWidth(), mBitmap.getHeight(), matrix, true);
            return faceBitmap;
        }

        public void run() {
            mImageMatrix = mCropImageView.getImageMatrix();
            Bitmap faceBitmap = prepareBitmap();

            mScale = 1.0F / mScale;
            if (faceBitmap != null && mDoFaceDetection) {
                FaceDetector detector = new FaceDetector(faceBitmap.getWidth(),
                        faceBitmap.getHeight(), mFaces.length);
                mNumFaces = detector.findFaces(faceBitmap, mFaces);
            }

            if (faceBitmap != null && faceBitmap != mBitmap) {
                faceBitmap.recycle();
            }

            mHandler.post(new Runnable() {
                public void run() {
                    mWaitingToPick = mNumFaces > 1;
                    if (mNumFaces > 0) {
                        for (int i = 0; i < mNumFaces; i++) {
                            handleFace(mFaces[i]);
                        }
                    } else {
                        makeDefault();
                    }
                    mCropImageView.invalidate();
                    if (mCropImageView.mHighlightViews.size() == 1) {
                        mCropRectangle = mCropImageView.mHighlightViews.get(0);
                        mCropRectangle.setFocus(true);
                    }
                }
            });
        }
    };
}



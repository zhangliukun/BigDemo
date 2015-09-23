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

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.zlk.bigdemo.R;


// This class is used by CropImage to display a highlighted cropping rectangle
// overlayed with the image. There are two coordinate spaces in use. One is
// image, another is screen. computeLayout() uses mMatrix to map from image
// space to screen space.

/**
 * 图片剪裁的指示框：原生态的实现基础上改造
 */
class HighlightRectangle{

	@SuppressWarnings("unused")
	private static final String TAG = "HighlightView";
	private static final int COLOR_OUTLINE = 0xFF008AFF;
    
	enum ModifyMode {Normal, Move}
	
    private View mContext; // The View displaying the image.
	Rect mDrawRect; // in screen space
	private RectF mImageRect; // in image space
	RectF mCropRect; // in image space
	Matrix mMatrix;
	private boolean mMaintainAspectRatio = false;
	private float mInitialAspectRatio;
	private Drawable mResizeDrawable;
	private final Paint mFocusPaint = new Paint();
	private final Paint mNoFocusPaint = new Paint();
	private final Paint mOutlinePaint = new Paint();

	public static final int GROW_NONE = (1 << 0);
	public static final int GROW_LEFT_EDGE = (1 << 1);
	public static final int GROW_RIGHT_EDGE = (1 << 2);
	public static final int GROW_TOP_EDGE = (1 << 3);
	public static final int GROW_BOTTOM_EDGE = (1 << 4);
	public static final int MOVE = (1 << 5);
	private ModifyMode mMode = ModifyMode.Normal;

	public HighlightRectangle(View ctx) {
		mContext = ctx;
		mHidden = false;
	}

	private void init() {
		android.content.res.Resources resources = mContext.getResources();
		mResizeDrawable = resources.getDrawable(R.drawable.camera_crop_holo);
		mOutlinePaint.setColor(COLOR_OUTLINE);
	}

	boolean mIsFocused;
	boolean mHidden;

	public boolean hasFocus() {
		return mIsFocused;
	}

	public void setFocus(boolean f) {
		mIsFocused = f;
	}

	public void setHidden(boolean hidden) {
		mHidden = hidden;
	}

	protected void draw(Canvas canvas) {
		if (mHidden) {
			return;
		}
		canvas.save();
		Path path = new Path();
		if (!hasFocus()) {
			canvas.drawRect(mDrawRect, mOutlinePaint);
		} else {
			Rect viewDrawingRect = new Rect();
			mContext.getDrawingRect(viewDrawingRect);
			path.addRect(new RectF(mDrawRect), Path.Direction.CW);
			try{
				canvas.clipPath(path, Region.Op.DIFFERENCE);
			} catch(Throwable tr){
				tr.printStackTrace();
			}
			canvas.drawRect(viewDrawingRect, hasFocus() ? mFocusPaint
					: mNoFocusPaint);
			canvas.restore();
			canvas.drawPath(path, mOutlinePaint);

			if(mMode != ModifyMode.Move){
				addCropHint(canvas, mDrawRect);
			}
		}
	}
	
	/**
	 * 添加剪裁的指示说明
	 * @param canvas
	 * @param drawRect
	 */
	private void addCropHint(Canvas canvas, Rect drawRect){
    	int left = drawRect.left + 1;
		int right = drawRect.right + 1;
		int top = drawRect.top + 4;
		int bottom = drawRect.bottom + 3;

		int widthWidth = mResizeDrawable.getIntrinsicWidth() / 2;
		int widthHeight = mResizeDrawable.getIntrinsicHeight() / 2;
		int heightHeight = mResizeDrawable.getIntrinsicHeight() / 2;
		int heightWidth = mResizeDrawable.getIntrinsicWidth() / 2;

		int xMiddle = drawRect.left
				+ ((drawRect.right - drawRect.left) / 2);
		int yMiddle = drawRect.top
				+ ((drawRect.bottom - drawRect.top) / 2);

		mResizeDrawable
				.setBounds(left - widthWidth, yMiddle - widthHeight,
						left + widthWidth, yMiddle + widthHeight);
		mResizeDrawable.draw(canvas);

		mResizeDrawable.setBounds(right - widthWidth, yMiddle
				- widthHeight, right + widthWidth, yMiddle
				+ widthHeight);
		mResizeDrawable.draw(canvas);

		mResizeDrawable.setBounds(xMiddle - heightWidth, top
				- heightHeight, xMiddle + heightWidth, top
				+ heightHeight);
		mResizeDrawable.draw(canvas);

		mResizeDrawable.setBounds(xMiddle - heightWidth, bottom
				- heightHeight, xMiddle + heightWidth, bottom
				+ heightHeight);
		mResizeDrawable.draw(canvas);
    }
	
	public void setMode(ModifyMode mode) {
        if (mode != mMode) {
            mMode = mode;
            mContext.invalidate();
        }
    }

	// Determines which edges are hit by touching at (x, y).
	public int getHit(float x, float y) {
		Rect r = computeLayout();
		final float hysteresis = 20F;
		int retval = GROW_NONE;

		// verticalCheck makes sure the position is between the top and
		// the bottom edge (with some tolerance). Similar for horizCheck.
		boolean verticalCheck = (y >= r.top - hysteresis)
				&& (y < r.bottom + hysteresis);
		boolean horizCheck = (x >= r.left - hysteresis)
				&& (x < r.right + hysteresis);

		// Check whether the position is near some edge(s).
		if ((Math.abs(r.left - x) < hysteresis) && verticalCheck) {
			retval |= GROW_LEFT_EDGE;
		}
		if ((Math.abs(r.right - x) < hysteresis) && verticalCheck) {
			retval |= GROW_RIGHT_EDGE;
		}
		if ((Math.abs(r.top - y) < hysteresis) && horizCheck) {
			retval |= GROW_TOP_EDGE;
		}
		if ((Math.abs(r.bottom - y) < hysteresis) && horizCheck) {
			retval |= GROW_BOTTOM_EDGE;
		}

		// Not near any edge but inside the rectangle: move.
		if (retval == GROW_NONE && r.contains((int) x, (int) y)) {
			retval = MOVE;
		}
		return retval;
	}

	// Handles motion (dx, dy) in screen space.
	// The "edge" parameter specifies which edges the user is dragging.
	public void handleMotion(int edge, float dx, float dy) {
		Rect r = computeLayout();
		if (edge == GROW_NONE) {
			return;
		} else if (edge == MOVE) {
			// Convert to image space before sending to moveBy().
			moveBy(dx * (mCropRect.width() / r.width()),
					dy * (mCropRect.height() / r.height()));
		} else {
			if (((GROW_LEFT_EDGE | GROW_RIGHT_EDGE) & edge) == 0) {
				dx = 0;
			}

			if (((GROW_TOP_EDGE | GROW_BOTTOM_EDGE) & edge) == 0) {
				dy = 0;
			}

			// Convert to image space before sending to growBy().
			float xDelta = dx * (mCropRect.width() / r.width());
			float yDelta = dy * (mCropRect.height() / r.height());
			growBy((((edge & GROW_LEFT_EDGE) != 0) ? -1 : 1) * xDelta,
					(((edge & GROW_TOP_EDGE) != 0) ? -1 : 1) * yDelta);
		}
	}

	// Grows the cropping rectange by (dx, dy) in image space.
	private void moveBy(float dx, float dy) {
		Rect invalRect = new Rect(mDrawRect);

		mCropRect.offset(dx, dy);

		// Put the cropping rectangle inside image rectangle.
		mCropRect.offset(Math.max(0, mImageRect.left - mCropRect.left),
				Math.max(0, mImageRect.top - mCropRect.top));

		mCropRect.offset(Math.min(0, mImageRect.right - mCropRect.right),
				Math.min(0, mImageRect.bottom - mCropRect.bottom));

		mDrawRect = computeLayout();
		invalRect.union(mDrawRect);
		invalRect.inset(-10, -10);
		mContext.invalidate(invalRect);
	}

	// Grows the cropping rectange by (dx, dy) in image space.
	private void growBy(float dx, float dy) {
		if (mMaintainAspectRatio) {
			if (dx != 0) {
				dy = dx / mInitialAspectRatio;
			} else if (dy != 0) {
				dx = dy * mInitialAspectRatio;
			}
		}

		// Don't let the cropping rectangle grow too fast.
		// Grow at most half of the difference between the image rectangle and
		// the cropping rectangle.
		RectF r = new RectF(mCropRect);
		if (dx > 0F && r.width() + 2 * dx > mImageRect.width()) {
			float adjustment = (mImageRect.width() - r.width()) / 2F;
			dx = adjustment;
			if (mMaintainAspectRatio) {
				dy = dx / mInitialAspectRatio;
			}
		}
		if (dy > 0F && r.height() + 2 * dy > mImageRect.height()) {
			float adjustment = (mImageRect.height() - r.height()) / 2F;
			dy = adjustment;
			if (mMaintainAspectRatio) {
				dx = dy * mInitialAspectRatio;
			}
		}

		r.inset(-dx, -dy);

		// Don't let the cropping rectangle shrink too fast.
		final float widthCap = 25F;
		if (r.width() < widthCap) {
			r.inset(-(widthCap - r.width()) / 2F, 0F);
		}
		float heightCap = mMaintainAspectRatio ? (widthCap / mInitialAspectRatio)
				: widthCap;
		if (r.height() < heightCap) {
			r.inset(0F, -(heightCap - r.height()) / 2F);
		}

		// Put the cropping rectangle inside the image rectangle.
		if (r.left < mImageRect.left) {
			r.offset(mImageRect.left - r.left, 0F);
		} else if (r.right > mImageRect.right) {
			r.offset(-(r.right - mImageRect.right), 0);
		}
		if (r.top < mImageRect.top) {
			r.offset(0F, mImageRect.top - r.top);
		} else if (r.bottom > mImageRect.bottom) {
			r.offset(0F, -(r.bottom - mImageRect.bottom));
		}

		mCropRect.set(r);
		mDrawRect = computeLayout();
		mContext.invalidate();
	}

	// Returns the cropping rectangle in image space.
	public Rect getCropRect() {
		return new Rect((int) mCropRect.left, (int) mCropRect.top,
				(int) mCropRect.right, (int) mCropRect.bottom);
	}

	// Maps the cropping rectangle from image space to screen space.
	private Rect computeLayout() {
		RectF r = new RectF(mCropRect.left, mCropRect.top, mCropRect.right,
				mCropRect.bottom);
		mMatrix.mapRect(r);
		return new Rect(Math.round(r.left), Math.round(r.top),
				Math.round(r.right), Math.round(r.bottom));
	}

	public void invalidate() {
		mDrawRect = computeLayout();
	}

	/**
	 * 
	 * @param m
	 * @param imageRect
	 * @param cropRect
	 * @param maintainAspectRatio  图片剪裁的过程中是否保持原有的比例不变  true 保持指定的比例不变 
	 */
	public void setup(Matrix m, Rect imageRect, RectF cropRect,
			boolean maintainAspectRatio) {
		mMatrix = new Matrix(m);

		mCropRect = cropRect;
		mImageRect = new RectF(imageRect);
		mMaintainAspectRatio = maintainAspectRatio;

		mInitialAspectRatio = mCropRect.width() / mCropRect.height();
		mDrawRect = computeLayout();

		mFocusPaint.setARGB(125, 50, 50, 50);
		mNoFocusPaint.setARGB(125, 50, 50, 50);
		mOutlinePaint.setStrokeWidth(3F);
		mOutlinePaint.setStyle(Paint.Style.STROKE);
		mOutlinePaint.setAntiAlias(true);
		mMode = ModifyMode.Normal;
		init();
	}
}

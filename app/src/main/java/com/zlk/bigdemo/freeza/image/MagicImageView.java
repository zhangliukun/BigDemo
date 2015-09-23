package com.zlk.bigdemo.freeza.image;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.zlk.bigdemo.android.volley.VolleyError;
import com.zlk.bigdemo.android.volley.toolbox.ImageLoader;
import com.zlk.bigdemo.android.volley.toolbox.ImageLoader.ImageContainer;
import com.zlk.bigdemo.android.volley.toolbox.ImageLoader.ImageListener;
import com.zlk.bigdemo.freeza.Freeza;

/**
 * Handles fetching an image from a URL as well as the life-cycle of the
 * associated request.
 */
public class MagicImageView extends ImageView {

	private int DEFAULT_ANIMATE_DURATION = 400;

	private int mAnimateDuration = DEFAULT_ANIMATE_DURATION;

	private boolean mShouldAlphaAnimate = false;

	public int mDefaultWidth = 0;

	/** The URL of the network image to load */
	private String mUrl;

	/**
	 * Resource ID of the image to be used as a placeholder until the network
	 * image is loaded.
	 */
	private Drawable mDefaultImage;

	/**
	 * Resource ID of the image to be used if the network response fails.
	 */
	private int mErrorImageId;
	
	/** Local copy of the ImageLoader. */
	private ImageLoader mImageLoader;

	/** Current ImageContainer. (either in-flight or finished) */
	private ImageContainer mImageContainer;
	
	private OnImageLoaded mImageLoaded;
	
	private static final String LCOAL_PRE = "local:";

	public MagicImageView(Context context) {
		this(context, null);
		init();
	}

	public MagicImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		init();
	}

	public MagicImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mImageLoader = Freeza.getInstance().getImageLoader();
		mDefaultWidth = getResources().getDisplayMetrics().widthPixels;
	}

	public void loadImage(String url, int defaultImage) {
		mUrl = url;
		if (defaultImage != 0) {
			mDefaultImage = getResources().getDrawable(defaultImage);
		}
		loadImageIfNecessary(false);
	}

	public void loadImage(String url) {
		mUrl = url;
		loadImageIfNecessary(false);
	}
	
	public void loadLocalImage(String url, int width,Drawable defaultImage, OnImageLoaded imageLoaded ) {
		mUrl = LCOAL_PRE+url+"_"+width;
		mDefaultWidth = width;
		mImageLoaded = imageLoaded;
		if (defaultImage != null) {
			mDefaultImage = defaultImage;
//			mDefaultImage = getResources().getDrawable(defaultImage);
		}
		loadImageIfNecessary(false);
	}

	public void loadImage(String url, Drawable defaultImage) {
		mUrl = url;
		mDefaultImage = defaultImage;
		loadImageIfNecessary(false);
	}

	/**
	 * Sets the error image resource ID to be used for this view in the event
	 * that the image requested fails to load.
	 */
	public void setErrorImageResId(int errorImage) {
		mErrorImageId = errorImage;
	}

	/**
	 * Loads the image for the view if it isn't already loaded.
	 * 
	 * @param isInLayoutPass
	 *            True if this was invoked from a layout pass, false otherwise.
	 */
	private void loadImageIfNecessary(final boolean isInLayoutPass) {
		int width = getWidth();
		int height = getHeight();

		boolean isFullyWrapContent = getLayoutParams().height == LayoutParams.WRAP_CONTENT
				&& getLayoutParams().width == LayoutParams.WRAP_CONTENT;
		if (width == 0 && height == 0 && !isFullyWrapContent) {
			return;
		}

		if (TextUtils.isEmpty(mUrl)) {
			if (mImageContainer != null) {
				mImageContainer.cancelRequest();
				mImageContainer = null;
			}
			if (mDefaultImage != null) {
				setImageDrawable(mDefaultImage);
			}
			return;
		}
		// if there was an old request in this view, check if it needs to be
		// canceled.
		if (mImageContainer != null && mImageContainer.getRequestUrl() != null) {
			if (mImageContainer.getRequestUrl().equals(mUrl)) {
				// if the request is from the same URL, return.
				return;
			} else {
				// if there is a pre-existing request, cancel it if it's
				// fetching a different URL.
				mImageContainer.cancelRequest();
				setImageBitmap(null);
			}
		}

		if (mImageLoader == null) {
			return;
		}
		ImageContainer newContainer = mImageLoader
				.get(mUrl, new ImageListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (mErrorImageId != 0) {
							setImageResource(mErrorImageId);
						} else {
							setImageDrawable(mDefaultImage);
						}
					}

					@Override
					public void onResponse(final ImageContainer response,
							boolean isImmediate) {
						// If this was an immediate response that was delivered
						// inside of a layout
						// pass do not set the image immediately as it will
						// trigger a requestLayout
						// inside of a layout. Instead, defer setting the image
						// by posting back to
						// the main thread.
						if (isImmediate && isInLayoutPass) {
							post(new Runnable() {
								@Override
								public void run() {
									onResponse(response, false);
								}
							});
							return;
						}
						if (response.getBitmap() != null
								&& !TextUtils.isEmpty(response.getRequestUrl())
								&& response.getRequestUrl().equals(mUrl)) {
							if(isImmediate){
								mShouldAlphaAnimate = false;
							}
							setImageBitmap(response.getBitmap());
						} else if (mDefaultImage != null) {
							setImageDrawable(mDefaultImage);
						}
						if(mImageLoaded != null){
							mImageLoaded.onLoaded();
						}
					}
				},(int) (mDefaultWidth), (int) (mDefaultWidth));

		// update the ImageContainer to be the new bitmap container.
		mImageContainer = newContainer;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		loadImageIfNecessary(true);
	}

	@Override
	protected void onDetachedFromWindow() {
		if (mImageContainer != null) {
			// If the view was bound to an image request, cancel it and clear
			// out the image from the view.
			mImageContainer.cancelRequest();
			setImageBitmap(null);
			// also clear out the container so we can reload the image if
			// necessary.
			mImageContainer = null;
		}
		super.onDetachedFromWindow();
	}
	
	public void setDefaultWidth(int width){
		mDefaultWidth = width;
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		invalidate();
	}

	public void reset() {
		if (mImageContainer != null) {
			mImageContainer.reSet();
		}
	}
	
	public interface OnImageLoaded{
		public void onLoaded();
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		if(mShouldAlphaAnimate) {
			ObjectAnimator.ofFloat(this, "alpha", 0, 1).setDuration(mAnimateDuration).start();
		}
	}

	public void setAnimateDuration(int duration){
		this.mAnimateDuration = duration;
	}
	
	public void setAnimate(boolean animate){
		mShouldAlphaAnimate = animate;
	}
}

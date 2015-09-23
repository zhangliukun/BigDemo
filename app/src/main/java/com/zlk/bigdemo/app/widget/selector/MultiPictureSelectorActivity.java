package com.zlk.bigdemo.app.widget.selector;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.zlk.bigdemo.R;
import com.zlk.bigdemo.app.BaseActivity;
import com.zlk.bigdemo.app.widget.selector.adapter.AlbumAdapter;
import com.zlk.bigdemo.app.widget.selector.adapter.GalleryPickerAdapter;
import com.zlk.bigdemo.app.widget.selector.support.AlbumItem;
import com.zlk.bigdemo.app.widget.selector.support.ImageItem;
import com.zlk.bigdemo.app.widget.selector.utils.AlbumManager;
import com.zlk.bigdemo.freeza.Freeza;
import com.zlk.bigdemo.freeza.thread.ThreadFactory;
import com.zlk.bigdemo.freeza.util.CameraUtils;

import java.util.ArrayList;
import java.util.List;

public class MultiPictureSelectorActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener {

	public static final int REQUEST_CODE = 123;
	public static final String RET_KEY = "bitmapUrls";
    public static final int MODE_SINGLE = 1;

	private int mMaxPictureCount = MAX_SELECTED_PIC_COUNT;
	public static final int MAX_SELECTED_PIC_COUNT = 9;
	private Handler mHandler = new Handler();

	public static void startSingleActivity(Activity context, int requestCode) {
		Intent intent = new Intent(context, MultiPictureSelectorActivity.class);
        intent.putExtra("mode", MODE_SINGLE);
		context.startActivityForResult(intent, requestCode);
	}

    public static void startActivity(Activity context, int requestCode) {
        Intent intent = new Intent(context, MultiPictureSelectorActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

	public static void startActivity(Activity context, int current_count,
			int requestCode) {
		Intent intent = new Intent(context, MultiPictureSelectorActivity.class);
		intent.putExtra("current_count", current_count);
		context.startActivityForResult(intent, requestCode);
	}

	public static void startActivity(Activity context, int current_count,
			int requestCode, int maxCount) {
		Intent intent = new Intent(context, MultiPictureSelectorActivity.class);
		intent.putExtra("current_count", current_count);
		intent.putExtra("max_count", maxCount);
		context.startActivityForResult(intent, requestCode);
	}

	public static void startActivityForDisk(Activity context, int current_count,
									 int requestCode, int maxCount, Fragment fragment) {
		Intent intent = new Intent(context, MultiPictureSelectorActivity.class);
		intent.putExtra("current_count", current_count);
		intent.putExtra("max_count", maxCount);
		intent.putExtra("for_disk", true);
		fragment.startActivityForResult(intent, requestCode);
	}

	private GridView mGridView;
	private GalleryPickerAdapter mGridAdapter;
	private ArrayList<ImageItem> mImagePathList = new ArrayList<ImageItem>();

	private ListView mMenuListView;
	private AlbumAdapter mMenuAdapter;
	private List<AlbumItem> mMenuList = new ArrayList<AlbumItem>();
	private View mMenuBg;

	private TextView mCompleteBtn;
	private TextView mShowMenu;
	private TextView mPreView;
	private ArrayList<String> mSelectImagePath = new ArrayList<String>();
	private int mCurrentCount = 0;
	private Uri mPictureUri;
	private long mBucketId;
    private int mMode;
    private boolean for_disk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.multi_picture_selector);
		Intent intent = getIntent();
		mCurrentCount = intent.getIntExtra("current_count", 0);
		mMaxPictureCount = intent.getIntExtra("max_count",
				MAX_SELECTED_PIC_COUNT);
        mMode = intent.getIntExtra("mode",0);
		for_disk = intent.getBooleanExtra("for_disk", false);
		initView();
		initData();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		AlbumManager.getInstance().clear();
		if (Freeza.getInstance().getImageCache() != null) {
			Freeza.getInstance().getImageCache().clearCaches();
		}
	}

	private void initView() {
		findViewById(R.id.multi_title_layout).setOnClickListener(this);
		mGridView = (GridView) findViewById(R.id.multi_picture_select_albums);
		mGridAdapter = new GalleryPickerAdapter(this, mImagePathList,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						int position = (Integer) v.getTag();
						ImageItem imageItem = mImagePathList.get(position);
						if (imageItem.getImageId() == 0
								&& TextUtils.isEmpty(imageItem.getImagePath())
								&& position == 0) {
							mPictureUri = CameraUtils.openCapture(
									MultiPictureSelectorActivity.this,
									CameraUtils.REQUEST_IMAGE_CAPTURE);
						} else {
							if (imageItem.isSelect() == 0) {
								if (mSelectImagePath.size() >= mMaxPictureCount
										- mCurrentCount) {
									Toast.makeText(
											MultiPictureSelectorActivity.this,
											getResources()
													.getString(
															R.string.multi_picture_selected_full),
											Toast.LENGTH_SHORT).show();
									return;
								}
								imageItem.setSelect(1);
								if (!mSelectImagePath.contains(imageItem
										.getImagePath())) {
									mSelectImagePath.add(imageItem.getImagePath());
								}
							} else {
								imageItem.setSelect(0);
								mSelectImagePath.remove(imageItem.getImagePath());
							}
						}
						enableCompleteButton();
						mGridAdapter.notifyDataSetChanged();
					}
				}, mMode == 0 ? null :new OnClickListener() {

					@Override
					public void onClick(View v) {
						int position = (Integer) v.getTag();
						ImageItem imageItem = mImagePathList.get(position);
						if (imageItem.getImageId() == 0
								&& TextUtils.isEmpty(imageItem.getImagePath())
								&& position == 0) {
							mPictureUri = CameraUtils.openCapture(
									MultiPictureSelectorActivity.this,
									CameraUtils.REQUEST_IMAGE_CAPTURE);
						} else {
                            if(mMode == MODE_SINGLE){
                                mSelectImagePath.clear();
                                mSelectImagePath.add(imageItem.getImagePath());
                                complete();
                            }else{
                                ArrayList<ImageItem> list = new ArrayList<ImageItem>();
                                list.addAll(mImagePathList);
                                if (mBucketId == 0) {
                                    position = position - 1;
                                    list.remove(0);
                                }
                                ShowAlbumImageActivity.startActivity(
                                        MultiPictureSelectorActivity.this, list,
                                        mSelectImagePath, position,
                                        mMaxPictureCount - mCurrentCount,
                                        REQUEST_CODE, for_disk);
                            }
						}
					}
				},mMode);
		mGridView.setAdapter(mGridAdapter);

		mMenuListView = (ListView) findViewById(R.id.multi_picture_select_listview);
		mMenuAdapter = new AlbumAdapter(this, mMenuList);
		mMenuListView.setAdapter(mMenuAdapter);
		mMenuListView.setOnItemClickListener(this);
		mMenuBg = findViewById(R.id.multi_menu_bg);
		mMenuBg.setOnClickListener(this);

		mCompleteBtn = (TextView) findViewById(R.id.picture_selector_save);
		mCompleteBtn.setOnClickListener(this);
		mShowMenu = (TextView) findViewById(R.id.multi_select);
		mShowMenu.setOnClickListener(this);
		mPreView = (TextView) findViewById(R.id.multi_look);
		mPreView.setOnClickListener(this);
        if(mMode == MODE_SINGLE){
            mCompleteBtn.setVisibility(View.GONE);
            mPreView.setVisibility(View.GONE);
        }

	}

	private void initData() {
		enableCompleteButton();
		loadImage(0);
	}

	private void loadImage(final long bucket_id) {
		ThreadFactory.createThread().start(new Runnable() {
			@Override
			public void run() {
				final List<ImageItem> list = AlbumManager.getInstance()
						.getAlbumPicture(MultiPictureSelectorActivity.this,
								bucket_id);
				if (list != null) {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							mImagePathList.clear();
							reset(list);

							mImagePathList.addAll(list);
							mGridAdapter.notifyDataSetChanged();
						}
					});
				}
			}
		});
	}

	private void reset(List<ImageItem> list) {
		if (mSelectImagePath.size() > 0) {
			for (ImageItem item : list) {
				item.setSelect(0);
				for (String imagePath : mSelectImagePath) {
					if (item.getImagePath().equals(imagePath)) {
						item.setSelect(1);
					}
				}
			}
		}
		enableCompleteButton();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mBucketId = id;
		for (AlbumItem item : mMenuList) {
			item.isChecked = false;
		}
		mMenuList.get(position).isChecked = true;
		loadImage(id);
		mShowMenu.setText(mMenuList.get(position).fileFolderName);
		mMenuAdapter.notifyDataSetChanged();
		setMenu();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case CameraUtils.REQUEST_IMAGE_CAPTURE:
				if (mPictureUri != null) {
					mSelectImagePath.clear();
					mSelectImagePath.add(mPictureUri.getPath());
					complete();
				}
				break;
			case REQUEST_CODE:
				List<String> list = data.getStringArrayListExtra("select");
				if (list != null) {
					mSelectImagePath.clear();
					mSelectImagePath.addAll(list);
				}
				boolean isfinish = data.getBooleanExtra("isFinish", false);
				if (isfinish) {
					complete();
				} else {
					reset(mImagePathList);
					mGridAdapter.notifyDataSetChanged();
				}
				break;
			}
		}
	}

	private void enableCompleteButton() {
		int size = mSelectImagePath.size();
		if (size > 0) {
			if (for_disk){
				mCompleteBtn.setText(getString(
						R.string.multi_disk_selected_text, size,
						mMaxPictureCount - mCurrentCount));
			} else {
				mCompleteBtn.setText(getString(
						R.string.multi_picture_selected_text, size,
						mMaxPictureCount - mCurrentCount));
			}
			mPreView.setText(getString(R.string.pre_view_desc, size));
			mCompleteBtn.setEnabled(true);
			mPreView.setEnabled(true);
		} else {
			if (for_disk){
				mCompleteBtn.setText(getString(R.string.disk_upload2) +"(0/" + mMaxPictureCount + ")");
			} else {
				mCompleteBtn.setText(getString(R.string.send)+"(0/" + mMaxPictureCount + ")");
			}
			mPreView.setText(getString(R.string.pre_view));
			mCompleteBtn.setEnabled(false);
			mPreView.setEnabled(false);
		}

	}

	private void showMenu() {
		if (mMenuList.size() == 0) {
			ThreadFactory.createThread().start(new Runnable() {

				@Override
				public void run() {
					final List<AlbumItem> list = AlbumManager.getInstance()
							.getAlbumsPath(MultiPictureSelectorActivity.this);
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							if (list != null) {
								mMenuList.addAll(list);
							}
							mMenuAdapter.notifyDataSetChanged();
						}
					});
				}
			});
		}
		mMenuListView.setVisibility(View.VISIBLE);
		mMenuBg.setVisibility(View.VISIBLE);
		AnimatorSet set = new AnimatorSet();
		Animator inputAni = ObjectAnimator.ofFloat(mMenuListView,
				"translationY", mMenuListView.getHeight(), 0);
		inputAni.setDuration(300);

		Animator commendAni = ObjectAnimator.ofFloat(mMenuBg, "alpha", 0, 0.7f);
		commendAni.setDuration(300);

		set.playTogether(inputAni, commendAni);
		set.start();
	}

	private void hideMenu() {
		AnimatorSet set = new AnimatorSet();
		Animator inputAni = ObjectAnimator.ofFloat(mMenuListView,
				"translationY", 0, mMenuListView.getHeight());
		inputAni.setDuration(300);

		Animator commendAni = ObjectAnimator.ofFloat(mMenuBg, "alpha", 0.7f, 0);
		commendAni.setDuration(300);

		set.playTogether(inputAni, commendAni);
		set.addListener(new Animator.AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mMenuListView.setVisibility(View.GONE);
				mMenuBg.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mMenuListView.setVisibility(View.GONE);
				mMenuBg.setVisibility(View.GONE);
			}
		});
		set.start();
	}

	@Override
	public void onBackPressed() {
		if (mMenuListView.getVisibility() == View.VISIBLE) {
			setMenu();
		} else {
			super.onBackPressed();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.multi_title_layout:
			mGridView.setSelection(0);
			break;
		case R.id.picture_selector_save:
			complete();
			break;
		case R.id.multi_select:
			setMenu();
			break;
		case R.id.multi_look:
			if (mSelectImagePath.size() == 0) {
				return;
			}
			ArrayList<ImageItem> list = new ArrayList<ImageItem>();
			for (String path : mSelectImagePath) {
				ImageItem item = AlbumManager.getInstance().getImageItem(path);
				if (item != null) {
					list.add(item);
				}
			}
			ShowAlbumImageActivity.startActivity(this, list, mSelectImagePath,
					0, MAX_SELECTED_PIC_COUNT - mCurrentCount, REQUEST_CODE, for_disk);
			break;
		case R.id.multi_menu_bg:
			setMenu();
			break;
		default:
			break;
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void setMenu() {
		if (mMenuListView.getVisibility() == View.GONE) {
			showMenu();
		} else {
			hideMenu();
		}
	}

	private void complete() {
		mCompleteBtn.setEnabled(false);
		int len = mSelectImagePath.size();
		Intent intent = new Intent();
		if (len > 1) {
			String[] paths = new String[len];
			for (int i = 0; i < len; i++) {
				String tempStr = mSelectImagePath.get(i);
				if (paths != null) {
					paths[i] = tempStr;
				}
			}
			intent.putExtra(RET_KEY, paths);
			setResult(RESULT_OK, intent);
		} else if (len == 1) {
			String path = mSelectImagePath.get(0);

			intent.putExtra(RET_KEY, new String[] { path });
			setResult(RESULT_OK, intent);
		}
		finish();
	}

	/**
	 * 所有相关的界面中，都禁止通过长按的方式显示键盘
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}

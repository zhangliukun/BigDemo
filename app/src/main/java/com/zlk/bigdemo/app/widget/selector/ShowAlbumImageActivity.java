package com.zlk.bigdemo.app.widget.selector;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.app.BaseActivity;
import com.zlk.bigdemo.app.widget.selector.adapter.ShowAlbumImageAdapter;
import com.zlk.bigdemo.app.widget.selector.support.ImageItem;

import java.util.ArrayList;
import java.util.List;

public class ShowAlbumImageActivity extends BaseActivity implements  OnClickListener{

	public static void startActivity(Activity context,
			ArrayList<ImageItem> urls, ArrayList<String> selectList,
			int position, int maxCount, int requestCode, boolean for_disk) {
		Intent intent = new Intent(context, ShowAlbumImageActivity.class);
		intent.putParcelableArrayListExtra("urls", urls);
		intent.putStringArrayListExtra("select", selectList);
		intent.putExtra("position", position);
		intent.putExtra("max", maxCount);
		intent.putExtra("for_disk", for_disk);
		context.startActivityForResult(intent, requestCode);
	}

	private ViewPager mViewPage;
	private ShowAlbumImageAdapter mPictureAdapter;
	private TextView mCompleteView;
	private TextView mCountView;
	private TextView mSelectBox;
	private View mTitleView;
	private View mBottomView;
	private ArrayList<ImageItem> mUrlList;
	private ArrayList<String> mSelectList = new ArrayList<String>();
	private int mCurrentPosition;
	private int mMaxCount;
	private boolean isVisible = true;
	private boolean for_disk = false;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mUrlList = getIntent().getParcelableArrayListExtra("urls");
		List<String> list = getIntent().getStringArrayListExtra("select");
		if (list != null) {
			mSelectList.addAll(list);
		}
		mCurrentPosition = getIntent().getIntExtra("position", 0);
		mMaxCount = getIntent().getIntExtra("max", 0);
		for_disk = getIntent().getBooleanExtra("for_disk", false);
		if (mUrlList == null || mUrlList.size() == 0) {
			finish();
			return;
		}
		setContentView(R.layout.show_album_image);
		initView();
		initData();
	}

	private void initView() {
		findViewById(R.id.back).setOnClickListener(this);
		mTitleView = findViewById(R.id.show_album_title);
		mBottomView = findViewById(R.id.show_album_bottom);
		mCompleteView = (TextView) findViewById(R.id.show_album_complete);
		mCompleteView.setOnClickListener(this);
		mCountView = (TextView) findViewById(R.id.show_album_count);
		mSelectBox = (TextView) findViewById(R.id.show_album_checkbox);
		mViewPage = (ViewPager) findViewById(R.id.show_album_image_pageview);
		mPictureAdapter = new ShowAlbumImageAdapter(
				getSupportFragmentManager(), mUrlList, new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (isVisible) {
							hideTitle();
						} else {
							showTitle();
						}
					}
				});
		mViewPage.setAdapter(mPictureAdapter);
		mViewPage.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mCurrentPosition = position;
				initData();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		mViewPage.setCurrentItem(mCurrentPosition);
		mSelectBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageItem imageItem = mUrlList.get(mCurrentPosition);
				if (imageItem.isSelect() == 0) {
					if (mSelectList.size() >= mMaxCount) {
						Toast.makeText(ShowAlbumImageActivity.this,
								R.string.multi_picture_selected_full,
								Toast.LENGTH_SHORT).show();
						return;
					}
					imageItem.setSelect(1);
					if (!mSelectList.contains(imageItem.getImagePath())) {
						mSelectList.add(imageItem.getImagePath());
					}
				} else {
					imageItem.setSelect(0);
					mSelectList.remove(imageItem.getImagePath());
				}
				initData();
			}
		});
	}

	private void initData() {
		mCountView.setText((mCurrentPosition + 1) + "/" + mUrlList.size());
		mSelectBox.setSelected(mUrlList.get(mCurrentPosition).isSelect() == 1);
		enableCompleteButton();
	}

	private void showTitle() {
		isVisible = true;
		AnimatorSet set = new AnimatorSet();
		Animator inputAni = ObjectAnimator.ofFloat(mTitleView, "translationY",
				-mTitleView.getHeight(), 0);
		inputAni.setDuration(300);

		Animator commendAni = ObjectAnimator.ofFloat(mBottomView,
				"translationY", mTitleView.getHeight(), 0);
		commendAni.setDuration(300);

		set.playTogether(inputAni, commendAni);
		set.start();
	}

	private void hideTitle() {
		AnimatorSet set = new AnimatorSet();
		Animator inputAni = ObjectAnimator.ofFloat(mTitleView, "translationY",
				0, -mTitleView.getHeight());
		inputAni.setDuration(300);

		Animator commendAni = ObjectAnimator.ofFloat(mBottomView,
				"translationY", 0, mTitleView.getHeight());
		commendAni.setDuration(300);
		set.addListener(new Animator.AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				isVisible = false;
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
		set.playTogether(inputAni, commendAni);
		set.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			onBackPressed();
			break;
		case R.id.show_album_complete:
			onBack(true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		onBack(false);
	}
	
	private void enableCompleteButton() {
		
		if (mSelectList == null || mSelectList.size() == 0) {
			mCompleteView.setText(getString(R.string.complete));
			mCompleteView.setEnabled(false);
		} else {

			if (for_disk){
				mCompleteView.setText(getString(
						R.string.multi_disk_selected_text, mSelectList.size(),
						mMaxCount));
			} else {
				mCompleteView.setText(getString(
						R.string.multi_picture_selected_text, mSelectList.size(),
						mMaxCount));
			}
			mCompleteView.setEnabled(true);
		}
	}

	private void onBack(boolean isFinish) {
		Intent intent = new Intent();
		intent.putStringArrayListExtra("select", mSelectList);
		intent.putExtra("isFinish", isFinish);
		setResult(RESULT_OK, intent);
		finish();
	}
}

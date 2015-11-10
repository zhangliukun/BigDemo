package com.zlk.bigdemo.app.main;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.app.BaseActivity;
import com.zlk.bigdemo.app.main.fragment.MyFragment;
import com.zlk.bigdemo.app.main.fragment.SecondFragment;
import com.zlk.bigdemo.app.main.fragment.ThirdFragment;
import com.zlk.bigdemo.app.opencv.Tutorial1Activity;
import com.zlk.bigdemo.freeza.util.CameraUtils;
import com.zlk.bigdemo.freeza.widget.selector.MultiPictureSelectorActivity;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 进入微信的主界面activity，这个activity中包含四个fragment，采用viewPager的滑动实现。
 * @author zlk
 *
 */
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {


    public static String TITLE = "title";
    List<Fragment>        mTabs         = new ArrayList<Fragment>();
    FragmentPagerAdapter  fragmentPagerAdapter;
    String[]              fragmentTitle = new String[] { "tab1", "tab2", "tab3" };
    String currentFragment = fragmentTitle[0];

    @Bind(R.id.id_viewpager)
    ViewPager mViwePager;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind({ R.id.imageView1, R.id.imageView2, R.id.imageView3 })
    List<ImageView> imageViews;

    @OnClick({ R.id.imageView1, R.id.imageView2, R.id.imageView3 })
    public void clickTabView(ImageView imageView) {
        clickTab(imageView);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initDatas();

    }

    private void initView() {
        mToolbar.setTitle("zalezone");
        mToolbar.setLogo(R.drawable.ic_launcher);
        mToolbar.setSubtitle("china");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(mToolbar);
        final Activity activity = this;
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_action_add_picture:
                        //MultiPictureSelectorActivity.startActivity(this,MultiPictureSelectorActivity.REQUEST_CODE);
                        MultiPictureSelectorActivity.startActivity(activity, 0, CameraUtils.REQUEST_PHOTO_LIBRARY, 9);
                        break;
                    case R.id.menu_action_opencv:
                        Intent opencv = new Intent(activity, Tutorial1Activity.class);
                        startActivity(opencv);
                }
                return true;
            }
        });
    }


    /**
     * 初始化fragment的信息
     */
    public void initDatas()
    {
        addFragmentTabs();
        // 定义viewpager的适配器，使得可以滑动操作
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }
        };

        mViwePager.setAdapter(fragmentPagerAdapter);
        mViwePager.addOnPageChangeListener(this);
    }

    /**
     * 增加fragment的tab
     */
    private void addFragmentTabs(){
        //朋友列表页面
        SecondFragment friendListFragment = new SecondFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, fragmentTitle[1]);
        friendListFragment.setArguments(bundle);
        mTabs.add(friendListFragment);
        //会话列表页面
        MyFragment myFragment = new MyFragment();
        bundle.clear();
        bundle.putString(TITLE, fragmentTitle[0]);
        myFragment.setArguments(bundle);
        mTabs.add(myFragment);
        //发现朋友圈页面
        ThirdFragment discoverFragment = new ThirdFragment();
        bundle.clear();
        bundle.putString(TITLE, fragmentTitle[2]);
        discoverFragment.setArguments(bundle);
        mTabs.add(discoverFragment);
    }


    /**
     * 点击tab标签
     *
     * @param v
     */
    public void clickTab(View v)
    {


        switch (v.getId()) {
            case R.id.imageView1:
                if (currentFragment.equals(fragmentTitle[0])){
                    return;
                }
                resetOtherTabs();
                currentFragment = fragmentTitle[0];
                mViwePager.setCurrentItem(0, false);
                imageViews.get(0).setBackgroundColor(getResources().getColor(R.color.footbar_press));
                break;
            case R.id.imageView2:
                if (currentFragment.equals(fragmentTitle[1])){
                    return;
                }
                resetOtherTabs();
                currentFragment = fragmentTitle[1];
                mViwePager.setCurrentItem(1, false);
                imageViews.get(1).setBackgroundColor(getResources().getColor(R.color.footbar_press));
                break;
            case R.id.imageView3:
                if (currentFragment.equals(fragmentTitle[2])){
                    return;
                }
                resetOtherTabs();
                currentFragment = fragmentTitle[2];
                mViwePager.setCurrentItem(2, false);
                imageViews.get(2).setBackgroundColor(getResources().getColor(R.color.footbar_press));
                break;
            default:
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : mTabs) {
            invokeOnActivityResult(fragment, requestCode, resultCode, data);
        }
    }


    /**
     * 重置其他的View的标签
     *
     */

    public void resetOtherTabs()
    {
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setBackgroundColor(getResources().getColor(R.color.toolbar_bg));
        }
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int arg0) {
        Log.i("tab_count", String.valueOf(arg0));
        clickTab(imageViews.get(arg0));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //在toolbar中进行操作
        int id = item.getItemId();
        switch (id){
            case R.id.menu_action_add_picture:
                //MultiPictureSelectorActivity.startActivity(this,MultiPictureSelectorActivity.REQUEST_CODE);
                MultiPictureSelectorActivity.startActivity(this, 0, CameraUtils.REQUEST_PHOTO_LIBRARY, 9);
                break;
            case R.id.menu_action_opencv:
                Intent opencv = new Intent(this, Tutorial1Activity.class);
                this.startActivity(opencv);
        }


        return super.onOptionsItemSelected(item);
    }

//    private void changeHeadImage(){
//        MultiPictureSelectorActivity.startSingleActivity(getActivity(), MultiPictureSelectorActivity.REQUEST_CODE);
//    }
}
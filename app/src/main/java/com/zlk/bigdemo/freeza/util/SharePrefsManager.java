package com.zlk.bigdemo.freeza.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import com.zlk.bigdemo.freeza.BaseApplication;

import java.lang.reflect.Type;

public class SharePrefsManager {

	private static SharePrefsManager sInstance;
	private static SharePrefsManager2 sCommonInstance;
	private SharedPreferences mManager;
	private String mDeviceId;

	private static final String SP_NAME = "youban";

	private SharePrefsManager() {
	}

	public static SharePrefsManager getInstance() {
		if (sInstance == null) {
			sInstance = new SharePrefsManager();
		}
		return sInstance;
	}

	public static SharePrefsManager2 getCommonInstance() {
		if (sCommonInstance == null) {
			sCommonInstance = new SharePrefsManager2();
		}
		return sCommonInstance;
	}

	public void putBoolean(String key, boolean value) {
        if(mManager == null){
            return;
        }
		mManager.edit().putBoolean(key, value).apply();
	}

	public void putString(String key, String value) {
        if(mManager == null){
            return;
        }
		mManager.edit().putString(key, value).apply();
	}

	public void putInt(String key, int value) {
        if(mManager == null){
            return;
        }
		mManager.edit().putInt(key, value).apply();
	}
	
	public void putLong(String key, long value) {
        if(mManager == null){
            return;
        }
		mManager.edit().putLong(key, value).apply();
	}

	public long getLong(String key) {
        if(mManager == null){
            return 0;
        }
		return mManager.getLong(key, 0);
	}

	public int getInt(String key) {
        if(mManager == null){
            return 0;
        }
		return mManager.getInt(key, 0);
	}

	public Editor editor() {
        if(mManager == null){
            return getCommonInstance().editor();
        }
		return mManager.edit();
	}

	public String getString(String key) {
        if(mManager == null){
            return "";
        }
		return mManager.getString(key, "");
	}

	public boolean getBoolean(String key) {
        if(mManager == null){
            return false;
        }
		if(!mManager.contains(key)){
			return false;
		}
		return mManager.getBoolean(key, false);
	}
	public boolean getBoolean(String key, boolean a) {
		if(mManager == null){
			return a;
		}
		if(!mManager.contains(key)){
			return a;
		}
		return mManager.getBoolean(key, a);
	}

	public boolean contain(String key){
        if(mManager == null){
            return false;
        }
        return mManager.contains(key);
    }

	public String getDeviceId() {
		if (TextUtils.isEmpty(mDeviceId)) {
			TelephonyManager tm = (TelephonyManager) BaseApplication
					.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
			try {
				mDeviceId = tm.getDeviceId();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return mDeviceId;
	}

	private void check() {
		if (Thread.currentThread().getId() != Looper.getMainLooper()
				.getThread().getId()) {
			throw new RuntimeException("Please call this method in Main Thread");
		}
	}

	public void init(String userId) {
		mManager = BaseApplication.getInstance().getSharedPreferences(
				SP_NAME + userId, Context.MODE_PRIVATE);
	}

	public void recycle() {
		sInstance = null;
		mManager = null;
	}

	public void setBean(String key, Object bean) {
		editor().putString(key, Jsons.toJson(bean)).apply();
	}

	public <T> T getBean(String key, Type clazz) {
		if(mManager != null) {
			String json = mManager.getString(key, null);
			if (TextUtils.isEmpty(json)) return null;
			return Jsons.fromJson(json, clazz);
		}
		return null;
	}
}

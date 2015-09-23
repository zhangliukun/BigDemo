package com.zlk.bigdemo.freeza.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.zlk.bigdemo.freeza.BaseApplication;


public class SharePrefsManager2 {

	private static SharePrefsManager2 sInstance;
	private SharedPreferences mManager;

    private static final String SP_NAME = "youban";

	SharePrefsManager2() {
	}

	public static SharePrefsManager2 getInstance() {
		if (sInstance == null) {
			sInstance = new SharePrefsManager2();
		}
		return sInstance;
	}
	
	public void putBoolean(String key, boolean value) {
		check();
		mManager.edit().putBoolean(key, value).apply();
	}

	public void putString(String key, String value) {
		check();
		mManager.edit().putString(key, value).apply();
	}
	
	public void putInt(String key, int value){
		check();
		mManager.edit().putInt(key, value).apply();
	}
	
	public long getLong(String key){
		check();
		return mManager.getLong(key, 0);
	}
	
	public int getInt(String key){
		check();
		return mManager.getInt(key, 0);
	}

	public int getInt(String key, int val) {
		check();
		return mManager.getInt(key, val);
	}
	
	public Editor editor(){
		check();
		return mManager.edit();
	}

	public String getString(String key) {
		check();
		return mManager.getString(key, "");
	}
	
	public boolean getBoolean(String key){
		check();
		return mManager.getBoolean(key, false);
	}

	public boolean getBoolean(String key,boolean deValue){
		check();
		return mManager.getBoolean(key,deValue);
	}
	
	public void putLong(String key, long value){
		check();
		mManager.edit().putLong(key, value).apply();
	}

	public void put(Context context, String fileName, String key, String value) {
		SharedPreferences state = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		state.edit().putString(key, value).apply();
	}

	public String get(Context context, String fileName, String key, String defaultValue) {
		SharedPreferences state = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return state.getString(key,defaultValue);
	}

	private void check(){
		if(mManager == null){
			mManager = BaseApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
	}


	public void recycle() {
		sInstance = null;
		mManager = null;
	}
}

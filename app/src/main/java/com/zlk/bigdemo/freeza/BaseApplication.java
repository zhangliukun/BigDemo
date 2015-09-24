package com.zlk.bigdemo.freeza;

import android.app.Application;
import android.util.Log;

public class BaseApplication extends Application {

	private static BaseApplication sInstance;
	
	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	public static BaseApplication getInstance(){
		if (sInstance==null){
			Log.i("application","null");
		}

		return sInstance;
	}

}

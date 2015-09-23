package com.zlk.bigdemo.freeza.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.zlk.bigdemo.freeza.BaseApplication;

public class VersionUtils {
	public static boolean isFirstEntry() {
		int lastVersionCode = SharePrefsManager.getInstance().getInt(
				"last_version_code");
		int currentVersionCode = getVerCode(BaseApplication.getInstance());
		return currentVersionCode > lastVersionCode;
	}

	public static void updateVersion() {
		int currentVersionCode = getVerCode(BaseApplication.getInstance());
		SharePrefsManager.getInstance().putInt("last_version_code",
				currentVersionCode);
	}

	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verCode;
	}

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return verName;
	}
	
	public static String getVerName() {
		String verName = "";
		Context context = BaseApplication.getInstance();
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return verName;
	}
}

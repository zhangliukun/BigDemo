package com.zlk.bigdemo.freeza.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.regex.Pattern;

public class Util {
	
	/**
     * Checks if the application is in the background (i.e behind another application's Activity).
     * 
     * @param context
     * @return true if another application is above this one.
     */
    public static boolean isApplicationBroughtToBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }

        return false;


    }
    
    public static boolean isApplicationRunningForeground (Context context)  
    {  
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);  
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();  
        if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName()))  
        {  
            return true ;  
        }  
       
        return false ;  
    }  
    
	public static  int getNumCores() {
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}
		}

		int size = 2;

		try {
			File dir = new File("/sys/devices/system/cpu/");
			File[] files = dir.listFiles(new CpuFilter());
			if (files != null && files.length > 0) {
				size = files.length;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return size;
	}
	
	/***
	 * 获得sdk版本
	 * **/
	public static int getAndroidSDKVersion() {  
        int version = 0;  
        try {  
            version = Integer.valueOf(android.os.Build.VERSION.SDK);  
        } catch (NumberFormatException e) {  
        }  
        return version;  
    }
}

package com.zlk.bigdemo.app.utils;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cookies 帮助类.
 * 
 * @author zijunlzj
 * 
 */
public class CookieUtil {

	/**
	 * 构建cookie 字符串
	 * 
	 * @param cookies
	 * @param domain
	 * @return
	 */
	public static String buildCookieString(String key, String value,
			String domain) {
		StringBuilder cookieToken = new StringBuilder();
		cookieToken.append(key);
		cookieToken.append(" = ");
		cookieToken.append(value);
		cookieToken.append(";");
		cookieToken.append("domain =  ");
		cookieToken.append(domain);
		cookieToken.append(";path = /");
		return cookieToken.toString();
	}

	/**
	 * 向对应的url 中注入cookie
	 * 
	 * @param context
	 * @param url
	 * @param cookies
	 * @return
	 */
	public static String injectCookie(Context context, String url,
			Map<String,String> cookies) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		String domain = getDomain(url);
		for(Entry<String, String> entry:cookies.entrySet()){
			String cookie = buildCookieString(entry.getKey(), entry.getValue(), domain);
			cookieManager.setCookie(url, cookie);
		}
		CookieSyncManager.getInstance().sync();
		return cookieManager.getCookie(url);
	}
	
	public static void clearCookies(Context context) {
	    CookieSyncManager.createInstance(context);
	    CookieManager cookieManager = CookieManager.getInstance();
	    cookieManager.removeSessionCookie();
	    CookieSyncManager.getInstance().sync();
	}

	public static String getDomain(String url) {
		String content = null;
		if (!TextUtils.isEmpty(url) && url.contains("http")) {
			Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
			Matcher m = p.matcher(url);
			if (m.find()) {
				content = m.group();
			}
		}
		return content;
	}

}

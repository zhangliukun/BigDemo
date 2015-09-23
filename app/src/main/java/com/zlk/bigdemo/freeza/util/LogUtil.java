package com.zlk.bigdemo.freeza.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * TODO  当前debug级别>=指定级别时才debug 而不是<=
 */
public class LogUtil {
	private static int sLevel = Log.VERBOSE;
	
	 /**
     * Send a DEBUG log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public static int d(String tag, String msg) {
		if (sLevel <= Log.DEBUG&& !TextUtils.isEmpty(msg)) {
			return Log.d(tag, msg);
		}
		return 0;
	}

	/**
     * Send a DEBUG log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public static int d(String tag, String msg, Throwable tr) {
		if (sLevel <= Log.DEBUG&& !TextUtils.isEmpty(msg)) {
			return Log.d(tag, msg, tr);
		}
		return 0;
	}

	 /**
     * Send an ERROR log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public static int e(String tag, String msg) {
		if (sLevel <= Log.ERROR&& !TextUtils.isEmpty(msg)) {
			return Log.e(tag, msg);
		}
		return 0;
	}

	/**
     * Send a ERROR log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public static int e(String tag, String msg, Throwable tr){
		if (sLevel <= Log.ERROR&& !TextUtils.isEmpty(msg)) {
			return Log.e(tag, msg, tr);
		}
		return 0;
	}
	
	/**
     * Send an INFO log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public static int i(String tag, String msg) {
		if (sLevel <= Log.INFO && !TextUtils.isEmpty(msg)) {
			return Log.i(tag, msg);
		}
		return 0;
	}
	
    /**
     * Send a VERBOSE log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public static int v(String tag, String msg) {
		if (sLevel <= Log.VERBOSE&& !TextUtils.isEmpty(msg)) {
			return Log.v(tag, msg);
		}
		return 0;
	}
	
    /**
     * Send a WARN log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public static int w(String tag, String msg, Throwable tr){
		if (sLevel <= Log.WARN&& !TextUtils.isEmpty(msg)) {
			return Log.w(tag, msg, tr);
		}
		return 0;
	}

    /**
     * Send a WARN log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public static int w(String tag, String msg) {
		if (sLevel <= Log.WARN&& !TextUtils.isEmpty(msg)) {
			return Log.w(tag, msg);
		}
		return 0;
	}

    /**
     * Send a WARN log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log
     */
	public static int w(String tag, Throwable e) {
		if (sLevel <= Log.WARN) {
			return Log.w(tag, e);
		}
		return 0;
	}

}

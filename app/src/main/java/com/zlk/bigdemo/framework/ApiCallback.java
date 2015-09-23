package com.zlk.bigdemo.framework;


/**
 * API调用回调
 * @author dingjian
 *
 * @param <T>
 */
public interface ApiCallback<T>{
	/**
	 * 接口调用数据
	 * 
	 * @param data	数据
	 */
	public abstract void onDataReceived(T data);
	
	/**
	 * 接口调用异常
	 * 
	 * @param id		
	 * @param progress	进度1-100
	 */
	public abstract void onProgress(Object id, int progress);
	
	/**
	 * 接口调用异常
	 * 
	 * @param code		异常码
	 * @param reason	异常原因
	 */
	public abstract void onException(int code, String reason);

}

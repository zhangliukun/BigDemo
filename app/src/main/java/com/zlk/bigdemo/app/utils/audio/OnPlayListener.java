package com.zlk.bigdemo.app.utils.audio;

/**
 * 播放状态监听
 * @author bokui
 *
 */
public interface OnPlayListener {
	/**
	 * 开始播放
	 */
	public static final int PLAY_START = 0x01;
	
	/**
	 * 播放已停止
	 */
	public static final int PLAY_STOPED = 0x02;
	
	/**
	 * 播放已暂停
	 */
	public static final int PLAY_PAUSED = 0x03;
	
	/**
	 * 播放已完成
	 */
	public static final int PLAY_COMPLEMENTED = 0x04;
	
	
    public static final int NO_ERROR = 0;
    /**
     * IO访问时出现的错误
     */
    public static final int IO_ACCESS_ERROR = 1;
    /**
     * 内部错误
     */
    public static final int INTERNAL_ERROR = 2;
    /**
     * 播放声音时的错误
     */
    public static final int IN_CALL_RECORD_ERROR = 3;
    /**
     * 下载声音时的错误
     */
    public static final int DOWNLOAD_RECORD_ERROR = 4;
	
	
	/**
	 * 播放状态监听
	 * @param url		播放url
	 * @param state		状态，{@link #PLAY_START PLAY_START},{@link #PLAY_PAUSED PLAY_PAUSED},
	 * {@link #PLAY_STOPED PLAY_STOPED},{@link #PLAY_COMPLEMENTEDPLAY_COMPLEMENTED}状态之一
	 */
	public void onPlayStateListener(String url, int state);
	
	/**
	 * 播放进度监听
	 * @param url		播放url
	 * @param progress	播放进度，播放进度从0-100。
	 */
	public void onProgressListener(String url, int progress);
	
	/**
	 * 播放错误监听，如果出现错误不会进入停止、完成状态
	 * @param url
	 * @param error
	 */
	public void onPlayErrorListener(String url, int error);
}

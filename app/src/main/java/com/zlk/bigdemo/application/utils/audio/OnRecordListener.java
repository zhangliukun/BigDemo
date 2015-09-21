package com.zlk.bigdemo.application.utils.audio;

/**
 * 录音状态监听
 * @author zengxinxin
 *
 */
public interface OnRecordListener {
	/**
	 * 录音开始
	 */
	public static final int RECORD_START = 0x01;
	
	/**
	 * 录音完成
	 */
	public static final int RECORD_COMPLEMENTED = 0x02;	
	
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
     * 录音时的错误
     */
    public static final int IN_RECORD_ERROR = 3;
	
	
	/**
	 * 录音状态监听
	 * @param state		状态，{@link #RECORD_START RECORD_START},{@link #RECORD_COMPLEMENTED RECORD_COMPLEMENTED}状态之一
	 */
	public void onRecordStateListener(int state);

	/**
	 * 录音时间监听 单位为s
	 * @param recordTime 录音时间
	 */
	public void onRecordProgressListener(int recordTime);

	/**
	 * 录音错误监听，如果出现错误不会进入停止状态
	 * @param error
	 */
	public void onRecordErrorListener(int error);

}

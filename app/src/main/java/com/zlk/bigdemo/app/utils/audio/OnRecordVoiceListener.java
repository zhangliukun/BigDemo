package com.zlk.bigdemo.app.utils.audio;

/**
 * 录音音量监听接口
 * Created by Yetwish on 2015/9/19.
 */
public interface OnRecordVoiceListener {

    public static final int MAX_AMPLITUDE = 32768;

    void onRecordVoice(int voiceLevel);
}

package com.zlk.bigdemo.freeza.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zlk.bigdemo.R;
import com.zlk.bigdemo.freeza.util.audio.AudioManagers;
import com.zlk.bigdemo.freeza.util.audio.OnPlayListener;
import com.zlk.bigdemo.freeza.util.audio.OnRecordListener;
import com.zlk.bigdemo.freeza.util.audio.OnRecordVoiceListener;

/**
 * Created by zale on 2015/11/13.
 */
public class FullScreenRecordView extends RelativeLayout{


    private Context mContext;
    private Handler handler;

    private AudioManagers audioManagers;

    /**
     * 本体view
     */
    private View fullScreenView;

    /**
     * 控制物体图片
     */
    private ImageView itemIV;

    /**
     * 计时textview
     * 计时时长count
     */
    private TextView recordTimeTV;
    private int recordTimeCount;

    /**
     * 录音条控件
     */
    private VoiceView recordVoiceView;

    /**
     * item的高宽
     */
    private int itemHeight;
    private int itemWidth;

    /**
     * 初始的触摸的xy
     */
    private int initialX;
    private int initialY;

    /**
     * 在绘画是是否已经获取到了item的高宽
     */
    private boolean isGetXY = false;

    /**
     * 延迟出现录音的时间
     */
    private int millisecond = 2000;


    /**
     * 录音路径
     */
    private String recordPath;

    /**
     * 回调接口
     */
    private onRecordStatusListener mRecordStatusListener;

    public FullScreenRecordView(Context context) {
        this(context, null);
    }

    public FullScreenRecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init(){

        fullScreenView = LayoutInflater.from(mContext).inflate(R.layout.layout_fullscreen_record_view, this);
        itemIV = (ImageView) fullScreenView.findViewById(R.id.image_item);
        recordTimeTV = (TextView) fullScreenView.findViewById(R.id.record_time);
        recordVoiceView = (VoiceView) fullScreenView.findViewById(R.id.voiceview);


        handler = new Handler();
        audioManagers = AudioManagers.getInstance();

        recordVoiceView.setMode(VoiceView.MODE_LEFT);
        recordVoiceView.setVisibility(View.VISIBLE);

        ViewTreeObserver observer = itemIV.getViewTreeObserver();
        //等到item被画出来以后才能真正获取到高宽
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isGetXY){
                    itemWidth = itemIV.getHeight();
                    itemHeight = itemIV.getWidth();
                    setItemLocation(initialX,initialY);
                    setItemVisible(true);
                    Log.i("addOnGlobal","width:"+itemWidth +" height"+itemHeight);
                    if (itemHeight!=0&&itemWidth!=0){
                        isGetXY = true;
                    }
                }
            }
        });
    }

    /**
     * 获取音量条大小的
     */
    OnRecordVoiceListener recordVoiceListener = new OnRecordVoiceListener() {
        @Override
        public void onRecordVoice(int voiceLevel) {
            recordVoiceView.addVoice(voiceLevel);
            Log.i("recordView",voiceLevel+"");
        }
    };

    /**
     * 录音回调接口
     */
    OnRecordListener recordListener = new OnRecordListener() {
        @Override
        public void onRecordStateListener(int state) {
            switch (state){
                case RECORD_START:
                    handler.postDelayed(recordTimeRunnable,1000);
                    Log.i("recordListener","start to record");
                    break;
                case RECORD_COMPLEMENTED:
                    Log.i("recordListener","record complete");
                    break;
                case IN_RECORD_ERROR:
                    Log.i("recordListener","record error");
                    break;
                default:
                    break;

            }
        }

        @Override
        public void onRecordProgressListener(int recordTime) {

        }

        @Override
        public void onRecordErrorListener(int error) {

        }
    };


//    /**
//     * 播放语音的回调
//     */
//    private OnPlayListener recordPlayListener = new OnPlayListener() {
//        @Override
//        public void onPlayStateListener(String url, int state) {
//
//        }
//
//        @Override
//        public void onProgressListener(String url, int progress) {
//
//        }
//
//        @Override
//        public void onPlayErrorListener(String url, int error) {
//
//        }
//    };

    /**
     * 回调方法
     * @param mRecordStatusListener
     */
    public void setmRecordStatusListener(onRecordStatusListener mRecordStatusListener){
        this.mRecordStatusListener = mRecordStatusListener;
    }

    /**
     * 设置物体是否可见
     * @param isVisible
     */
    private void setItemVisible(boolean isVisible){
        if (isVisible){
            itemIV.setVisibility(View.VISIBLE);
        }else {
            itemIV.setVisibility(View.GONE);
        }
    }

    /**
     * 设置是否全屏
     * @param isFullScreen
     */
    public void setIsFullScreen(boolean isFullScreen){
        if (isFullScreen) {
            WindowManager.LayoutParams params = ((Activity)mContext).getWindow().getAttributes();
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            ((Activity)mContext).getWindow().setAttributes(params);
            ((Activity)mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams params = ((Activity)mContext).getWindow().getAttributes();
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ((Activity)mContext).getWindow().setAttributes(params);
            ((Activity)mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                setIsFullScreen(true);
//                Log.i("down direction","x:"+event.getX()+" y:"+event.getY());
//                mRecordStatusListener.startRecord();
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                Log.i("move direction","x:"+event.getX()+" y:"+event.getY());
//                break;
//
//            case MotionEvent.ACTION_UP:
//                setIsFullScreen(false);
//                mRecordStatusListener.endRecord();
//                Log.i("up direction","x:"+event.getX()+" y:"+event.getY());
//                break;
//
//        }
//        return true;
//        //return super.onTouchEvent(event);
//    }


    /**
     * 处理touch事件
     * @param event
     */
    public void getOnTouchEvent(final MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i("down direction", "x:" + event.getX() + " y:" + event.getY());
                handler.postDelayed(startRecordRunnable, millisecond);
                initialX = (int) event.getX();
                initialY = (int) event.getY();
                setItemLocation(initialX,initialY);
                break;

            case MotionEvent.ACTION_MOVE:
                Log.i("move direction", "x:" + event.getX() + " y:" + event.getY());
                setItemLocation((int) event.getX(), (int) event.getY());
                break;

            case MotionEvent.ACTION_UP:
                //audioManagers.play(recordPath,recordPlayListener);
                audioManagers.stopRecord();
                resetState();
                processRecord((int)event.getX(),(int)event.getY());
                mRecordStatusListener.endRecord(recordPath,recordTimeCount);
                Log.i("up direction","x:"+event.getX()+" y:"+event.getY());
                break;
        }
    }

    /**
     * 重置所有的状态
     */
    private void resetState() {
        recordTimeCount = 0;
        recordTimeTV.setText("00:00");
        recordPath="";
        setIsFullScreen(false);
        recordVoiceView.clear();
        handler.removeCallbacks(startRecordRunnable);
        handler.removeCallbacks(recordTimeRunnable);
    }


    /**
     * 根据离开时候的坐标来判断是否需要发送
     * 根据录音时间的长短来判断是否需要发送
     * @param upX
     * @param upY
     */
    private void processRecord(int upX,int upY){
        Log.i("Window size", "getHeight:" + getHeight() + " getWidth:" + getWidth());
    }


    /**
     * 延迟启动录音
     */
    private Runnable startRecordRunnable = new Runnable() {
        @Override
        public void run() {
            mRecordStatusListener.startRecord();
            setIsFullScreen(true);
            recordPath = audioManagers.record(recordListener,recordVoiceListener);
        }
    };

    /**
     * 更新时间的runnable
     */
    private Runnable recordTimeRunnable = new Runnable() {
        @Override
        public void run() {
            recordTimeCount++;
            updateRecordTV();
            handler.postDelayed(recordTimeRunnable,1000);
        }
    };

    /**
     * UI显示时间的格式
     */
    private void updateRecordTV(){
        if (recordTimeCount<=9){
            recordTimeTV.setText("00:0"+recordTimeCount);
        }else {
            recordTimeTV.setText("00:"+recordTimeCount);
        }
    }

    /**
     * 设置物体的位置
     * @param x
     * @param y
     */
    public void setItemLocation(int x,int y){
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) itemIV.getLayoutParams();
        layoutParams.leftMargin = x - itemWidth/2;
        layoutParams.topMargin = y - itemHeight/2;
        //Log.i("width and height","width:"+itemWidth+" height:"+itemHeight);
        itemIV.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    public static interface onRecordStatusListener {
        public void startRecord();
        public void endRecord(String recordPath,int recordTime);
    }

}

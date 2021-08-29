package com.example.myapplication_tv2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.myapplication_tv2.util.TimeUtil;

public class MediaPlayerTestActivity extends Activity implements SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnVideoSizeChangedListener,
        SeekBar.OnSeekBarChangeListener
{

    private ImageView playOrPauseIv;
    private SeekBar mSeekBar;
    private String path;
    private RelativeLayout rootViewRl;
    private LinearLayout controlLl;
    private TextView startTime, endTime;
    private ImageView forwardButton, backwardButton;
    private boolean isShow = false;         //进度条是否显示

    private SurfaceView videoSuf;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mPlayer;
    AssetFileDescriptor fileDescriptor;

    public static final int UPDATE_TIME = 0x0001;
    public static final int HIDE_CONTROL = 0x0002;

    String urltuzi = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    String urlxibu = "https://vfx.mtime.cn/Video/2019/03/21/mp4/190321153853126488.mp4";

    private final Handler mHandlerUpdateTime = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TIME:
                    updateTime();
                    mHandlerUpdateTime.sendEmptyMessageDelayed(UPDATE_TIME, 500);     //不断往mHandler 发送更新时间的信息
                    return true;
            }
            return  false;
        }
    }) ;
    private final Handler mHandlerHideControl = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HIDE_CONTROL:
                    hideControl();
                    return true;
            }
            return  false;
        }
    }) ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player_test);
        initViews();
        initData();
        initSurfaceView();
        initPlayer();
        initEvent();
    }

    @Override
    protected void onStop() {
        //mPlayer.stop();
        mPlayer.release();
        super.onStop();
    }

    private void initViews() {
        playOrPauseIv = (ImageView) findViewById(R.id.playOrPause);
        startTime = (TextView) findViewById(R.id.tv_start_time);
        endTime = (TextView) findViewById(R.id.tv_end_time);
        mSeekBar = (SeekBar) findViewById(R.id.tv_progess);
        rootViewRl = (RelativeLayout) findViewById(R.id.root_rl);
        controlLl = (LinearLayout) findViewById(R.id.control_ll);       //进度条所在视图
        forwardButton = (ImageView) findViewById(R.id.tv_forward);
        backwardButton = (ImageView) findViewById(R.id.tv_backward);
    }
    private void initData() {
        //设置视频路径
        path = Environment.getExternalStorageDirectory().getPath() + "/20180730.mp4";//这里写上你的视频地址
        //本Project的 资源
        fileDescriptor = getResources().openRawResourceFd(R.raw.test);

        Log.i("视频大小: " , "" + fileDescriptor.getLength()/1024/1024);
    }
    private void initSurfaceView() {
        videoSuf = (SurfaceView) findViewById(R.id.surfaceView);
        videoSuf.setZOrderOnTop(false);
        //videoSuf.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        videoSuf.getHolder().addCallback(this);
    }
    private void initPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnInfoListener(this);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnSeekCompleteListener(this);
        mPlayer.setOnVideoSizeChangedListener(this);

    }
    private void initEvent() {
        /*playOrPauseIv.setOnClickListener(this);
        rootViewRl.setOnClickListener(this);
        //rootViewRl.setOnTouchListener((View.OnTouchListener) this);
        forwardButton.setOnClickListener(this);          //前进10s
        backwardButton.setOnClickListener(this);        //后退10s */
        mSeekBar.setOnSeekBarChangeListener(this);      //当进度条被滚动时调用
    }

    //------------------------------------三个Surface相关----------------------------------------------------
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            //使用手机本地视频
            mPlayer.reset();
            mPlayer.setLooping(true);
            mPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            //mPlayer.setDataSource(this, Uri.parse(urltuzi));

            mPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setDisplay(holder);

        Log.i("总时长",mPlayer.getDuration()+"");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    @Override
    public void onPrepared(MediaPlayer mp) {        // 当MediaPlayer准备好了
        startTime.setText(TimeUtil.getTimeFromMillisecond((long) mp.getCurrentPosition()));
        endTime.setText(TimeUtil.getTimeFromMillisecond((long) mp.getDuration()));
        mSeekBar.setMax(mp.getDuration());
        mSeekBar.setProgress(mp.getCurrentPosition());
        play();
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        //play();
        //mp.seekTo(0);
    }
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }
    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    private void play() {               // 播放则
        if (mPlayer == null) {
            return;
        }
        //Log.i("playPath", path);
        if (mPlayer.isPlaying()) {      //正在播放
            isShow = false;
            mPlayer.pause();            //则暂停
            mHandlerUpdateTime.removeMessages(UPDATE_TIME);                               //停止更新 时间
            mHandlerHideControl.removeMessages(HIDE_CONTROL);                              //取消 隐藏 进度条
            playOrPauseIv.setVisibility(View.VISIBLE);
            playOrPauseIv.setImageResource(android.R.drawable.ic_media_play);
        } else {
            isShow = true;
            mPlayer.start();            //否则就 播放
            mHandlerUpdateTime.sendEmptyMessageDelayed(UPDATE_TIME, 500);           //更新时间
            mHandlerHideControl.sendEmptyMessageDelayed(HIDE_CONTROL, 5000);       //5s后隐藏进度条
            playOrPauseIv.setVisibility(View.INVISIBLE);
            playOrPauseIv.setImageResource(android.R.drawable.ic_media_pause);
        }
    }
    @Override
    public void onSeekComplete(MediaPlayer mp) {
        //TODO
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

    }
    @SuppressLint("NonConstantResourceId")
    //@Override
    /*public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_backward:
                backWard();
                break;
            case R.id.tv_forward:
                forWard();
                break;
            case R.id.playOrPause:
                play();
                break;
            case R.id.root_rl:
                showControl();      //点一下屏幕  没有显示就显示进度条
                break;
        }
    }*/
    /**
     * 更新播放时间文本 与 进度条位置，
     * 每隔0.5秒就更新一次时间
     */
    private void updateTime() {
        startTime.setText(TimeUtil.getTimeFromMillisecond((long) mPlayer.getCurrentPosition()));
        mSeekBar.setProgress(mPlayer.getCurrentPosition());
    }

    /**
     * 隐藏进度条
     */
    private void hideControl() {
        isShow = false;
        if(!mPlayer.isPlaying()){mHandlerUpdateTime.removeMessages(UPDATE_TIME); }     //不用更新时间了
        //controlLl.animate().setDuration(300).translationY(controlLl.getHeight());       //下移 视图高度的量 移出界面
        controlLl.setVisibility(View.INVISIBLE);
    }
    /**
     * 显示进度条
     */
    private void showControl() {        // 按ok键时 执行
        /*if (isShow) {           //2 已经显示
            play();             //3 切换视频状态
        }*/
        play();

        isShow = true;          //1  设置已经显示进度条
        mHandlerHideControl.removeMessages(HIDE_CONTROL);      // 暂时不隐藏了 先显示  判断是否在播放，5s后还是要消失的
        //controlLl.animate().setDuration(300).translationY(0);       // 显示进度条
        controlLl.setVisibility(View.VISIBLE);
        mHandlerUpdateTime.sendEmptyMessage(UPDATE_TIME);     // 更新时间条

        if(mPlayer.isPlaying()){ mHandlerHideControl.sendEmptyMessageDelayed(HIDE_CONTROL, 5000); }    // 若正在播放 5s后隐藏时间条

    }
    void showProgress() {       //仅仅当快进 后退时调用 显示进度条
        isShow = true;
        mHandlerHideControl.removeMessages(HIDE_CONTROL);
        //controlLl.animate().setDuration(300).translationY(0);
        controlLl.setVisibility(View.VISIBLE);
        if(mPlayer.isPlaying()){ mHandlerHideControl.sendEmptyMessageDelayed(HIDE_CONTROL, 5000); }
    }
    /**
     * 设置快进10秒方法
     */
    private void forWard(){
        showProgress();
        if(mPlayer != null){
            int position = mPlayer.getCurrentPosition();
            mPlayer.seekTo(position + 5000);
        }
    }

    /**
     * 设置快退5秒的方法
     */
    public void backWard(){
        showProgress();
        if(mPlayer != null){
            int position = mPlayer.getCurrentPosition();
            if(position > 5000){       //可以倒退10秒
                position-=5000;
            }else{                      //不能倒退十秒 直接到头
                position = 0;
            }
            mPlayer.seekTo(position);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()){
            case KeyEvent.KEYCODE_DPAD_CENTER:
                showControl();
                return true;        //消耗了
            case KeyEvent.KEYCODE_DPAD_LEFT:
                backWard();
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                forWard();
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                showProgress();
                return true;
            case KeyEvent.KEYCODE_BACK:
                if(isShow) {
                    mHandlerHideControl.sendEmptyMessageDelayed(HIDE_CONTROL, 0);
                    return true;
                }
        }

        return super.onKeyDown(keyCode, event);
    }

    //OnSeekBarChangeListener
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        if(mPlayer != null && b){
            //mPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


}

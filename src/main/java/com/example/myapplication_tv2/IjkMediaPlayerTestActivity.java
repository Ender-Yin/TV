package com.example.myapplication_tv2;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.myapplication_tv2.ijk.application.Settings;
import com.example.myapplication_tv2.ijk.media.IjkVideoView;
import com.example.myapplication_tv2.util.TimeUtil;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class IjkMediaPlayerTestActivity extends Activity implements
        IMediaPlayer.OnPreparedListener,
        SeekBar.OnSeekBarChangeListener
{

    private ImageView playOrPauseIv;
    private SeekBar mSeekBar;
    private String path;
    private RelativeLayout rootViewRl;
    private LinearLayout controlLl;
    private TextView startTime, endTime;
    private boolean isShow = false;         //进度条是否显示

    private SurfaceView videoSuf;
    private SurfaceHolder surfaceHolder;

    AssetFileDescriptor fileDescriptor;

    public static final int UPDATE_TIME = 0x0001;
    public static final int HIDE_CONTROL = 0x0002;

    //---------------ijk-------------------
    IjkVideoView ijkVideoView;
    String url1 = "http://mirror.aarnet.edu.au/pub/TED-talks/911Mothers_2010W-480p.mp4";
    String urltuzi = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    String urlxibu = "https://vfx.mtime.cn/Video/2019/03/21/mp4/190321153853126488.mp4";
    //

    //---------------Handler---------------
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
        setContentView(R.layout.activity_ijk_media_player_test);

        initViews();
        initData();
        initPlayer();
        initHudView();
        initEvent();

        //----------------------
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String key = this.getString(R.string.pref_key_player);       // key值名字 保存在String资源中
        String value = mSharedPreferences.getString(key, "没找到preference");       // 根据key值 在本地用户偏好设置 中查找当前偏好播放器
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, "");      //切换到原生player
        editor.apply();

        Log.e("偏好设置-使用哪个MediaPlayer: ", value);
        Log.e("偏好设置-使用哪个MediaPlayer: ", ijkVideoView.getSetting().getPlayer() + "");
        //Log.i("视频类: ", ijkVideoView.getmMediaPlayer().getClass().toString() + "");

    }
    @Override
    protected void onStop() {
        ijkVideoView.pause();
        ijkVideoView.release(true);
        super.onStop();
    }

    private void initViews() {
        playOrPauseIv = (ImageView) findViewById(R.id.playOrPause2);
        startTime = (TextView) findViewById(R.id.tv_start_time2);
        endTime = (TextView) findViewById(R.id.tv_end_time2);
        mSeekBar = (SeekBar) findViewById(R.id.tv_progess2);
        rootViewRl = (RelativeLayout) findViewById(R.id.root_rl2);
        controlLl = (LinearLayout) findViewById(R.id.control_ll2);       //进度条所在视图
        ImageView forwardButton = (ImageView) findViewById(R.id.tv_forward2);
        ImageView backwardButton = (ImageView) findViewById(R.id.tv_backward2);
    }
    private void initData() {
        //设置视频路径
        //path = Environment.getExternalStorageDirectory().getPath() + "/20180730.mp4";//这里写上你的视频地址
        //本Project的 项目资源
        fileDescriptor = getResources().openRawResourceFd(R.raw.test);
        Log.i("视频大小: " , "" + fileDescriptor.getLength()/1024/1024);
    }
    private void initHudView() {
        //---------视频各种参数的视图
        TableLayout mHudView = (TableLayout) findViewById(R.id.hud_view);
        ijkVideoView.setHudView(mHudView);
        //mHudView.setVisibility(View.GONE);
    }
    private void initPlayer() {
        ijkVideoView = findViewById(R.id.ijk_video_view);
        ijkVideoView.setOnPreparedListener(this);

        ijkVideoView.setVideoURI(Uri.parse(url1));
        //ijkVideoView.setAspectRatio(IRenderView.AR_4_3_FIT_PARENT);
    }
    //IMediaPlayer的接口方法重写
    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        //设置 进度条的 视频总时长
        startTime.setText(TimeUtil.getTimeFromMillisecond((long) ijkVideoView.getCurrentPosition()));
        endTime.setText(TimeUtil.getTimeFromMillisecond((long) ijkVideoView.getDuration()));
        mSeekBar.setMax(ijkVideoView.getDuration());
        mSeekBar.setProgress(ijkVideoView.getCurrentPosition());
        //playOrStop();
        ijkVideoView.start();
    }
    private void initEvent() {
        Button btn1 = findViewById(R.id.btn1_changeurl);
        Button btn2 = findViewById(R.id.btn2_changeurl);
        Button btn3 = findViewById(R.id.btn_playagain);
        Button btn4 = findViewById(R.id.btn_AndroidPlayer);
        btn1.setOnClickListener(v -> {
            ijkVideoView.stopPlayback();
            ijkVideoView.initRenders();      //创建surface view , 同时设置surfaceHolder.callback监听
            ijkVideoView.setVideoOnlyURI(Uri.parse(urltuzi));       //设置url
            ijkVideoView.start();

        });
        btn2.setOnClickListener(v -> {
            ijkVideoView.stopPlayback();
            ijkVideoView.initRenders();      //套娃设置
            ijkVideoView.setVideoURI(Uri.parse(url1));
            ijkVideoView.start();
        });
        btn3.setOnClickListener(v -> {
            playOrStop();
        });
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String key = this.getString(R.string.pref_key_player);       // key值名字 保存在String资源中
        String value = mSharedPreferences.getString(key, "没找到preference");       // 根据key值 在本地用户偏好设置 中查找当前偏好播放器
        btn4.setOnClickListener(v -> {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(key, String.valueOf(Settings.PV_PLAYER__AndroidMediaPlayer));      //切换到原生player
            editor.apply();

            ijkVideoView.stopPlayback();
            ijkVideoView.initRenders();      //套娃设置
            ijkVideoView.setVideoURI(Uri.parse(urltuzi));      //且播放兔子
            ijkVideoView.start();
            Log.e("视频mediaPlayer 是哪个: ", ijkVideoView.getmMediaPlayer().getClass().toString() + "");
        });

        btn1.requestFocus();
        mSeekBar.setOnSeekBarChangeListener(this);      //当进度条被滚动时调用
    }

    //------------------------------------三个Surface相关----------------------------------------------------
    //-----------------------------------MediaPlayer相关---------------------------------------------------
    private void playOrStop() {               // 播放则
        if (ijkVideoView == null) {
            return;
        }
        //Log.i("playPath", path);
        if (ijkVideoView.isPlaying()) {      //正在播放
            isShow = false;
            ijkVideoView.pause();            //则暂停
            mHandlerUpdateTime.removeMessages(UPDATE_TIME);                                 //停止更新 时间
            mHandlerHideControl.removeMessages(HIDE_CONTROL);                              //取消 隐藏 进度条
            playOrPauseIv.setVisibility(View.VISIBLE);
            playOrPauseIv.setImageResource(android.R.drawable.ic_media_play);
        } else {
            isShow = true;
            ijkVideoView.start();            //否则就 播放
            mHandlerUpdateTime.sendEmptyMessageDelayed(UPDATE_TIME, 500);           //更新时间
            mHandlerHideControl.sendEmptyMessageDelayed(HIDE_CONTROL, 5000);       //5s后隐藏进度条
            playOrPauseIv.setVisibility(View.INVISIBLE);
            playOrPauseIv.setImageResource(android.R.drawable.ic_media_pause);
            Log.e("视频mediaPlayer 是哪个: ", ijkVideoView.getmMediaPlayer().getClass().toString() + "");
        }
    }

    /**
     * 更新播放时间文本 与 进度条位置，
     * 每隔0.5秒就更新一次时间
     */
    private void updateTime() {
        startTime.setText(TimeUtil.getTimeFromMillisecond((long) ijkVideoView.getCurrentPosition()));
        mSeekBar.setProgress(ijkVideoView.getCurrentPosition());     // 进度条随着 视频的实际进度 随之改变进度
    }

    /**
     * 隐藏进度条
     */
    private void hideControl() {
        isShow = false;
        if(!ijkVideoView.isPlaying()){mHandlerUpdateTime.removeMessages(UPDATE_TIME); }     //不用更新时间了
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
        playOrStop();

        isShow = true;          //1  设置已经显示进度条
        mHandlerHideControl.removeMessages(HIDE_CONTROL);      // 暂时不隐藏了 先显示  判断是否在播放，5s后还是要消失的
        //controlLl.animate().setDuration(300).translationY(0);       // 显示进度条
        controlLl.setVisibility(View.VISIBLE);
        mHandlerUpdateTime.sendEmptyMessage(UPDATE_TIME);     // 更新时间条

        if(ijkVideoView.isPlaying()){ mHandlerHideControl.sendEmptyMessageDelayed(HIDE_CONTROL, 5000); }    // 若正在播放 5s后隐藏时间条

    }
    void showProgress() {       //仅仅当快进 后退时调用 显示进度条
        isShow = true;
        mHandlerHideControl.removeMessages(HIDE_CONTROL);
        //controlLl.animate().setDuration(300).translationY(0);
        controlLl.setVisibility(View.VISIBLE);
        if(ijkVideoView.isPlaying()){ mHandlerHideControl.sendEmptyMessageDelayed(HIDE_CONTROL, 5000); }    // 5s后隐藏 进度条
    }
    /**
     * 设置快进10秒方法
     */
    private void forWard(){
        showProgress();
        if(ijkVideoView != null){
            int position = ijkVideoView.getCurrentPosition();
            ijkVideoView.seekTo(position + 5000);
        }
    }

    /**
     * 设置快退5秒的方法
     */
    public void backWard(){
        showProgress();
        if(ijkVideoView != null){
            int position = ijkVideoView.getCurrentPosition();
            if(position > 5000){       //可以倒退10秒
                position-=5000;
            }else{                      //不能倒退十秒 直接到头
                position = 0;
            }
            ijkVideoView.seekTo(position);
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
        if(ijkVideoView != null && b){
            //ijkVideoView.seekTo(progress);
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
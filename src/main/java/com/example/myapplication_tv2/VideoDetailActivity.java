package com.example.myapplication_tv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaController2;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;

public class VideoDetailActivity extends Activity implements SurfaceHolder.Callback{
    private MediaController mediaController2;
    private MediaPlayer mPlayer = null;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        TextView textViewTitle = findViewById(R.id.detail_video_title);
        VideoView videoView = findViewById(R.id.videoView);
        MediaController controller = new MediaController(this);

        String url1 = "http://mirror.aarnet.edu.au/pub/TED-talks/911Mothers_2010W-480p.mp4";
        String url2 = "http://cctvalih5ca.v.myalicdn.com/live/cctv1_2/index.m3u8";
        videoView.setVideoURI(Uri.parse(url1));
        //videoView.setVideoURI(Uri.parse("android.resource://com.example/myapplication_tv2/" + R.raw.test));
        /**
         * 将控制器和播放器进行互相关联
         */
        videoView.setMediaController(controller);
        controller.setMediaPlayer(videoView);
        videoView.start();


        //绑定视频标题
        Bundle bundle = this.getIntent().getExtras();
        String videoTitle = bundle.getString("videoTitle");
        textViewTitle.setText(videoTitle);

        //----------------------------------初始化MediaPlayer----------------------------------------
        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        mPlayer = new MediaPlayer();

        findViewById(R.id.btn_start).setOnClickListener(v -> mPlayer.start());
        findViewById(R.id.btn_stop).setOnClickListener(v -> mPlayer.pause());
        findViewById(R.id.btn_getPosition).setOnClickListener(v -> Log.i("现在位置 : ", String.valueOf(mPlayer.getCurrentPosition())) );
        findViewById(R.id.btn_seekTo).setOnClickListener(v -> {
            mPlayer.seekTo(70000);
            Log.i("现在位置 : ", String.valueOf(mPlayer.getCurrentPosition()));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.btn_start).requestFocus();
    }
    public void bindView(){ }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        //mediaController2.setMediaPlayer(mPlayer);
        try {
            //mPlayer.setDataSource(this,Uri.parse("http://cctvalih5ca.v.myalicdn.com/live/cctv1_2/index.m3u8"));
            //添加播放视频的路径与配置MediaPlayer
            AssetFileDescriptor fileDescriptor = getResources().openRawResourceFd(R.raw.test);
            mPlayer.reset();
            mPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            mPlayer.prepare();

            mPlayer.setOnPreparedListener(mp -> mPlayer.start());       //设置 当视频资源已经准备好的 监听

        } catch (IllegalArgumentException e){
            e.printStackTrace();
        } catch (IllegalStateException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //给mMediaPlayer添加预览的SurfaceHolder，将播放器和SurfaceView关联起来
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setDisplay(surfaceHolder);

        Log.i("视频时长 : ", String.valueOf(mPlayer.getDuration()));
        Log.i("现在位置 : ", String.valueOf(mPlayer.getCurrentPosition()));

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        mPlayer.release();
        super.onDestroy();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
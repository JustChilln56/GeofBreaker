package com.example.ponggame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.app.Activity;

public class soundActivity extends Activity {

    MediaPlayer mMediaPlayer;
    SeekBar seekbar;
    AudioManager audioManager;
    TextView textview;
    int length;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(R.drawable.geofbreaker);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        textview = findViewById(R.id.textView1);
        seekbar = findViewById(R.id.seekBar);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        assert audioManager != null;
        seekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));


        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekbar.setProgress(volume);



        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                textview.setText("Volume : " + i);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this,R.raw.song2);
        //I GOT HOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOES
        //mMediaPlayer = MediaPlayer.create(this,R.raw.sheckwes);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

    }

    @Override
    protected void onDestroy() {
        //other codes
        super.onDestroy();
        mMediaPlayer.stop();
    }
    protected void onResume() {
        super.onResume();
        mMediaPlayer.seekTo(length);
        mMediaPlayer.start();

    }
    protected void onPause() {
        super.onPause();
        mMediaPlayer.pause();
        length = mMediaPlayer.getCurrentPosition();
    }
}

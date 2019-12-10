package com.example.ponggame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivityPong extends Activity {

    private PongGame mPongGame;
    private MediaPlayer mMediaPlayer;
    int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.pause();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mPongGame = new PongGame(this, size.x, size.y);

        setContentView(mPongGame);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toast.makeText(this, "Press & hold left half of screen to move paddle left;\n" +
                "right half of screen to move right", Toast.LENGTH_LONG).show();

        mMediaPlayer = MediaPlayer.create(this,R.raw.song1);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayer.seekTo(length);
        mMediaPlayer.start();
        mPongGame.resume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.pause();
        length = mMediaPlayer.getCurrentPosition();
        mPongGame.pause();

    }
    @Override
    protected void onDestroy() {
        //other codes
        super.onDestroy();
        mMediaPlayer.stop();
    }
}

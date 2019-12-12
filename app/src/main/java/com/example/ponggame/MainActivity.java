package com.example.ponggame;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.view.WindowManager;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends Activity {
    MediaPlayer mMediaPlayer;
    ImageView image;
    int length;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //createSignInIntent();

        setContentView(R.layout.activity_main);
        image = findViewById(R.id.imageView);
        image.setImageResource(R.drawable.geofbreaker);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.pause();
        mMediaPlayer = MediaPlayer.create(this,R.raw.str);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        Button soundButton =  findViewById(R.id.sound3);
        Button playButton =   findViewById(R.id.play2);
        Button leaderboardButton = findViewById((R.id.leaderboard));

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity1();

            }
        });
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });



    }

    public void openActivity1() {
        Intent intent = new Intent(MainActivity.this, MainActivityPong.class);
        startActivity(intent);
        mMediaPlayer.pause();
    }
    public void openActivity2() {
        Intent intent = new Intent(MainActivity.this, soundActivity.class);
        startActivity(intent);
        mMediaPlayer.pause();
    }
    public void openActivity3() {
        Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
        startActivity(intent);
        mMediaPlayer.pause();
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

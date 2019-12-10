package com.example.ponggame;

import android.os.Bundle;
import android.app.Activity;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;

public class LeaderboardActivity extends Activity {
    MediaPlayer mMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //createSignInIntent();
        setContentView(R.layout.activity_leaderboard);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this,R.raw.song);
        //I GOT HOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOES
        //mMediaPlayer = MediaPlayer.create(this,R.raw.sheckwes);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("leaderboard");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                initList(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    protected void onDestroy() {
        //other codes
        super.onDestroy();
        mMediaPlayer.stop();
    }
    protected void onResume() {
        super.onResume();
        mMediaPlayer.start();

    }
    protected void onPause() {
        super.onPause();
        mMediaPlayer.pause();
    }

    public void initList(DataSnapshot dataSnapshot) {

        ListView list = findViewById(R.id.leaderlist);

        ArrayList<Leaderboard> listLeaders = new ArrayList<Leaderboard>();


        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Leaderboard ls = ds.getValue(Leaderboard.class);
            listLeaders.add(ls);
        }

        Collections.sort(listLeaders);

        ArrayList<String> stringLeaders = new ArrayList<String>();

        for (Leaderboard l : listLeaders) {
            stringLeaders.add(l.getUser() + " " +l.getScore());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringLeaders);
        list.setAdapter(arrayAdapter);

    }
}

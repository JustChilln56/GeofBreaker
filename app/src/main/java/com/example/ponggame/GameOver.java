package com.example.ponggame;

import android.content.Intent;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class GameOver extends Activity {
    int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        String scr = String.valueOf(PongGame.mScore);
        //String scr = getIntent().getStringExtra("score");
        System.out.println(scr);
        TextView score1 = findViewById(R.id.textView3);
        score1.setText(scr);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("leaderboard");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Leaderboard> listLeaders = new ArrayList<Leaderboard>();

                //count the number stuff in leaderboard;
                int i = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    i++;
                }
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("leaderboard");
                Leaderboard test = new Leaderboard(LoginActivity.user.getEmail(), PongGame.mScore);
                rootRef.child("" + i).setValue(test);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button startOver = findViewById(R.id.startOver);
        Button exit = findViewById(R.id.exit);
        startOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOver.this, MainActivityPong.class);
                startActivity(intent);
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOver.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

}

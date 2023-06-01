package edu.hitsz.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.R;
import edu.hitsz.game.Game;

public class LauncherActivity extends AppCompatActivity {

    Button offlineGame;
    Button onlineGame;

    public static boolean isOnline =false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        offlineGame = findViewById(R.id.offline_btn);
        onlineGame = findViewById(R.id.online_btn);


        //选择单机模式
        offlineGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent offlineIntent = new Intent(LauncherActivity.this, OffLineActivity.class);
                startActivity(offlineIntent);
            }
        });

        //选择联机模式
        onlineGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOnline = true;
                Intent onlineIntent = new Intent(LauncherActivity.this, LogInActivity.class);
                startActivity(onlineIntent);
            }
        });



    }
}

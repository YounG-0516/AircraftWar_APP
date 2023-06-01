package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.R;
import edu.hitsz.game.Game;

public class OffLineActivity extends AppCompatActivity {

    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;
    public static int gameType=0;
    Switch bgm;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        Button medium_btn = findViewById(R.id.medium_btn);
        Button easy_btn = findViewById(R.id.easy_btn);
        Button hard_btn = findViewById(R.id.hard_btn);
        bgm = findViewById(R.id.bgm);

        getScreenHW();

        Intent intent = new Intent(OffLineActivity.this, GameActivity.class);
        medium_btn.setOnClickListener(view -> {
            gameType=1;
            intent.putExtra("gameType",gameType);
            startActivity(intent);
        });

        easy_btn.setOnClickListener(view -> {
            gameType =2;
            intent.putExtra("gameType",gameType);
            startActivity(intent);
        });

        hard_btn.setOnClickListener(view -> {
            gameType =3;
            intent.putExtra("gameType",gameType);
            startActivity(intent);
        });

        bgm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(bgm.isChecked()){
                    Game.needMusic=true;
                }else{
                    Game.needMusic=false;
                }
            }
        });
    }

    public void getScreenHW(){
        //定义DisplayMetrics 对象
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口的宽度
        WINDOW_WIDTH = dm.widthPixels;
        //窗口高度
        WINDOW_HEIGHT = dm.heightPixels;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

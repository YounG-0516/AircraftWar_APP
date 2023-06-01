package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import edu.hitsz.R;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;
    public static final String IP = "10.250.96.130";
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        startButton = findViewById(R.id.start_btn);

        getScreenHW();

        startButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LauncherActivity.class);
            startActivity(intent);
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

        Log.i(TAG, "screenWidth : " + WINDOW_WIDTH + " screenHeight : " + WINDOW_HEIGHT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
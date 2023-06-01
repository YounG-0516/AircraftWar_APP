package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

import edu.hitsz.DAO.User;
import edu.hitsz.game.Game;
import edu.hitsz.game.EasyGame;
import edu.hitsz.game.HardGame;
import edu.hitsz.game.MediumGame;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";

    private int gameType=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d(TAG,"handleMessage");
                if (msg.what == 1) {
                    if(!LauncherActivity.isOnline){
                        Intent intent = new Intent(GameActivity.this,RankingActivity.class);

                        User user = (User)msg.obj;
                        //intent.putExtra("user_name", user.getName());
                        intent.putExtra("user_score", user.getScore());
                        intent.putExtra("user_time", user.getTime());

                        Toast.makeText(GameActivity.this,"GameOver",Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
            }
        };

        if(getIntent() != null){
            gameType = getIntent().getIntExtra("gameType",1);
        }
        Game basGameView = null;
        if(gameType == 1){
            try {
                basGameView = new MediumGame(this,handler);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else if(gameType == 3){
            try {
                basGameView = new HardGame(this,handler);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            try {
                basGameView = new EasyGame(this,handler);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        setContentView(basGameView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
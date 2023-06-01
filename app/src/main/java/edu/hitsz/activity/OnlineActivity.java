package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import edu.hitsz.R;
import edu.hitsz.game.Game;
import edu.hitsz.game.HardGame;
import edu.hitsz.game.MediumGame;

public class OnlineActivity extends AppCompatActivity {

    private static final String TAG = "OnlineActivity";

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    Handler handler;

    Game game;
    private static int opponentScore = 0;
    private static boolean gameOverFlag = false;
    private static String opName;
    private static String myName;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        //用于发送接收到的服务器端的消息，显示在界面上
        handler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                //启动游戏
                if (msg.what == 0x123 && msg.obj.equals("start")) {

                    try {
                        game = new MediumGame(OnlineActivity.this, handler);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    setContentView(game);

                    // 如果开启游戏，那么就新开一个线程给服务端发送当前分数
                    // 如果当前玩家已经死亡，那么就给服务器传"end"信息
                    new Thread(() -> {

                        //发送当前分数
                        while (!game.isGameOverFlag()) {

                            writer.println(Game.score);
                            Log.i(TAG,"send to server: score "+Game.score);
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        //死亡后发送结束标志
                        writer.println("end");
                        Log.i(TAG,"send to server: end");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setContentView(R.layout.activity_end);
                            }
                        });

                    }).start();

                } else if (msg.what == 0x123 && msg.obj.equals("gameover")) {
                    //设置标志：双方游戏全部结束
                    setGameOverFlag(true);
                    Log.i(TAG,"游戏结束");

                    Intent intent = new Intent(OnlineActivity.this, OverActivity.class);
                    intent.putExtra("myScore",Game.score);
                    intent.putExtra("otherScore",opponentScore);
                    intent.putExtra("opName",opName);
                    intent.putExtra("myName",myName);
                    startActivity(intent);
                    Log.i(TAG,"跳转");

                } else if (msg.what == 0x456) {
                    myName = (String) msg.obj;
                    Log.e(TAG, "myname: " + myName);
                } else if (msg.what == 0x789){
                    opName = (String) msg.obj;
                    Log.e(TAG, "opname: "+ opName);
                } else {

                    try {
                        if ((String)msg.obj != null){
                            opponentScore = Integer.parseInt((String)msg.obj);
                        }
                    } catch (NumberFormatException e) {

                    }

                }
            }
        };

        new Thread(new ClientThread(handler)).start();

    }


    class ClientThread implements Runnable{
        private Handler handler;    //向客户端的UI发送消息

        public ClientThread(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {

            try{
                //连接到服务器
                socket = new Socket();
                socket.connect(new InetSocketAddress(MainActivity.IP, 8888), 5000);
                writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream(), StandardCharsets.UTF_8)), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

                //创建子线程接收服务端信息
                //服务器端可能回复的消息："start"/"end"/不发送，此时显示对手名称及分数
                new Thread(() -> {
                    String msg;
                    int count=0;
                    try{
                        while ((msg = reader.readLine()) != null){
                            Log.e(TAG, "get from server: "+msg);

                            if(count==1){
                                Message msg2 = new Message();
                                msg2.what = 0x789;
                                msg2.obj = msg;
                                handler.sendMessage(msg2);
                                Log.e(TAG, "send opname:"+msg);
                                count++;
                            }

                            if(count==0){
                                Message msg1 = new Message();
                                msg1.what = 0x456;
                                msg1.obj = msg;
                                handler.sendMessage(msg1);
                                Log.e(TAG, "send myname:"+msg);
                                count++;
                            }

                            Message msgFromServer = new Message();
                            msgFromServer.what = 0x123;
                            msgFromServer.obj = msg;
                            handler.sendMessage(msgFromServer);
                        }
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static int getOpponentScore() {
        return opponentScore;
    }

    public static String getOpponentName() {
        return opName;
    }

    private void setGameOverFlag(boolean gameOverFlag) {
        OnlineActivity.gameOverFlag = gameOverFlag;
    }

    public static boolean isGameOverFlag() {
        return gameOverFlag;
    }

}


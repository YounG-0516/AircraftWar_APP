package edu.hitsz.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import edu.hitsz.R;

public class LogInActivity extends AppCompatActivity {

    private final static String TAG = "LogInActivity";

    Button loginButton;
    Button registerButton;
    EditText login_ID;
    EditText login_password;

    private String user_ID;
    private String user_password;

    private PrintWriter out;
    private Socket socket;
    public Handler handler;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_in);

        loginButton = findViewById(R.id.login_in_btn);
        registerButton = findViewById(R.id.register_btn);
        login_ID = findViewById(R.id.login_ID);
        login_password = findViewById(R.id.login_password);

        login_ID.setTextColor(Color.WHITE);
        login_password.setTextColor(Color.WHITE);

        //注册按钮
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //登录按钮
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.login_in_btn) {
                    user_ID = login_ID.getText().toString();
                    user_password = login_password.getText().toString();
                    new Thread(new Connect()).start();
                }
            }
        });

        //Looper.prepare();
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what == 0x11){
                    Intent startGame = new Intent(LogInActivity.this, OnlineActivity.class);
                    Toast.makeText(LogInActivity.this, "登陆成功",Toast.LENGTH_SHORT).show();
                    startActivity(startGame);
                }else if(msg.what == 0x22){
                    Toast.makeText(LogInActivity.this, "用户名或密码错误",Toast.LENGTH_SHORT).show();
                }else if(msg.what == 0x33){
                    Toast.makeText(LogInActivity.this, "该用户不存在",Toast.LENGTH_SHORT).show();
                }
            }
        };
        //Looper.loop();
    }


    public class Connect implements Runnable {

        private BufferedReader in;

        @Override
        public void run() {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(MainActivity.IP, 9999), 5000);

                /**
                 * 向服务器发送请求码
                 */
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

                /**
                 * 客户端组装 JSON对象
                 */
                JSONObject jsonObject = new JSONObject();
                //发送操作码
                jsonObject.put("operation", "login");
                jsonObject.put("ID",user_ID);
                jsonObject.put("PSW",user_password);
                Log.e(TAG, jsonObject.toString());
                // 发送 JSON 对象给服务器
                out.println(jsonObject.toString());

                /**
                 * 判断从服务器端返回的信息
                 */
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = in.readLine(); // 读取另一头传过来的数据
                System.out.println(line+"45678");

                if(Objects.equals(line, "login_success")){
                    //Intent startGame = new Intent(LogInActivity.this, GameActivity.class);
                    //startActivity(startGame);
                    Message message = Message.obtain();
                    message.what = 0x11 ;
                    message.obj = "login_success";
                    handler.sendMessage(message);
                }else if(Objects.equals(line, "password_failed")){
                    //Toast.makeText(LogInActivity.this, "用户名或密码错误",Toast.LENGTH_SHORT).show();
                    Message message = Message.obtain();
                    message.what = 0x22 ;
                    message.obj = "password_failed";
                    handler.sendMessage(message);
                }else if(Objects.equals(line, "userID_failed")){
                    //Toast.makeText(LogInActivity.this, "该用户不存在",Toast.LENGTH_SHORT).show();
                    Message message = Message.obtain();
                    message.what = 0x33 ;
                    message.obj = "userID_failed";
                    handler.sendMessage(message);
                }

                socket.shutdownOutput();
                socket.close(); // 关闭socket

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }

    }
}

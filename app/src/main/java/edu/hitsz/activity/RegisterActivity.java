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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.ResourceBundle;

import edu.hitsz.DAO.User;
import edu.hitsz.DAO.UserDaoImpl;
import edu.hitsz.R;

public class RegisterActivity extends AppCompatActivity {

    private final static String TAG = "RegisterActivity";

    private Button login;
    private EditText register_ID;
    private EditText register_password;
    private EditText register_confirm_password;

    private String user_ID;
    private String user_password;
    private String user_confirm_password;

    private Handler handler;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        login = findViewById(R.id.register_btn);
        register_ID = findViewById(R.id.register_ID);
        register_password = findViewById(R.id.register_password);
        register_confirm_password = findViewById(R.id.confirm_password);

        register_ID.setTextColor(Color.WHITE);
        register_password.setTextColor(Color.WHITE);
        register_confirm_password.setTextColor(Color.WHITE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.register_btn) {
                    user_ID = register_ID.getText().toString();
                    user_password = register_password.getText().toString();
                    user_confirm_password = register_confirm_password.getText().toString();

                    //判断两次输入是否相同
                    if(! user_password.equals(user_confirm_password)){
                        Toast.makeText(RegisterActivity.this,"两次密码输入不同，请重新输入",Toast.LENGTH_LONG).show();
                    }else{
                        //通过新线程连接socket
                        new Thread(new Connect()).start();
                    }

                }
            }
        });


        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what == 0x44){
                    Toast.makeText(RegisterActivity.this, "注册成功",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
                }else if(msg.what == 0x55){
                    Toast.makeText(RegisterActivity.this, "该用户ID已存在",Toast.LENGTH_SHORT).show();
                }else if(msg.what == 0x66){
                    Toast.makeText(RegisterActivity.this, "注册失败",Toast.LENGTH_SHORT).show();
                }
            }
        };

    }

    public class Connect implements Runnable {
        @Override
        public void run() {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(MainActivity.IP,9999),5000);

                /**
                 * 向服务器发送请求码
                 */
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

                /**
                 * 客户端组装 JSON对象
                 */
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("operation", "register");
                jsonObject.put("ID",user_ID);
                jsonObject.put("PSW",user_password);
                Log.e(TAG, jsonObject.toString());
                // 发送 JSON 对象给服务器
                out.println(jsonObject.toString());

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = reader.readLine(); // 读取另一头传过来的数据
                System.out.println(line+"12345");

                if(Objects.equals(line, "register_success")){
                    //Intent startGame = new Intent(LogInActivity.this, GameActivity.class);
                    //startActivity(startGame);
                    Message message = Message.obtain();
                    message.what = 0x44 ;
                    message.obj = "register_success";
                    handler.sendMessage(message);
                }else if(Objects.equals(line, "register_failed")){
                    //Toast.makeText(LogInActivity.this, "用户名或密码错误",Toast.LENGTH_SHORT).show();
                    Message message = Message.obtain();
                    message.what = 0x55 ;
                    message.obj = "register_failed";
                    handler.sendMessage(message);
                }else{
                    //Toast.makeText(LogInActivity.this, "该用户不存在",Toast.LENGTH_SHORT).show();
                    Message message = Message.obtain();
                    message.what = 0x66 ;
                    message.obj = "userID_failed";
                    handler.sendMessage(message);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

